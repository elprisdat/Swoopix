package com.example.swoopixapp;

import java.util.List;

public class WeatherResponse {
    public MainData main;
    public List<WeatherData> weather;

    public static class MainData {
        public float temp;
        public float feels_like;
        public float temp_min;
        public float temp_max;
        public int pressure;
        public int humidity;
    }

    public static class WeatherData {
        public int id;
        public String main;
        public String description;
        public String icon;
    }
} 