package com.example.swoopixapp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TextView coffeeDescription;
    private TextView ingredientsLabel;
    private TextView coffeeIngredients;
    private TextView ratingValue;
    private RecyclerView relatedProductsRecyclerView;
    private FloatingActionButton favoriteButton;
    
    private int currentQuantity = 1;
    private String selectedSize = "Medium";
    private String selectedSugar = "Normal";
    private String selectedPrice;
    private boolean isFavorite = false;
    
    // Map untuk deskripsi kopi
    private Map<String, String> coffeeDescriptions = new HashMap<>();
    private Map<String, String> coffeeIngredientsList = new HashMap<>();
    private Map<String, Float> coffeeRatings = new HashMap<>();
    
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
        btnAddToCart = findViewById(R.id.btnAddToCart);
        sizeChipGroup = findViewById(R.id.size_chip_group);
        sugarChipGroup = findViewById(R.id.sugar_chip_group);
        coffeeDescription = findViewById(R.id.tv_coffee_description);
        ingredientsLabel = findViewById(R.id.tv_ingredients_label);
        coffeeIngredients = findViewById(R.id.tv_coffee_ingredients);
        ratingValue = findViewById(R.id.tv_rating_value);
        relatedProductsRecyclerView = findViewById(R.id.related_products_recycler);
        favoriteButton = findViewById(R.id.btn_favorite);
        
        // Inisialisasi data deskripsi kopi
        initCoffeeData();
        
        // Ambil data dari intent
        String name = getIntent().getStringExtra("COFFEE_NAME");
        selectedPrice = getIntent().getStringExtra("COFFEE_PRICE");
        int imageRes = getIntent().getIntExtra("COFFEE_IMAGE", R.drawable.ic_home);
        
        // Set data ke view
        coffeeName.setText(name);
        coffeePrice.setText(selectedPrice);
        coffeeImage.setImageResource(imageRes);
        
        // Set deskripsi kopi berdasarkan nama
        setProductDetails(name);
        
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
        setupFavoriteButton();
        
        // Setup produk terkait
        setupRelatedProducts(name);
        
        // Animasi untuk konten
        animateContent();
    }
    
    private void initCoffeeData() {
        // Deskripsi kopi
        coffeeDescriptions.put("Espresso", "Minuman kopi yang kental dan kuat, diseduh dengan tekanan tinggi untuk mengekstrak rasa dan aroma terbaik dari biji kopi.");
        coffeeDescriptions.put("Americano", "Espresso yang dicampur dengan air panas, memberikan rasa kopi yang ringan tetapi tetap kaya akan aroma.");
        coffeeDescriptions.put("Caramel Macchiato", "Espresso yang dicampur dengan susu yang dipanaskan dengan uap, ditambah sirup vanilla, dan dihias dengan karamel.");
        coffeeDescriptions.put("Cappuccino", "Kopi klasik Italia yang terdiri dari espresso, susu yang dipanaskan dengan uap, dan busa susu yang lembut di bagian atas.");
        coffeeDescriptions.put("Java Chip Frappuccino", "Minuman blended yang menyegarkan dengan campuran espresso, susu, es, cokelat dan potongan chip cokelat.");
        coffeeDescriptions.put("Caramel Frappuccino", "Minuman dingin yang creamy dengan sentuhan karamel, dicampur dengan es dan whipped cream di atasnya.");
        coffeeDescriptions.put("Mocha Frappuccino", "Perpaduan sempurna antara cokelat dan kopi, diblend dengan es dan dihias dengan whipped cream.");
        coffeeDescriptions.put("Cold Brew", "Kopi yang diseduh dengan air dingin selama 20 jam, menghasilkan rasa yang halus dan lebih rendah keasaman.");
        coffeeDescriptions.put("Vanilla Sweet Cream Cold Brew", "Cold brew yang ditambahkan dengan krim manis vanilla yang lembut, menciptakan kelembutan di setiap tegukan.");
        coffeeDescriptions.put("Dark Mocha Cold Brew", "Cold brew dengan sentuhan cokelat gelap yang kaya, menciptakan harmoni rasa pahit dan manis.");
        coffeeDescriptions.put("Matcha Latte", "Bubuk teh hijau premium yang dicampur dengan susu hangat dan sedikit pemanis, kaya akan antioksidan.");
        coffeeDescriptions.put("Earl Grey Tea", "Teh hitam klasik dengan aroma bergamot yang menyegarkan, disajikan dengan lemon atau susu sesuai selera.");
        coffeeDescriptions.put("Green Tea Latte", "Teh hijau berkualitas tinggi yang dicampur dengan susu hangat dan sedikit pemanis, memberikan ketenangan dalam setiap tegukan.");
        
        // Daftar bahan setiap kopi
        coffeeIngredientsList.put("Espresso", "Biji kopi arabika premium yang digiling halus dan diseduh dengan tekanan tinggi.");
        coffeeIngredientsList.put("Americano", "Espresso, air panas.");
        coffeeIngredientsList.put("Caramel Macchiato", "Espresso, susu segar, sirup vanilla, saus karamel.");
        coffeeIngredientsList.put("Cappuccino", "Espresso, susu segar, busa susu.");
        coffeeIngredientsList.put("Java Chip Frappuccino", "Espresso, susu, es, bubuk cokelat, potongan cokelat, whipped cream.");
        coffeeIngredientsList.put("Caramel Frappuccino", "Espresso, susu, es, sirup karamel, saus karamel, whipped cream.");
        coffeeIngredientsList.put("Mocha Frappuccino", "Espresso, susu, es, saus cokelat, whipped cream.");
        coffeeIngredientsList.put("Cold Brew", "Biji kopi arabika yang diseduh dengan air dingin selama 20 jam.");
        coffeeIngredientsList.put("Vanilla Sweet Cream Cold Brew", "Cold brew, krim segar, sirup vanilla.");
        coffeeIngredientsList.put("Dark Mocha Cold Brew", "Cold brew, saus cokelat gelap, krim segar.");
        coffeeIngredientsList.put("Matcha Latte", "Bubuk matcha kualitas premium, susu segar, sedikit gula.");
        coffeeIngredientsList.put("Earl Grey Tea", "Daun teh hitam, minyak bergamot, bunga cornflower biru.");
        coffeeIngredientsList.put("Green Tea Latte", "Bubuk teh hijau premium, susu segar, sedikit madu.");
        
        // Rating produk
        coffeeRatings.put("Espresso", 4.7f);
        coffeeRatings.put("Americano", 4.5f);
        coffeeRatings.put("Caramel Macchiato", 4.8f);
        coffeeRatings.put("Cappuccino", 4.6f);
        coffeeRatings.put("Java Chip Frappuccino", 4.9f);
        coffeeRatings.put("Caramel Frappuccino", 4.7f);
        coffeeRatings.put("Mocha Frappuccino", 4.6f);
        coffeeRatings.put("Cold Brew", 4.5f);
        coffeeRatings.put("Vanilla Sweet Cream Cold Brew", 4.8f);
        coffeeRatings.put("Dark Mocha Cold Brew", 4.7f);
        coffeeRatings.put("Matcha Latte", 4.6f);
        coffeeRatings.put("Earl Grey Tea", 4.4f);
        coffeeRatings.put("Green Tea Latte", 4.5f);
    }
    
    private void setProductDetails(String productName) {
        // Set deskripsi produk
        String description = coffeeDescriptions.get(productName);
        if (description != null) {
            coffeeDescription.setText(description);
        } else {
            coffeeDescription.setText("Nikmatnya kopi premium pilihan dengan cita rasa khas yang akan memanjakan lidah Anda.");
        }
        
        // Set bahan-bahan
        String ingredients = coffeeIngredientsList.get(productName);
        if (ingredients != null) {
            coffeeIngredients.setText(ingredients);
        } else {
            coffeeIngredients.setText("Biji kopi pilihan, air, dan sentuhan khas barista kami.");
        }
        
        // Set rating
        Float rating = coffeeRatings.get(productName);
        if (rating != null) {
            ratingValue.setText(String.format("%.1f/5.0", rating));
        } else {
            ratingValue.setText("4.5/5.0");
        }
    }
    
    private void setupQuantityButtons() {
        btnIncrement.setOnClickListener(v -> {
            if (currentQuantity < 10) {
                currentQuantity++;
                quantity.setText(String.valueOf(currentQuantity));
                updatePrice();
            }
        });
        
        btnDecrement.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                quantity.setText(String.valueOf(currentQuantity));
                updatePrice();
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
        // Update harga berdasarkan ukuran dan quantity
        String basePrice = selectedPrice.replace("Rp ", "").replace(".", "");
        int price = Integer.parseInt(basePrice);
        
        if (selectedSize.equals("Large")) {
            price += 5000;
        } else if (selectedSize.equals("Small")) {
            price -= 3000;
        }
        
        // Hitung total harga berdasarkan quantity
        int totalPrice = price * currentQuantity;
        
        // Format harga
        String formattedPrice = "Rp " + formatPrice(totalPrice);
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
            // Animasi button
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            btnAddToCart.startAnimation(animation);
            
            // Tampilkan alert dialog
            showOrderConfirmation();
        });
    }
    
    private void setupFavoriteButton() {
        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                Toast.makeText(this, coffeeName.getText() + " ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(this, coffeeName.getText() + " dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
            
            // Animasi button
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            favoriteButton.startAnimation(animation);
        });
    }
    
    private void setupRelatedProducts(String currentProduct) {
        // Daftar produk terkait (sederhana - dapat ditingkatkan dengan algoritma rekomendasi lebih kompleks)
        List<String> relatedNames = new ArrayList<>();
        List<Integer> relatedImages = new ArrayList<>();
        List<String> relatedPrices = new ArrayList<>();
        
        // Menambahkan produk terkait berdasarkan kategori
        if (currentProduct.contains("Espresso") || currentProduct.contains("Americano") || 
            currentProduct.contains("Macchiato") || currentProduct.contains("Cappuccino")) {
            // Produk terkait untuk kopi espresso-based
            relatedNames.add("Espresso");
            relatedNames.add("Americano");
            relatedNames.add("Cappuccino");
            
            relatedImages.add(R.drawable.ic_coffee_bean);
            relatedImages.add(R.drawable.ic_coffee_cup);
            relatedImages.add(R.drawable.ic_cappuccino);
            
            relatedPrices.add("Rp 20.000");
            relatedPrices.add("Rp 28.000");
            relatedPrices.add("Rp 32.000");
        } else if (currentProduct.contains("Frappuccino")) {
            // Produk terkait untuk Frappuccino
            relatedNames.add("Java Chip Frappuccino");
            relatedNames.add("Caramel Frappuccino");
            relatedNames.add("Mocha Frappuccino");
            
            relatedImages.add(R.drawable.ic_frappuccino);
            relatedImages.add(R.drawable.ic_frappuccino);
            relatedImages.add(R.drawable.ic_frappuccino);
            
            relatedPrices.add("Rp 45.000");
            relatedPrices.add("Rp 43.000");
            relatedPrices.add("Rp 42.000");
        } else if (currentProduct.contains("Tea") || currentProduct.contains("Matcha")) {
            // Produk terkait untuk teh
            relatedNames.add("Matcha Latte");
            relatedNames.add("Earl Grey Tea");
            relatedNames.add("Green Tea Latte");
            
            relatedImages.add(R.drawable.ic_matcha);
            relatedImages.add(R.drawable.ic_tea);
            relatedImages.add(R.drawable.ic_matcha);
            
            relatedPrices.add("Rp 40.000");
            relatedPrices.add("Rp 28.000");
            relatedPrices.add("Rp 35.000");
        } else {
            // Default related products
            relatedNames.add("Cappuccino");
            relatedNames.add("Caramel Macchiato");
            relatedNames.add("Cold Brew");
            
            relatedImages.add(R.drawable.ic_cappuccino);
            relatedImages.add(R.drawable.ic_latte);
            relatedImages.add(R.drawable.ic_coffee_cup);
            
            relatedPrices.add("Rp 32.000");
            relatedPrices.add("Rp 38.000");
            relatedPrices.add("Rp 30.000");
        }
        
        // Filter out current product from related products
        for (int i = 0; i < relatedNames.size(); i++) {
            if (relatedNames.get(i).equals(currentProduct)) {
                relatedNames.remove(i);
                relatedImages.remove(i);
                relatedPrices.remove(i);
                break;
            }
        }
        
        // Setup adapter for related products
        ServiceAdapter relatedAdapter = new ServiceAdapter(this, relatedNames, relatedImages, relatedPrices);
        relatedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedProductsRecyclerView.setAdapter(relatedAdapter);
    }
    
    private void animateContent() {
        // Animate product image
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(500);
        coffeeImage.startAnimation(fadeIn);
        
        // Animate product details with slight delay
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideUp.setDuration(400);
        slideUp.setStartOffset(200);
        coffeeName.startAnimation(slideUp);
        coffeePrice.startAnimation(slideUp);
        
        // Animate add to cart button
        Animation slideIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideIn.setDuration(500);
        slideIn.setStartOffset(400);
        btnAddToCart.startAnimation(slideIn);
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