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

public class signup extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private TextView loginTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        
        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("SwoopixPrefs", MODE_PRIVATE);
        
        // Inisialisasi UI components
        nameEditText = findViewById(R.id.editTextName1);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        signupButton = findViewById(R.id.signup2);
        loginTextView = findViewById(R.id.loginText);
        
        // Set listener untuk tombol signup
        signupButton.setOnClickListener(v -> attemptSignup());
        
        // Set listener untuk text login
        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(signup.this, login.class);
            startActivity(intent);
            finish();
        });
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    /**
     * Mencoba untuk signup dengan data yang diinputkan
     */
    private void attemptSignup() {
        // Reset errors
        nameEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);
        
        // Ambil nilai dari form
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Flag untuk melacak apakah ada error
        boolean cancel = false;
        View focusView = null;
        
        // Validasi nama
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Nama diperlukan");
            focusView = nameEditText;
            cancel = true;
        } else if (name.length() < 3) {
            nameEditText.setError("Nama terlalu pendek");
            focusView = nameEditText;
            cancel = true;
        }
        
        // Validasi password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password diperlukan");
            focusView = passwordEditText;
            cancel = true;
        } else if (password.length() < 4) {
            passwordEditText.setError("Password minimal 4 karakter");
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
            // Simpan data user ke SharedPreferences
            saveUserData(name, email, password);
            
            // Tunjukkan pesan sukses
            Toast.makeText(this, "Pendaftaran berhasil. Silakan login.", Toast.LENGTH_SHORT).show();
            
            // Arahkan ke halaman login
            Intent intent = new Intent(signup.this, login.class);
            startActivity(intent);
            finish();
        }
    }
    
    /**
     * Memeriksa apakah email valid
     */
    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Menyimpan data user ke SharedPreferences
     */
    private void saveUserData(String name, String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", name);
        editor.putString("userEmail", email);
        editor.putString("userPassword", password);
        editor.apply();
        
        Log.d(TAG, "User data saved: " + name + ", " + email);
    }
}