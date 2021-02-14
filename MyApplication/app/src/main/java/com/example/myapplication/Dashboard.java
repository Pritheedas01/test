package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    TextView username;
    Button new_audio,upload_audio,logout,profile;
    String name, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username=findViewById(R.id.username);
        new_audio=findViewById(R.id.new_audio);
        upload_audio=findViewById(R.id.upload_audio);
        logout=findViewById(R.id.log_out);
        profile=findViewById(R.id.profile);

        Intent intent =getIntent();
        final String user =intent.getStringExtra("username");
        username.setText(user);

        final String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Query usQuery=reference.orderByChild("id").equalTo(myid);
        usQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    name=""+ds.child("username").getValue();
                    email=""+ds.child("email").getValue();
                    password=""+ds.child("password").getValue();
                    username.setText(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next= new Intent(Dashboard.this,Profile.class);
                next.putExtra("email",email);
                next.putExtra("password",password);
                startActivity(next);
            }
        });
        new_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Dashboard.this,MainActivity.class);
                next.putExtra("email",email);
                next.putExtra("password",password);
                startActivity(next);
            }
        });
        upload_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Dashboard.this,Upload_list.class);
                startActivity(next);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent next = new Intent(Dashboard.this,Login.class);
                startActivity(next);
            }
        });
    }

}