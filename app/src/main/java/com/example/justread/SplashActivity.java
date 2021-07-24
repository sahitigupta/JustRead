package com.example.justread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.justread.UserActivities.SigningActivity;

public class SplashActivity extends AppCompatActivity {

    // TODO: Logic for edit book details (Separate module in itself - a fresh activity) - requires advanced RV concepts
    // TODO: Logic for delete book (a button, database part) - partly done - even this requires advanced recycler view(RV) concepts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent signing = new Intent(SplashActivity.this, SigningActivity.class);
                startActivity(signing);
                finish();
            }
        }, 3000);
    }
}