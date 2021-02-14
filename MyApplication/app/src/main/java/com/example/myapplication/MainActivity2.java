package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
      private ImageButton listBtn;
    private ImageButton recordBtn;
    private TextView filenameText;
    private EditText audio_name,audio_des;
    private boolean isRecording = false;

    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private ProgressDialog mDialog;
    private Chronometer timer;
    String recordPath=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
        filenameText = findViewById(R.id.record_filename);
        mDialog =new ProgressDialog(this);
        audio_des=findViewById(R.id.audio_des1);
        audio_name=findViewById(R.id.audio_name1);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    stopRecording();

                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped));
                    isRecording = false;
                } else {
                    if(checkPermissions()) {
                        startRecording();
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording));
                        isRecording = true;
                    }
                }

            }
        });
    }
    private void stopRecording() {

        timer.stop();
        filenameText.setText("Recording Stopped, File Saved : ");
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        uploadAudio();
    }

    private void startRecording() {

        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        recordPath = this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();
        recordFile = "Recording_" + formatter.format(now) + ".3gp";
        filenameText.setText("Recording, File Name : " + recordFile);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            stopRecording();
        }
    }
    private void uploadAudio() {
        /*mDialog.setMessage("Uploading Audio....");
        mDialog.show();
        StorageReference filepath= mStorageRef.child(recordFile);
        Uri uri = Uri.fromFile(new File(recordPath + "/" + recordFile));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mDialog.dismiss();
                Toast.makeText(MainActivity2.this,"file uploaded",Toast.LENGTH_LONG).show();
            }
        });

    }*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        long secondsT = (SystemClock.elapsedRealtime() - timer.getBase()) / 1000;
        String asText = (secondsT / 60) + ":" + (secondsT % 60);

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Details");
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", ID);
        hashMap.put("audio_name", audio_name.getText().toString().trim());
        hashMap.put("audio_des", audio_des.getText().toString().trim());
        hashMap.put("date",date);
        hashMap.put("duration", asText);
        mDatabaseRef.child(audio_name.getText().toString().trim()).setValue(hashMap);
        Toast.makeText(MainActivity2.this, "Upload successfully", Toast.LENGTH_LONG).show();
    }
}