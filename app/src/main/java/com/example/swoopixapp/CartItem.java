package com.example.swoopixapp;

public class CartItem {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int imageResourceId;
    private String size; // Ukuran (Small, Medium, Large)
    private String notes; // Catatan tambahan dari pengguna

    public CartItem(int id, String name, double price, int quantity, int imageResourceId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResourceId = imageResourceId;
        this.size = "Medium"; // Default size
        this.notes = "";
    }

    public CartItem(int id, String name, double price, int quantity, int imageResourceId, String size, String notes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResourceId = imageResourceId;
        this.size = size;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Hitung total harga item (harga * kuantitas)
    public double getTotalPrice() {
        return price * quantity;
    }
} 