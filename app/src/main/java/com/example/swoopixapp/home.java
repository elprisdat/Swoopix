package com.example.swoopixapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home extends Fragment {
    private TextView temperatureText;
    private TextView weatherDescription;
    private ImageView weatherIcon;
    private RecyclerView servicesGrid;
    private static final String WEATHER_API_KEY = "YOUR_API_KEY"; // Ganti dengan API key OpenWeatherMap Anda

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        temperatureText = view.findViewById(R.id.temperature);
        weatherDescription = view.findViewById(R.id.weather_description);
        weatherIcon = view.findViewById(R.id.weather_icon);
        servicesGrid = view.findViewById(R.id.services_grid);

        // Setup services grid
        setupServicesGrid();

        // Load weather data
        loadWeatherData();

        return view;
    }

    private void setupServicesGrid() {
        servicesGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // TODO: Implement ServiceAdapter
    }

    private void loadWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        WeatherService service = retrofit.create(WeatherService.class);
        
        // Menggunakan koordinat Jakarta sebagai default
        service.getCurrentWeather("-6.2088", "106.8456", WEATHER_API_KEY, "metric")
            .enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        WeatherResponse weather = response.body();
                        updateWeatherUI(weather);
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    // Handle error
                    weatherDescription.setText("Gagal memuat data cuaca");
                }
            });
    }

    private void updateWeatherUI(WeatherResponse weather) {
        temperatureText.setText(String.format("%.1f°C", weather.main.temp));
        weatherDescription.setText(weather.weather.get(0).description);
        
        // Load weather icon
        String iconUrl = String.format("https://openweathermap.org/img/w/%s.png", 
            weather.weather.get(0).icon);
        Glide.with(this)
            .load(iconUrl)
            .into(weatherIcon);
    }
}