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

### 2. Styling

Aplikasi menggunakan Material Design 3 dengan pendekatan styling sebagai berikut:

- **Tema Utama:** Menggunakan tema hitam-putih yang didefinisikan di `themes.xml` dengan parent `Theme.Material3.Light.NoActionBar`.

```xml
<style name="Theme.Swoopix" parent="Theme.Material3.Light.NoActionBar">
    <!-- Primary brand color -->
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryVariant">@color/primary_dark</item>
    <item name="colorOnPrimary">@color/white</item>
    <!-- ... konfigurasi warna lainnya ... -->
</style>
```

- **Skema Warna:** Menggunakan skema hitam-putih yang didefinisikan di `colors.xml`:
  - Primary: #000000 (hitam)
  - Secondary: #FFFFFF (putih)
  - Warna aksen dan status untuk error, success, warning, info

- **Komponen UI:**
  - BottomNavigationView dengan label dan ikon
  - MaterialCardView untuk item dan kartu
  - Sistem grid untuk menampilkan item kopi
  - TabLayout dengan custom indicator untuk carousel

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
