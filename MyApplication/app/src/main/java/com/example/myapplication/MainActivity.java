package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    EditText audio_name, audio_des;
    Button upload, file_manager, record;
    TextView recordlabel;
    private Uri uri;
    private static final int CHOOSE_AUDIO = 1;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private String fileName = null;
    private ProgressDialog mDialog;
    private static  final int AUDIO_REQUEST=1;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio_des = findViewById(R.id.audio_des);
        audio_name = findViewById(R.id.audio_name);

        upload = findViewById(R.id.upload);
        file_manager = findViewById(R.id.file_manager);
        recordlabel=findViewById(R.id.recordlabel);
        record = findViewById(R.id.record);
        mDialog =new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Details");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
                } else {

                    uploadImage();
                }

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
       file_manager.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showFileChoose();

           }
       });
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_AUDIO);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDIO_REQUEST && resultCode == Activity.RESULT_OK  && data != null) {
            Uri uri=data.getData();
            StorageReference filepath = mStorageRef.child("Photo").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this,"Uploading Finish",Toast.LENGTH_SHORT).show();

                }
            });

        }

        if (requestCode == CHOOSE_AUDIO && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", ID);
        hashMap.put("audio_name", audio_name.getText().toString().trim());
        hashMap.put("audio_des", audio_des.getText().toString().trim());
        hashMap.put("date",date);
        hashMap.put("duration","0.0");
        mDatabaseRef.child(audio_name.getText().toString().trim()).setValue(hashMap);
        Toast.makeText(MainActivity.this, "Upload successfully", Toast.LENGTH_LONG).show();
       /* if (uri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

            mUploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uploadID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", uploadID);
                                    hashMap.put("imageUrl", uri.toString());
                                    hashMap.put("status", "online");
                                    hashMap.put("typingto", "noOne ");
                                    mDatabaseRef.child(uploadID).setValue(hashMap);
                                    Toast.makeText(MainActivity.this, "Upload successfully", Toast.LENGTH_LONG).show();


                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }*/
    }
}