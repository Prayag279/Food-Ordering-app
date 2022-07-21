package com.example.a2amunch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void createacc(View view) {
        Intent i = new Intent(this,otpAuth.class);
        startActivity(i);
    }

    public void login(View view) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }


}