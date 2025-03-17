package com.example.swoopixapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable dynamic colors if available (Android 12+)
        DynamicColors.applyToActivityIfAvailable(this);
        
        setContentView(R.layout.activity_main);
        
        // Setup edge-to-edge
        getWindow().setDecorFitsSystemWindows(false);
    }
}