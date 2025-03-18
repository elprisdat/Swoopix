package com.example.swoopixapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome extends AppCompatActivity {
    private Button loginButton;
    private Button signupButton;
    private ImageView logoImageView;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        
        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("SwoopixPrefs", MODE_PRIVATE);
        
        // Cek apakah user sudah login
        if (isLoggedIn()) {
            // Jika sudah login, langsung ke MainActivity
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(welcome.this, MainActivity.class));
                finish();
            }, 1000); // Delay 1 detik untuk menampilkan splash screen
            return;
        }
        
        // Inisialisasi UI components
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        logoImageView = findViewById(R.id.logoImageView);
        titleTextView = findViewById(R.id.titleTextView);
        subtitleTextView = findViewById(R.id.subtitleTextView);
        
        // Siapkan animasi
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1500);
        
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideUp.setDuration(1000);
        
        // Terapkan animasi
        logoImageView.startAnimation(fadeIn);
        titleTextView.startAnimation(fadeIn);
        subtitleTextView.startAnimation(fadeIn);
        
        // Tunda munculnya button
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loginButton.setVisibility(View.VISIBLE);
            signupButton.setVisibility(View.VISIBLE);
            loginButton.startAnimation(slideUp);
            signupButton.startAnimation(slideUp);
        }, 1000);
        
        // Set listeners untuk button
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(welcome.this, login.class);
            startActivity(intent);
        });
        
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(welcome.this, signup.class);
            startActivity(intent);
        });
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    /**
     * Memeriksa apakah user sudah login sebelumnya
     */
    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}