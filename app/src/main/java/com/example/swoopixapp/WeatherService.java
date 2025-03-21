package com.example.swoopixapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
        @Query("lat") String lat,
        @Query("lon") String lon,
        @Query("appid") String apiKey,
        @Query("units") String units
    );
}