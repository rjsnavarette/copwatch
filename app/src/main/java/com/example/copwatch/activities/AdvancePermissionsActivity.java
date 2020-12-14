package com.example.copwatch.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.example.copwatch.R;

public class AdvancePermissionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_permissions);
        ButterKnife.bind(this);
    }
}