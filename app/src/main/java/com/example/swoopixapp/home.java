package com.example.swoopixapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView servicesGrid;
    private TextView greetingText;
    private TextView categoriesTitle;
    private TextView popularTitle;
    private EditText searchEditText;
    private MaterialCardView searchBar;
    private ImageView searchIcon;
    
    // Weather widget
    private ConstraintLayout weatherWidget;
    private TextView weatherTemp;
    private TextView weatherDesc;
    private TextView weatherLocation;
    private TextView weatherRecommendation;
    private ImageView weatherIcon;
    
    // API Key OpenWeatherMap
    private static final String WEATHER_API_KEY = "dcc44c87fd77c6aa6c467f7731dfe57f";
    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/";
    
    // Koordinat Surabaya
    private static final String SURABAYA_LAT = "-7.2575";
    private static final String SURABAYA_LON = "112.7521";
    
    // Category cards
    private MaterialCardView espressoCategory;
    private MaterialCardView frappuccinoCategory;
    private MaterialCardView coldBrewCategory;
    private MaterialCardView teaCategory;
    
    // Map for storing category products
    private Map<String, List<String>> categoryProducts = new HashMap<>();
    private Map<String, List<Integer>> categoryImages = new HashMap<>();
    private Map<String, List<String>> categoryPrices = new HashMap<>();
    
    // Map for storing weather recommendations
    private Map<String, List<String>> weatherRecommendations = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        try {
            // Inisialisasi UI components
            initializeViews(rootView);
            
            // Setup greeting berdasarkan waktu
            setupGreeting();
            
            // Inisialisasi data kategori
            initializeCategoryData();
            
            // Inisialisasi rekomendasi cuaca
            initializeWeatherRecommendations();
            
            // Setup menu kopi (tampilkan semua produk pertama kali)
            setupCoffeeMenu(null);
            
            // Setup kategori click listeners
            setupCategoryListeners();
            
            // Setup fitur pencarian
            setupSearch();
            
            // Ambil data cuaca
            fetchWeatherData();
            
            return rootView;
        } catch (Exception e) {
            Log.e(TAG, "Error pada inisialisasi Home Fragment: " + e.getMessage());
            Toast.makeText(getContext(), "Terjadi kesalahan saat memuat halaman", Toast.LENGTH_SHORT).show();
            return rootView;
        }
    }
    
    private void initializeViews(View rootView) {
        servicesGrid = rootView.findViewById(R.id.services_grid);
        greetingText = rootView.findViewById(R.id.greeting_text);
        categoriesTitle = rootView.findViewById(R.id.categories_title);
        popularTitle = rootView.findViewById(R.id.popular_title);
        searchBar = rootView.findViewById(R.id.search_bar);
        searchIcon = rootView.findViewById(R.id.search_icon);
        searchEditText = rootView.findViewById(R.id.search_input);
        
        // Inisialisasi widget cuaca
        weatherWidget = rootView.findViewById(R.id.weather_widget);
        weatherTemp = rootView.findViewById(R.id.weather_temperature);
        weatherDesc = rootView.findViewById(R.id.weather_description);
        weatherLocation = rootView.findViewById(R.id.weather_location);
        weatherRecommendation = rootView.findViewById(R.id.weather_recommendation);
        weatherIcon = rootView.findViewById(R.id.weather_icon);
        
        // Inisialisasi kategori cards
        espressoCategory = rootView.findViewById(R.id.espresso_category);
        frappuccinoCategory = rootView.findViewById(R.id.frappuccino_category);
        coldBrewCategory = rootView.findViewById(R.id.cold_brew_category);
        teaCategory = rootView.findViewById(R.id.tea_category);
    }
    
    private void setupGreeting() {
        // Setup greeting berdasarkan waktu
        String greeting;
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String userName = "Pelanggan"; // Bisa diganti dengan nama user jika ada sistem login
        
        if (hourOfDay < 12) {
            greeting = "Selamat pagi, " + userName + "!";
        } else if (hourOfDay < 15) {
            greeting = "Selamat siang, " + userName + "!";
        } else if (hourOfDay < 18) {
            greeting = "Selamat sore, " + userName + "!"; 
        } else {
            greeting = "Selamat malam, " + userName + "!";
        }
        
        greetingText.setText(greeting);
    }
    
    private void initializeCategoryData() {
        // Data produk untuk kategori Espresso
        categoryProducts.put("Espresso", new ArrayList<>(Arrays.asList(
            "Espresso",
            "Americano", 
            "Caramel Macchiato",
            "Cappuccino"
        )));
        
        categoryImages.put("Espresso", new ArrayList<>(Arrays.asList(
            R.drawable.ic_coffee_bean,
            R.drawable.ic_coffee_cup,
            R.drawable.ic_latte,
            R.drawable.ic_cappuccino
        )));
        
        categoryPrices.put("Espresso", new ArrayList<>(Arrays.asList(
            "Rp 20.000",
            "Rp 28.000",
            "Rp 38.000",
            "Rp 32.000"
        )));
        
        // Data produk untuk kategori Frappuccino
        categoryProducts.put("Frappuccino", new ArrayList<>(Arrays.asList(
            "Java Chip Frappuccino",
            "Caramel Frappuccino",
            "Mocha Frappuccino"
        )));
        
        categoryImages.put("Frappuccino", new ArrayList<>(Arrays.asList(
            R.drawable.ic_frappuccino,
            R.drawable.ic_frappuccino,
            R.drawable.ic_frappuccino
        )));
        
        categoryPrices.put("Frappuccino", new ArrayList<>(Arrays.asList(
            "Rp 45.000",
            "Rp 43.000",
            "Rp 42.000"
        )));
        
        // Data produk untuk kategori Cold Brew
        categoryProducts.put("Cold Brew", new ArrayList<>(Arrays.asList(
            "Cold Brew",
            "Vanilla Sweet Cream Cold Brew",
            "Dark Mocha Cold Brew"
        )));
        
        categoryImages.put("Cold Brew", new ArrayList<>(Arrays.asList(
            R.drawable.ic_coffee_cup,
            R.drawable.ic_coffee_cup,
            R.drawable.ic_coffee_cup
        )));
        
        categoryPrices.put("Cold Brew", new ArrayList<>(Arrays.asList(
            "Rp 30.000",
            "Rp 35.000",
            "Rp 37.000"
        )));
        
        // Data produk untuk kategori Tea
        categoryProducts.put("Tea", new ArrayList<>(Arrays.asList(
            "Matcha Latte",
            "Earl Grey Tea",
            "Green Tea Latte"
        )));
        
        categoryImages.put("Tea", new ArrayList<>(Arrays.asList(
            R.drawable.ic_matcha,
            R.drawable.ic_tea,
            R.drawable.ic_matcha
        )));
        
        categoryPrices.put("Tea", new ArrayList<>(Arrays.asList(
            "Rp 40.000",
            "Rp 28.000",
            "Rp 35.000"
        )));
        
        // Data untuk semua menu (digunakan untuk tampilan awal)
        categoryProducts.put("All", new ArrayList<>(Arrays.asList(
            "Caramel Macchiato",
            "Java Chip Frappuccino",
            "Americano",
            "Espresso",
            "Cappuccino",
            "Cold Brew",
            "Matcha Latte",
            "Dark Mocha"
        )));
        
        categoryImages.put("All", new ArrayList<>(Arrays.asList(
            R.drawable.ic_latte,
            R.drawable.ic_frappuccino,
            R.drawable.ic_coffee_cup,
            R.drawable.ic_coffee_bean,
            R.drawable.ic_cappuccino,
            R.drawable.ic_coffee_cup,
            R.drawable.ic_matcha,
            R.drawable.ic_coffee_cup
        )));
        
        categoryPrices.put("All", new ArrayList<>(Arrays.asList(
            "Rp 38.000",
            "Rp 45.000",
            "Rp 28.000",
            "Rp 20.000",
            "Rp 32.000",
            "Rp 30.000",
            "Rp 40.000",
            "Rp 42.000"
        )));
    }
    
    private void initializeWeatherRecommendations() {
        // Rekomendasi untuk cuaca cerah/panas
        weatherRecommendations.put("Clear", new ArrayList<>(Arrays.asList(
            "Java Chip Frappuccino",
            "Cold Brew",
            "Vanilla Sweet Cream Cold Brew",
            "Caramel Frappuccino"
        )));
        
        // Rekomendasi untuk cuaca berawan/mendung
        weatherRecommendations.put("Clouds", new ArrayList<>(Arrays.asList(
            "Matcha Latte",
            "Caramel Macchiato",
            "Cappuccino",
            "Green Tea Latte"
        )));
        
        // Rekomendasi untuk cuaca hujan
        weatherRecommendations.put("Rain", new ArrayList<>(Arrays.asList(
            "Americano",
            "Cappuccino",
            "Earl Grey Tea",
            "Dark Mocha"
        )));
        
        // Default recommendation
        weatherRecommendations.put("Default", new ArrayList<>(Arrays.asList(
            "Caramel Macchiato",
            "Espresso",
            "Cold Brew",
            "Green Tea Latte"
        )));
    }
    
    private void setupCoffeeMenu(String category) {
        // Ambil data berdasarkan kategori, default ke "All" jika null
        String categoryKey = category != null ? category : "All";
        
        // Update judul untuk menunjukkan kategori yang dipilih
        if (category != null) {
            popularTitle.setText("Menu " + category);
        } else {
            popularTitle.setText("Menu Populer");
        }
        
        List<String> menuList = categoryProducts.get(categoryKey);
        List<Integer> imageList = categoryImages.get(categoryKey);
        List<String> priceList = categoryPrices.get(categoryKey);
        
        // Pastikan data tidak null
        if (menuList == null || imageList == null || priceList == null) {
            Log.e(TAG, "Data kategori tidak ditemukan: " + categoryKey);
            return;
        }
        
        // Set adapter dengan layout grid yang responsif
        ServiceAdapter adapter = new ServiceAdapter(getContext(), menuList, imageList, priceList);
        
        // Hitung jumlah kolom berdasarkan lebar layar untuk responsivitas
        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        servicesGrid.setLayoutManager(layoutManager);
        servicesGrid.setAdapter(adapter);
        
        // Debugging
        Log.d(TAG, "Menu " + categoryKey + " dimuat: " + menuList.size() + " items dengan " + spanCount + " kolom");
    }
    
    private void setupCategoryListeners() {
        // Listener untuk kategori Espresso
        espressoCategory.setOnClickListener(v -> {
            setupCoffeeMenu("Espresso");
            Toast.makeText(getContext(), "Menampilkan menu Espresso", Toast.LENGTH_SHORT).show();
        });
        
        // Listener untuk kategori Frappuccino
        frappuccinoCategory.setOnClickListener(v -> {
            setupCoffeeMenu("Frappuccino");
            Toast.makeText(getContext(), "Menampilkan menu Frappuccino", Toast.LENGTH_SHORT).show();
        });
        
        // Listener untuk kategori Cold Brew
        coldBrewCategory.setOnClickListener(v -> {
            setupCoffeeMenu("Cold Brew");
            Toast.makeText(getContext(), "Menampilkan menu Cold Brew", Toast.LENGTH_SHORT).show();
        });
        
        // Listener untuk kategori Tea
        teaCategory.setOnClickListener(v -> {
            setupCoffeeMenu("Tea");
            Toast.makeText(getContext(), "Menampilkan menu Tea", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak digunakan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tidak digunakan
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchProducts(s.toString());
            }
        });
    }
    
    private void searchProducts(String query) {
        // Jika query kosong, tampilkan semua produk
        if (query.isEmpty()) {
            setupCoffeeMenu(null);
            return;
        }
        
        // Cari di semua kategori
        List<String> allProducts = categoryProducts.get("All");
        List<Integer> allImages = categoryImages.get("All");
        List<String> allPrices = categoryPrices.get("All");
        
        // Filter produk berdasarkan query
        List<String> filteredProducts = new ArrayList<>();
        List<Integer> filteredImages = new ArrayList<>();
        List<String> filteredPrices = new ArrayList<>();
        
        query = query.toLowerCase();
        
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).toLowerCase().contains(query)) {
                filteredProducts.add(allProducts.get(i));
                filteredImages.add(allImages.get(i));
                filteredPrices.add(allPrices.get(i));
            }
        }
        
        // Update UI dengan hasil pencarian
        popularTitle.setText("Hasil Pencarian");
        
        // Tampilkan hasil
        ServiceAdapter adapter = new ServiceAdapter(getContext(), filteredProducts, filteredImages, filteredPrices);
        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        servicesGrid.setLayoutManager(layoutManager);
        servicesGrid.setAdapter(adapter);
        
        Log.d(TAG, "Pencarian: " + query + ", ditemukan " + filteredProducts.size() + " produk");
    }
    
    private void fetchWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeather(
                SURABAYA_LAT,
                SURABAYA_LON,
                WEATHER_API_KEY,
                "metric"); // Gunakan unit metrik (Celsius)
        
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherUI(response.body());
                } else {
                    Log.e(TAG, "API Error: " + response.code());
                    Toast.makeText(getContext(), "Gagal mendapatkan data cuaca", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "API Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Gagal terhubung ke layanan cuaca", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateWeatherUI(WeatherResponse weatherData) {
        if (getContext() == null || weatherData == null || weatherData.main == null || weatherData.weather == null || weatherData.weather.isEmpty()) {
            return;
        }
        
        try {
            // Update suhu
            int temperature = Math.round(weatherData.main.temp);
            weatherTemp.setText(temperature + "°C");
            
            // Update deskripsi cuaca
            String description = weatherData.weather.get(0).description;
            weatherDesc.setText(description);
            
            // Update lokasi
            weatherLocation.setText("Surabaya");
            
            // Update rekomendasi berdasarkan cuaca
            String weatherMain = weatherData.weather.get(0).main;
            updateWeatherRecommendation(weatherMain);
            
            // Tampilkan widget cuaca
            weatherWidget.setVisibility(View.VISIBLE);
            
        } catch (Exception e) {
            Log.e(TAG, "Error updating weather UI: " + e.getMessage());
        }
    }
    
    private void updateWeatherRecommendation(String weatherCondition) {
        // Ambil rekomendasi berdasarkan kondisi cuaca
        List<String> recommendations = weatherRecommendations.get(weatherCondition);
        
        // Jika tidak ada rekomendasi untuk kondisi cuaca ini, gunakan default
        if (recommendations == null) {
            recommendations = weatherRecommendations.get("Default");
        }
        
        // Pilih rekomendasi secara acak
        if (recommendations != null && !recommendations.isEmpty()) {
            int randomIndex = (int) (Math.random() * recommendations.size());
            String recommendation = recommendations.get(randomIndex);
            
            weatherRecommendation.setText("Rekomendasi: " + recommendation);
            
            // Filter produk berdasarkan rekomendasi
            List<String> allProducts = categoryProducts.get("All");
            List<Integer> allImages = categoryImages.get("All");
            List<String> allPrices = categoryPrices.get("All");
            
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).equals(recommendation)) {
                    // Tampilkan produk yang direkomendasikan di bagian atas
                    List<String> recommendedProducts = new ArrayList<>();
                    List<Integer> recommendedImages = new ArrayList<>();
                    List<String> recommendedPrices = new ArrayList<>();
                    
                    recommendedProducts.add(allProducts.get(i));
                    recommendedImages.add(allImages.get(i));
                    recommendedPrices.add(allPrices.get(i));
                    
                    // Tambahkan produk lain
                    for (int j = 0; j < allProducts.size(); j++) {
                        if (j != i) {
                            recommendedProducts.add(allProducts.get(j));
                            recommendedImages.add(allImages.get(j));
                            recommendedPrices.add(allPrices.get(j));
                        }
                    }
                    
                    // Update UI dengan rekomendasi
                    ServiceAdapter adapter = new ServiceAdapter(getContext(), recommendedProducts, recommendedImages, recommendedPrices);
                    int spanCount = calculateSpanCount();
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
                    servicesGrid.setLayoutManager(layoutManager);
                    servicesGrid.setAdapter(adapter);
                    
                    // Update judul
                    popularTitle.setText("Rekomendasi Cuaca");
                    
                    break;
                }
            }
        }
    }
    
    /**
     * Menghitung jumlah kolom berdasarkan lebar layar untuk responsivitas
     */
    private int calculateSpanCount() {
        // Default 2 kolom
        int spanCount = 2;
        
        if (getActivity() != null) {
            // Mendapatkan lebar layar dalam piksel
            int displayWidth = getResources().getDisplayMetrics().widthPixels;
            
            // Jika layar lebih besar dari 600dp (tablet), gunakan 3 kolom
            if (displayWidth / getResources().getDisplayMetrics().density >= 600) {
                spanCount = 3;
            }
            
            // Jika layar lebih besar dari 960dp (tablet besar), gunakan 4 kolom
            if (displayWidth / getResources().getDisplayMetrics().density >= 960) {
                spanCount = 4;
            }
        }
        
        return spanCount;
    }
}