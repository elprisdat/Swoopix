package com.example.swoopixapp;

/**
 * Kelas model untuk item dalam keranjang belanja
 */
public class CartItem {
    private String name;
    private String price;
    private int quantity;
    private int imageResource;
    private double priceValue;
    private String size;
    private String sugar;

    /**
     * Constructor untuk CartItem
     *
     * @param name Nama item
     * @param price Harga item dalam format string (contoh: "Rp 25.000")
     * @param quantity Jumlah item
     * @param imageResource Resource ID untuk gambar item
     */
    public CartItem(String name, String price, int quantity, int imageResource) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
        this.priceValue = extractPriceValue(price);
        this.size = "Medium";  // Default size
        this.sugar = "Normal"; // Default sugar level
    }

    /**
     * Constructor untuk CartItem dengan ukuran dan gula
     */
    public CartItem(String name, String price, int quantity, int imageResource, String size, String sugar) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
        this.priceValue = extractPriceValue(price);
        this.size = size;
        this.sugar = sugar;
    }

    /**
     * Mengekstrak nilai numerik dari string harga
     */
    private double extractPriceValue(String priceString) {
        // Contoh konversi "Rp 25.000" menjadi 25000.0
        try {
            String numericValue = priceString.replaceAll("[^0-9]", "");
            return Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            // Default ke 0 jika format tidak valid
            return 0;
        }
    }

    /**
     * Mendapatkan nama item
     */
    public String getName() {
        return name;
    }

    /**
     * Mengatur nama item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mendapatkan harga item dalam format string
     */
    public String getPrice() {
        return price;
    }

    /**
     * Mengatur harga item dengan format string
     */
    public void setPrice(String price) {
        this.price = price;
        this.priceValue = extractPriceValue(price);
    }

    /**
     * Mendapatkan jumlah item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Mengatur jumlah item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity < 1 ? 1 : quantity;
    }

    /**
     * Mendapatkan resource ID gambar
     */
    public int getImageResource() {
        return imageResource;
    }

    /**
     * Mengatur resource ID gambar
     */
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    /**
     * Menghitung total harga item (harga x jumlah)
     */
    public double getTotalPrice() {
        return priceValue * quantity;
    }

    /**
     * Mendapatkan ukuran (size) dari item
     */
    public String getSize() {
        return size;
    }

    /**
     * Mengatur ukuran (size) item
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Mendapatkan level gula dari item
     */
    public String getSugar() {
        return sugar;
    }

    /**
     * Mengatur level gula item
     */
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }
} 