package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    TextView showemail,showpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        showemail=findViewById(R.id.show_email);
        showpassword=findViewById(R.id.show_password);

        Intent intent =getIntent();
        final String email =intent.getStringExtra("email");
        final String password =intent.getStringExtra("password");

        showpassword.setText(password);
        showemail.setText(email);
    }
}