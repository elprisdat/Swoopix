package com.example.swoopixapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        
        // Inisialisasi SharedPreferences untuk menyimpan data user
        sharedPreferences = getSharedPreferences("SwoopixPrefs", MODE_PRIVATE);
        
        // Cek apakah user sudah login sebelumnya
        if (isLoggedIn()) {
            startMainActivity();
            return;
        }
        
        // Inisialisasi UI components
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.btnLogin);
        signupTextView = findViewById(R.id.signupText);
        
        // Set listener untuk tombol login
        loginButton.setOnClickListener(v -> attemptLogin());
        
        // Set listener untuk text signup
        signupTextView.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, signup.class);
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
    
    /**
     * Mencoba untuk login dengan data yang diinputkan
     */
    private void attemptLogin() {
        // Reset errors
        emailEditText.setError(null);
        passwordEditText.setError(null);
        
        // Ambil nilai dari form
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Flag untuk melacak apakah ada error
        boolean cancel = false;
        View focusView = null;
        
        // Validasi password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password diperlukan");
            focusView = passwordEditText;
            cancel = true;
        } else if (password.length() < 4) {
            passwordEditText.setError("Password terlalu pendek");
            focusView = passwordEditText;
            cancel = true;
        }
        
        // Validasi email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email diperlukan");
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError("Email tidak valid");
            focusView = emailEditText;
            cancel = true;
        }
        
        if (cancel) {
            // Ada error, fokus ke field yang error
            focusView.requestFocus();
        } else {
            // Lakukan proses login dengan sharedPreferences
            String savedEmail = sharedPreferences.getString("userEmail", "");
            String savedPassword = sharedPreferences.getString("userPassword", "");
            
            if (savedEmail.equals(email) && savedPassword.equals(password)) {
                // Login berhasil
                saveLoginStatus(true, email);
                startMainActivity();
            } else {
                // Cek apakah ini adalah akun default
                if (email.equals("user@swoopix.com") && password.equals("password")) {
                    // Login berhasil dengan akun default
                    saveLoginStatus(true, email);
                    startMainActivity();
                } else {
                    // Login gagal
                    Toast.makeText(this, "Email atau password salah. Silakan daftar jika belum memiliki akun.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    
    /**
     * Memeriksa apakah email valid
     */
    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Menyimpan status login ke SharedPreferences
     */
    private void saveLoginStatus(boolean isLoggedIn, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putString("loggedInEmail", email);
        editor.apply();
    }
    
    /**
     * Memulai MainActivity
     */
    private void startMainActivity() {
        Intent intent = new Intent(login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}