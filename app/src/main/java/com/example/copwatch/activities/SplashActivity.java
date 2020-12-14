package com.example.copwatch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.copwatch.R;

public class SplashActivity extends AppCompatActivity {

    private final Handler timerHandler = new Handler();
    private static final int SPLASH_DISPLAY_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        timerHandler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out);
        }, SPLASH_DISPLAY_TIME);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}