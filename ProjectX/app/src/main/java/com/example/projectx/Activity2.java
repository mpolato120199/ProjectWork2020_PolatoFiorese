package com.example.projectx;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        getSupportActionBar().setTitle("Activity2");
    }
}
