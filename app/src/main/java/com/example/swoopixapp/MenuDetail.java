package com.example.swoopixapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class MenuDetail extends AppCompatActivity {
    
    private TextView coffeeName;
    private TextView coffeePrice;
    private ImageView coffeeImage;
    private TextView quantity;
    private MaterialButton btnIncrement;
    private MaterialButton btnDecrement;
    private MaterialButton btnAddToCart;
    private ChipGroup sizeChipGroup;
    private ChipGroup sugarChipGroup;
    
    private int currentQuantity = 1;
    private String selectedSize = "Medium";
    private String selectedSugar = "Normal";
    private String selectedPrice;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        
        // Inisialisasi views
        coffeeName = findViewById(R.id.tv_coffee_name);
        coffeePrice = findViewById(R.id.tv_coffee_price);
        coffeeImage = findViewById(R.id.iv_coffee_image);
        quantity = findViewById(R.id.tv_quantity);
        btnIncrement = findViewById(R.id.btn_increment);
        btnDecrement = findViewById(R.id.btn_decrement);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        sizeChipGroup = findViewById(R.id.size_chip_group);
        sugarChipGroup = findViewById(R.id.sugar_chip_group);
        
        // Ambil data dari intent
        String name = getIntent().getStringExtra("COFFEE_NAME");
        selectedPrice = getIntent().getStringExtra("COFFEE_PRICE");
        int imageRes = getIntent().getIntExtra("COFFEE_IMAGE", R.drawable.ic_home);
        
        // Set data ke view
        coffeeName.setText(name);
        coffeePrice.setText(selectedPrice);
        coffeeImage.setImageResource(imageRes);
        
        // Set back button
        ImageView backButton = findViewById(R.id.iv_back);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
        
        // Inisialisasi quantity
        quantity.setText(String.valueOf(currentQuantity));
        
        // Setup onClick listeners
        setupQuantityButtons();
        setupChipGroups();
        setupAddToCartButton();
    }
    
    private void setupQuantityButtons() {
        btnIncrement.setOnClickListener(v -> {
            if (currentQuantity < 10) {
                currentQuantity++;
                quantity.setText(String.valueOf(currentQuantity));
            }
        });
        
        btnDecrement.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                quantity.setText(String.valueOf(currentQuantity));
            }
        });
    }
    
    private void setupChipGroups() {
        // Size chip group listener
        sizeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                selectedSize = chip.getText().toString();
                updatePrice();
            }
        });
        
        // Sugar chip group listener
        sugarChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                selectedSugar = chip.getText().toString();
            }
        });
    }
    
    private void updatePrice() {
        // Update harga berdasarkan ukuran
        String basePrice = selectedPrice.replace("Rp ", "").replace(".", "");
        int price = Integer.parseInt(basePrice);
        
        if (selectedSize.equals("Large")) {
            price += 5000;
        } else if (selectedSize.equals("Small")) {
            price -= 3000;
        }
        
        // Format harga
        String formattedPrice = "Rp " + formatPrice(price);
        coffeePrice.setText(formattedPrice);
    }
    
    private String formatPrice(int price) {
        String priceString = String.valueOf(price);
        StringBuilder formatted = new StringBuilder();
        
        for (int i = 0; i < priceString.length(); i++) {
            if (i > 0 && (priceString.length() - i) % 3 == 0) {
                formatted.append(".");
            }
            formatted.append(priceString.charAt(i));
        }
        
        return formatted.toString();
    }
    
    private void setupAddToCartButton() {
        btnAddToCart.setOnClickListener(v -> {
            // Tampilkan alert dialog
            showOrderConfirmation();
        });
    }
    
    private void showOrderConfirmation() {
        String message = String.format(
                "%s\nUkuran: %s\nGula: %s\nJumlah: %d\nTotal: %s",
                coffeeName.getText(),
                selectedSize,
                selectedSugar,
                currentQuantity,
                coffeePrice.getText()
        );
        
        new AlertDialog.Builder(this)
                .setTitle("Tambahkan ke Keranjang")
                .setMessage(message)
                .setPositiveButton("Ya", (dialog, which) -> {
                    Toast.makeText(this, "Berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
} 