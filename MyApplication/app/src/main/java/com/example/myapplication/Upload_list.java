package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Upload_list extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterData adapterData;
    DatabaseReference reference;
    List<ModelData> list;
    EditText search_name;
    ImageView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        recyclerView = findViewById(R.id.recycler_view_data);
        recyclerView.setHasFixedSize(true);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        search=findViewById(R.id.search);
        search_name=findViewById(R.id.search_name);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Details");
                Query usQuery=reference.orderByChild("audio_name").equalTo(search_name.getText().toString().trim());
                usQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            StringBuffer buffer = new StringBuffer();

                            buffer.append("Audio name:-:"+ds.child("audio_name").getValue()+"\n");
                            buffer.append("Audio description:- :"+ds.child("audio_des").getValue()+"\n");
                            buffer.append("Date :"+ds.child("date").getValue()+"\n");
                            buffer.append("Duration :"+ds.child("duration").getValue()+"\n");
                            showMessage("Details",buffer.toString());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        final String username1=preferences.getString("username","");

        list=new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Details").child(username1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ModelData p = dataSnapshot1.getValue(ModelData.class);
                    String username1=""+dataSnapshot1.child("Username").getValue();
                    list.add(p);
                    // if (username1.equals(username)){
                    //     list.add(p);
                    //  }
                }
                adapterData = new AdapterData(Upload_list.this, list);
                recyclerView.setAdapter(adapterData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Upload_list.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();


    }
}