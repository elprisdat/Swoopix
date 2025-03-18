package com.example.swoopixapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class profile extends Fragment {
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView rewardsValueTextView;
    private View logoutButton;
    private View editProfileButton;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // Inisialisasi SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("SwoopixPrefs", Context.MODE_PRIVATE);
        
        // Inisialisasi view components
        userNameTextView = view.findViewById(R.id.user_name);
        userEmailTextView = view.findViewById(R.id.user_email);
        rewardsValueTextView = view.findViewById(R.id.rewards_value);
        logoutButton = view.findViewById(R.id.logout_item);
        editProfileButton = view.findViewById(R.id.personal_info_item);
        
        // Load data pengguna dari SharedPreferences
        loadUserData();
        
        // Set listener untuk tombol logout
        logoutButton.setOnClickListener(v -> logoutUser());
        
        // Set listener untuk tombol edit profile
        editProfileButton.setOnClickListener(v -> editProfile());
        
        return view;
    }
    
    /**
     * Memuat data pengguna dari SharedPreferences
     */
    private void loadUserData() {
        String userName = sharedPreferences.getString("userName", "Pengguna Swoopix");
        String userEmail = sharedPreferences.getString("userEmail", "guest@swoopix.com");
        int rewardsPoints = sharedPreferences.getInt("rewardsPoints", 0);
        
        // Set data ke view
        userNameTextView.setText(userName);
        userEmailTextView.setText(userEmail);
        rewardsValueTextView.setText(rewardsPoints + " Points");
        
        // Jika user adalah user default, tambahkan pesan "Default User"
        if (userEmail.equals("user@swoopix.com")) {
            userNameTextView.setText(userName + " (Default)");
        }
        
        // Jika user belum login, tampilkan pesan
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            userNameTextView.setText("Guest User (Belum Login)");
            userEmailTextView.setText("Silakan login untuk melihat profil Anda");
            rewardsValueTextView.setText("0 Points");
        }
    }
    
    /**
     * Melakukan logout user
     */
    private void logoutUser() {
        // Hapus status login
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        
        // Tampilkan pesan
        Toast.makeText(getContext(), "Anda telah berhasil logout", Toast.LENGTH_SHORT).show();
        
        // Kembali ke halaman login
        Intent intent = new Intent(getActivity(), login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    
    /**
     * Membuka halaman edit profile
     */
    private void editProfile() {
        // Untuk sekarang hanya tampilkan toast
        Toast.makeText(getContext(), "Fitur edit profile akan segera hadir", Toast.LENGTH_SHORT).show();
    }
}