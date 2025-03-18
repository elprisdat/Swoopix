package com.example.swoopixapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Paksa light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        
        super.onCreate(savedInstanceState);
        
        try {
            // Enable dynamic colors if available (Android 12+)
            DynamicColors.applyToActivityIfAvailable(this);
            
            setContentView(R.layout.activity_main);
            
            // Setup status bar untuk menangani transparansi
            setupStatusBar();
            
            // Setup Navigation
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            
            // Menggunakan NavHostFragment untuk mendapatkan NavController
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
                
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
            
                // Setup Bottom Navigation
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_order,
                    R.id.navigation_voucher,
                    R.id.navigation_notification,
                    R.id.navigation_profile
                ).build();
                
                // Setup BottomNavigationView dengan NavController
                NavigationUI.setupWithNavController(bottomNavigationView, navController);
                
                // Tambahkan listener manual untuk memastikan navigasi berfungsi
                bottomNavigationView.setOnItemSelectedListener(item -> {
                    try {
                        int itemId = item.getItemId();
                        return navigateToFragment(itemId);
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to fragment: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Navigasi gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                
                Toast.makeText(this, "Inisialisasi navigasi berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "NavHostFragment tidak ditemukan", Toast.LENGTH_LONG).show();
                Log.e(TAG, "NavHostFragment tidak ditemukan di layout");
            }
        } catch (Exception e) {
            // Tangani error untuk mencegah crash
            Log.e(TAG, "Error saat inisialisasi: " + e.getMessage(), e);
            Toast.makeText(this, "Terjadi kesalahan saat inisialisasi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Metode publik untuk navigasi antar fragment
     * @param fragmentId ID fragment tujuan
     * @return true jika navigasi berhasil, false jika tidak
     */
    public boolean navigateToFragment(int fragmentId) {
        try {
            if (navController == null) {
                Log.e(TAG, "NavController is null");
                return false;
            }
            
            if (fragmentId == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
                return true;
            } else if (fragmentId == R.id.navigation_order) {
                navController.navigate(R.id.navigation_order);
                return true;
            } else if (fragmentId == R.id.navigation_voucher) {
                navController.navigate(R.id.navigation_voucher);
                return true;
            } else if (fragmentId == R.id.navigation_notification) {
                navController.navigate(R.id.navigation_notification);
                return true;
            } else if (fragmentId == R.id.navigation_profile) {
                navController.navigate(R.id.navigation_profile);
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to fragment: " + e.getMessage());
            Toast.makeText(this, "Navigasi gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        try {
            return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
        } catch (Exception e) {
            Log.e(TAG, "Error saat navigateUp: " + e.getMessage(), e);
            return super.onSupportNavigateUp();
        }
    }

    /**
     * Mengatur status bar agar konten tidak tertimpa status bar
     */
    private void setupStatusBar() {
        // Memberi jarak (padding) antara status bar dengan konten
        // Berikan padding pada content utama agar tidak tertimpa oleh status bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        
        // Set status bar semi-transparan
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        
        // Untuk memastikan text dan icon status bar terlihat di background terang
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // Jika tema menggunakan warna terang, maka icon status bar menjadi gelap
            boolean isLightTheme = isLightTheme();
            getWindow().getDecorView().setSystemUiVisibility(
                    isLightTheme ? 
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN :
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }
    
    /**
     * Memeriksa apakah tema yang digunakan adalah tema terang atau gelap
     */
    private boolean isLightTheme() {
        // Dalam contoh ini, kita tetapkan tema gelap karena warna primary kita adalah hitam/gelap
        return false;
    }
}