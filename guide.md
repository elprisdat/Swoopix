# Panduan SwoopixApp

## Cara Membuat APK Debug

Untuk membuat file APK debug yang bisa diinstal di perangkat Android untuk keperluan pengujian, ikuti langkah-langkah berikut:

1. Buka terminal atau command prompt di direktori root proyek (tempat file `gradlew.bat` berada)
2. Jalankan perintah berikut:

   ```bash
   .\gradlew.bat assembleDebug
   ```

3. Tunggu hingga proses build selesai. Jika berhasil, Anda akan melihat pesan "BUILD SUCCESSFUL"
4. File APK debug akan tersedia di lokasi:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

5. File APK ini dapat diinstal langsung ke perangkat Android yang sudah mengaktifkan "Install dari sumber tidak dikenal" di pengaturan.

### Troubleshooting

Jika terjadi error saat build:
- Pastikan Java Development Kit (JDK) terinstal dengan benar
- Periksa variabel PATH dan JAVA_HOME di sistem
- Periksa pesan error yang ditampilkan untuk mengetahui masalah spesifik
- Coba jalankan `.\gradlew.bat clean` sebelum mencoba build kembali

## Cara Kerja Aplikasi

### 1. Inisialisasi Data

Aplikasi melakukan inisialisasi data dengan cara berikut:

- **Data Menu Kopi:** Semua data menu kopi diinisialisasi secara lokal di file `home.java` dalam metode `initializeCategoryData()`. Data dikelompokkan berdasarkan kategori (Espresso, Frappuccino, Cold Brew, dan Tea).

```java
private void initializeCategoryData() {
    // Inisialisasi data untuk kategori Espresso
    categoryProducts.put("Espresso", Arrays.asList(
        "Espresso", "Americano", "Cappuccino", "Latte", "Mocha", "Macchiato"
    ));
    categoryPrices.put("Espresso", Arrays.asList(
        "Rp 18.000", "Rp 22.000", "Rp 25.000", "Rp 25.000", "Rp 28.000", "Rp 24.000"
    ));
    // ... data untuk kategori lainnya ...
}
```

- **Data Promo:** Data promo diinisialisasi di metode `setupPromoCarousel()` di file `home.java`. Promo-promo disimpan sebagai objek `PromoItem` dengan atribut judul, deskripsi, tanggal berlaku, gambar, dan status (baru/tidak).

- **Detail Produk:** Informasi detail untuk setiap produk seperti deskripsi, bahan, dan rating diinisialisasi di `MenuDetail.java` melalui metode `initCoffeeData()`. Data ini termasuk deskripsi produk, daftar bahan, dan rating pengguna.

```java
private void initCoffeeData() {
    // Deskripsi kopi
    coffeeDescriptions.put("Espresso", "Minuman kopi yang kental dan kuat, diseduh dengan tekanan tinggi untuk mengekstrak rasa dan aroma terbaik dari biji kopi.");
    // ... deskripsi untuk produk lainnya ...
    
    // Daftar bahan setiap kopi
    coffeeIngredientsList.put("Espresso", "Biji kopi arabika premium yang digiling halus dan diseduh dengan tekanan tinggi.");
    // ... daftar bahan untuk produk lainnya ...
    
    // Rating produk
    coffeeRatings.put("Espresso", 4.7f);
    // ... rating untuk produk lainnya ...
}
```

### 2. Styling dan UI

Aplikasi menggunakan Material Design 3 dengan pendekatan styling sebagai berikut:

- **Tema Utama:** Menggunakan tema teal dan putih yang didefinisikan di `themes.xml` dengan parent `Theme.Material3.Light.NoActionBar`.

```xml
<style name="Theme.Swoopix" parent="Theme.Material3.Light.NoActionBar">
    <!-- Primary brand color -->
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryVariant">@color/primary_dark</item>
    <item name="colorOnPrimary">@color/white</item>
    <!-- ... konfigurasi warna lainnya ... -->
</style>
```

- **Skema Warna:** Menggunakan skema teal dan putih yang didefinisikan di `colors.xml`:
  - Primary: #003B40 (teal)
  - Primary Dark: #002B30
  - Primary Light: #004B50
  - Secondary: #FFFFFF (putih)
  - Warna aksen dan status untuk error, success, warning, info

- **Komponen UI:**
  - BottomNavigationView dengan label dan ikon
  - MaterialCardView untuk item dan kartu
  - Sistem grid untuk menampilkan item kopi
  - TabLayout dengan custom indicator untuk carousel
  - ChipGroup untuk pemilihan varian produk
  - Animasi transisi antar halaman dan komponen UI

- **Tematik Warna Kartu:** Menggunakan array warna untuk berbagai kartu produk dalam `ServiceAdapter.java` untuk memberikan tampilan yang dinamis dan menarik:

```java
// Teal & White theme colors
private static final int[] CARD_COLORS = {
    Color.parseColor("#003B40"), // Teal Primary
    Color.parseColor("#004B50"), // Teal Light
    Color.parseColor("#005F66"), // Teal Medium
    Color.parseColor("#006E76"), // Teal Dark
    Color.parseColor("#008891"), // Teal Accent
    Color.parseColor("#009DAA")  // Teal Bright
};
```

### 3. Pengambilan Data Cuaca

Aplikasi mengambil data cuaca dari OpenWeatherMap API:

1. **Konfigurasi Retrofit:**
   - Menggunakan Retrofit2 untuk API calls
   - Menggunakan Gson untuk konversi JSON

2. **Proses Request:**
   - Memanggil endpoint `/weather` dengan parameter koordinat Surabaya, API key, dan unit metrik
   - Menggunakan implementasi asynchronous dengan Callback

```java
private void fetchWeatherData() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    WeatherService service = retrofit.create(WeatherService.class);
    Call<WeatherResponse> call = service.getCurrentWeather(
        SURABAYA_LAT, SURABAYA_LON, WEATHER_API_KEY, "metric");

    call.enqueue(new Callback<WeatherResponse>() {
        @Override
        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                updateWeatherUI(response.body());
            }
        }
        // ... error handling ...
    });
}
```

3. **Parsing Data:**
   - Respons API dikonversi ke objek `WeatherResponse` dengan model yang sesuai
   - Data cuaca disimpan dalam `WeatherResponse.MainData` dan `WeatherResponse.WeatherData`

4. **Menampilkan Data:**
   - Suhu ditampilkan dalam widget cuaca
   - Kode kondisi cuaca dikonversi menjadi deskripsi dan ikon yang sesuai

### 4. Rekomendasi Minuman Berdasarkan Cuaca

Aplikasi memberikan rekomendasi minuman berdasarkan kondisi cuaca saat ini:

1. **Mapping Rekomendasi:**
   - Dalam metode `initializeWeatherRecommendations()` di `home.java`, aplikasi membuat mapping antara kondisi cuaca dan rekomendasi minuman

```java
private void initializeWeatherRecommendations() {
    // Mapping untuk cuaca cerah
    weatherRecommendations.put("Clear", Arrays.asList(
        "Cold Brew Hitam",
        "Frappuccino Karamel",
        "Es Teh Lemon"
    ));
    
    // Mapping untuk cuaca berawan
    weatherRecommendations.put("Clouds", Arrays.asList(
        "Latte",
        "Cold Brew Coklat",
        "Frappuccino Vanilla"
    ));
    
    // ... mapping untuk kondisi cuaca lainnya ...
}
```

2. **Algoritma Pemilihan:**
   - Ketika data cuaca diterima, kondisi cuaca saat ini (seperti "Clear", "Clouds", "Rain") diekstrak
   - Rekomendasi dipilih dari mapping menggunakan kondisi sebagai key
   - Jika tidak ada rekomendasi spesifik, default rekomendasi ditampilkan

```java
private void updateWeatherRecommendation(String weatherCondition) {
    List<String> recommendations = weatherRecommendations.get(weatherCondition);
    if (recommendations != null && !recommendations.isEmpty()) {
        // Memilih rekomendasi acak dari list
        int randomIndex = new Random().nextInt(recommendations.size());
        String recommendation = recommendations.get(randomIndex);
        weatherRecommendation.setText("Rekomendasi: " + recommendation);
    } else {
        // Default rekomendasi jika tidak ada yang cocok
        weatherRecommendation.setText("Rekomendasi: Americano");
    }
}
```

### 5. Pencarian Produk (Search)

Fitur pencarian produk diimplementasikan dengan cara:

1. **Input Pencarian:**
   - Menggunakan EditText dengan TextWatcher untuk mendeteksi perubahan teks

```java
private void setupSearch() {
    searchEditText.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchProducts(s.toString());
        }
        // ... metode TextWatcher lainnya ...
    });
}
```

2. **Algoritma Pencarian:**
   - Dalam metode `searchProducts()`, aplikasi melakukan pencarian produk dari semua kategori
   - Pencarian menggunakan filter case-insensitive untuk nama produk
   - Hasil pencarian difilter dan ditampilkan dalam grid

```java
private void searchProducts(String query) {
    if (query.isEmpty()) {
        // Menampilkan tampilan kategori jika pencarian kosong
        // ...
        return;
    }

    // Mengumpulkan semua produk dari semua kategori
    List<String> allProducts = new ArrayList<>();
    List<Integer> allImages = new ArrayList<>();
    List<String> allPrices = new ArrayList<>();
    
    // Looping melalui semua kategori untuk mengumpulkan data
    for (String category : categoryProducts.keySet()) {
        List<String> products = categoryProducts.get(category);
        List<Integer> images = categoryImages.get(category);
        List<String> prices = categoryPrices.get(category);
        
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                // Menambahkan produk jika cocok dengan query
                String product = products.get(i);
                if (product.toLowerCase().contains(query.toLowerCase())) {
                    allProducts.add(product);
                    allImages.add(images.get(i));
                    allPrices.add(prices.get(i));
                }
            }
        }
    }
    
    // Menampilkan hasil pencarian
    servicesGrid.setAdapter(new ServiceAdapter(getContext(), allProducts, allImages, allPrices));
    
    // Update visibilitas komponen UI
    // ...
}
```

3. **Menampilkan Hasil:**
   - Hasil pencarian ditampilkan menggunakan RecyclerView dengan ServiceAdapter
   - UI diperbarui untuk menunjukkan jumlah hasil pencarian
   - Pesan "Tidak ditemukan" ditampilkan jika tidak ada hasil yang cocok

### 6. Detail Menu dan Kustomisasi

Halaman detail menu memungkinkan pengguna untuk melihat informasi lengkap tentang produk dan menyesuaikan pesanan mereka:

1. **Tampilan Detail:**
   - Menampilkan gambar produk, nama, harga, deskripsi, bahan, dan rating
   - Informasi diambil dari `MenuDetail.java` berdasarkan nama produk

2. **Kustomisasi Pesanan:**
   - Pengguna dapat memilih ukuran (size) dengan ChipGroup: Small, Medium, Large
   - Pengguna dapat memilih tingkat gula dengan ChipGroup: Less Sugar, Normal, Extra Sugar
   - Perubahan ukuran akan mempengaruhi harga produk (Large +5.000, Small -3.000)

```java
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
```

3. **Pengaturan Jumlah:**
   - Tombol + dan - untuk mengatur jumlah pesanan (1-10)
   - Harga total diperbarui secara otomatis berdasarkan jumlah

4. **Fitur Favorit:**
   - Tombol favorit untuk menandai produk sebagai favorit
   - Status favorit diindikasikan dengan perubahan ikon dan warna
   - Implementasi animasi ketika menambahkan ke favorit

5. **Produk Terkait:**
   - Menampilkan daftar produk terkait berdasarkan kategori produk saat ini
   - Menggunakan RecyclerView horizontal dengan ServiceAdapter
   - Produk terkait difilter untuk tidak menampilkan produk yang sedang dilihat

```java
private void setupRelatedProducts(String currentProduct) {
    // Daftar produk terkait (algoritma rekomendasi sederhana berdasarkan kategori)
    List<String> relatedNames = new ArrayList<>();
    List<Integer> relatedImages = new ArrayList<>();
    List<String> relatedPrices = new ArrayList<>();
    
    // Menambahkan produk terkait berdasarkan kategori
    if (currentProduct.contains("Espresso") || currentProduct.contains("Americano") || 
        currentProduct.contains("Macchiato") || currentProduct.contains("Cappuccino")) {
        // Produk terkait untuk kopi espresso-based
        // ...
    } else if (currentProduct.contains("Frappuccino")) {
        // Produk terkait untuk Frappuccino
        // ...
    }
    
    // Filter out current product from related products
    // ...
    
    // Setup adapter for related products
    ServiceAdapter relatedAdapter = new ServiceAdapter(this, relatedNames, relatedImages, relatedPrices);
    relatedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    relatedProductsRecyclerView.setAdapter(relatedAdapter);
}
```

### 7. Keranjang Belanja

Fitur keranjang belanja memungkinkan pengguna untuk menyimpan dan mengelola item yang ingin dibeli:

1. **Model Data Keranjang:**
   - Menggunakan kelas `CartItem.java` untuk menyimpan informasi produk dalam keranjang
   - Setiap item berisi: nama, harga, jumlah, gambar, ukuran (size), dan tingkat gula

```java
public CartItem(String name, String price, int quantity, int imageResource, String size, String sugar) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.imageResource = imageResource;
    this.priceValue = extractPriceValue(price);
    this.size = size;
    this.sugar = sugar;
}
```

2. **Tampilan Keranjang:**
   - Menggunakan RecyclerView dengan CardView untuk setiap item dalam keranjang
   - Layout `fragment_cart.xml` menampilkan daftar item dan panel ringkasan total
   - Tampilan "Keranjang Kosong" ditampilkan saat tidak ada item

3. **Pengelolaan Item:**
   - Implementasi dalam `CartAdapter.java` untuk menampilkan dan mengelola item
   - Fitur tambah/kurangi jumlah untuk setiap item
   - Tombol hapus untuk menghapus item dari keranjang
   - Callback interface untuk komunikasi antara adapter dan fragment

```java
public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
    CartItem item = cartItems.get(position);
    
    // Set data
    holder.nameTextView.setText(item.getName());
    holder.priceTextView.setText(formatRupiah(item.getTotalPrice()));
    holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
    holder.variantTextView.setText(item.getSize() + ", " + item.getSugar());
    holder.itemImageView.setImageResource(item.getImageResource());
    
    // Pasang event listener
    holder.plusButton.setOnClickListener(v -> {
        int newQuantity = item.getQuantity() + 1;
        item.setQuantity(newQuantity);
        holder.quantityTextView.setText(String.valueOf(newQuantity));
        holder.priceTextView.setText(formatRupiah(item.getTotalPrice()));
        
        if (listener != null) {
            listener.onQuantityChanged(holder.getAdapterPosition(), newQuantity);
        }
    });
    
    // ... event listeners lainnya ...
}
```

4. **Perhitungan Harga:**
   - Menghitung total harga secara real-time saat jumlah item berubah
   - Format harga dalam format mata uang Rupiah
   - Metode `getTotalPrice()` di `CartItem.java` untuk menghitung subtotal per item

5. **Animasi:**
   - Animasi slide-in saat item ditambahkan ke keranjang
   - Animasi fade-out saat item dihapus dari keranjang

```java
private void setAnimation(View viewToAnimate, int position) {
    if (position > lastPosition) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setDuration(350);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }
}
```

### 8. Proses Checkout

Proses checkout memungkinkan pengguna untuk menyelesaikan pembelian:

1. **Tampilan Checkout:**
   - Halaman `activity_checkout.xml` menampilkan ringkasan pesanan
   - Data pelanggan ditampilkan di bagian atas
   - Daftar produk yang dibeli menggunakan RecyclerView
   - Ringkasan biaya (subtotal, pajak, biaya pengiriman, total)

2. **Informasi Pesanan:**
   - Menampilkan setiap item dengan nama, varian, jumlah, dan harga
   - Format ringkas menggunakan `item_checkout_product.xml`
   - Perhitungan biaya tambahan (pajak 10%, biaya pengiriman)

```xml
<TextView
    android:id="@+id/product_name"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:ellipsize="end"
    android:maxLines="1"
    android:textColor="@color/text_primary"
    android:textSize="14sp"
    app:layout_constraintEnd_toStartOf="@+id/product_price"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    tools:text="Espresso Coffee" />

<TextView
    android:id="@+id/product_variant"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_marginTop="2dp"
    android:ellipsize="end"
    android:maxLines="1"
    android:textColor="@color/text_secondary"
    android:textSize="12sp"
    app:layout_constraintEnd_toEndOf="@+id/product_name"
    app:layout_constraintStart_toStartOf="@+id/product_name"
    app:layout_constraintTop_toBottomOf="@+id/product_name"
    tools:text="Medium, Normal Sugar" />
```

3. **Metode Pembayaran:**
   - Pilihan metode pembayaran menggunakan RadioGroup (Tunai, Kartu Kredit/Debit, E-Wallet)
   - Setiap metode dapat dikustomisasi lebih lanjut

4. **Alamat Pengiriman:**
   - Menampilkan dan memungkinkan perubahan alamat pengiriman
   - Tombol "Ubah Alamat" untuk memperbarui alamat

5. **Finalisasi Pesanan:**
   - Tombol "Pesan Sekarang" untuk menyelesaikan proses checkout
   - Validasi data sebelum melanjutkan ke konfirmasi pesanan
   - Menampilkan dialog konfirmasi sebelum menyelesaikan pesanan

### 9. Sistem Autentikasi

Aplikasi menggunakan sistem autentikasi sederhana berbasis SharedPreferences:

1. **Login:**
   - Implementasi di `login.java` untuk proses autentikasi
   - Validasi form untuk email dan password
   - Menyimpan status login dengan SharedPreferences

```java
private void attemptLogin() {
    // Reset errors
    emailEditText.setError(null);
    passwordEditText.setError(null);
    
    // Ambil nilai dari form
    String email = emailEditText.getText().toString().trim();
    String password = passwordEditText.getText().toString().trim();
    
    // Validasi form
    // ...
    
    // Proses login dengan sharedPreferences
    String savedEmail = sharedPreferences.getString("userEmail", "");
    String savedPassword = sharedPreferences.getString("userPassword", "");
    
    if (savedEmail.equals(email) && savedPassword.equals(password)) {
        // Login berhasil
        saveLoginStatus(true, email);
        startMainActivity();
    } else {
        // Cek akun default
        if (email.equals("user@swoopix.com") && password.equals("password")) {
            // Login berhasil dengan akun default
            saveLoginStatus(true, email);
            startMainActivity();
        } else {
            // Login gagal
            Toast.makeText(this, "Email atau password salah.", Toast.LENGTH_LONG).show();
        }
    }
}
```

2. **Registrasi:**
   - Pendaftaran pengguna baru dengan validasi data
   - Menyimpan informasi pengguna dalam SharedPreferences

3. **Session Management:**
   - Memeriksa status login saat aplikasi dimulai
   - Redirect ke MainActivity jika sudah login
   - Logout dengan menghapus status login dari SharedPreferences

```java
private boolean isLoggedIn() {
    return sharedPreferences.getBoolean("isLoggedIn", false);
}

private void saveLoginStatus(boolean isLoggedIn, String email) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean("isLoggedIn", isLoggedIn);
    editor.putString("loggedInEmail", email);
    editor.apply();
}
```

4. **Profil Pengguna:**
   - Menampilkan dan mengelola data pengguna
   - Memungkinkan perubahan informasi profil

### 10. Pengelolaan State Aplikasi

Aplikasi mengelola state dan data dengan cara berikut:

1. **SharedPreferences:**
   - Menyimpan data pengguna dan preferensi aplikasi
   - Status login dan informasi sesi
   - Preferensi tampilan dan pengaturan

2. **Penyimpanan Lokal:**
   - Data produk, promosi, dan kategori di-hardcode dalam aplikasi
   - Format data dirancang untuk mudah diperbarui atau diganti dengan data dari API

3. **Intent dan Bundle:**
   - Mengirim data antar aktivitas menggunakan Intent dan Bundle
   - Contoh dalam transfer data dari daftar produk ke detail produk

```java
Intent intent = new Intent(context, MenuDetail.class);
intent.putExtra("COFFEE_NAME", productNames.get(position));
intent.putExtra("COFFEE_PRICE", productPrices.get(position));
intent.putExtra("COFFEE_IMAGE", productImages.get(position));
context.startActivity(intent);
```

4. **Singleton dan Static Data:**
   - Beberapa data dibagikan melalui singleton atau static fields
   - Memastikan konsistensi data di seluruh aplikasi

5. **Lifecycle Management:**
   - Menggunakan metode lifecycle Activity dan Fragment untuk mengelola state
   - Menyimpan dan memulihkan data saat konfigurasi berubah (rotasi layar)
