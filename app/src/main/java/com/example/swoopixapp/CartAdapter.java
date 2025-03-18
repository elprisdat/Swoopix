package com.example.swoopixapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemRemoved(int position);
    }

    public CartAdapter(Context context, CartItemListener listener) {
        this.context = context;
        this.cartItems = new ArrayList<>();
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(CartItem item) {
        // Periksa apakah item sudah ada di keranjang
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem existingItem = cartItems.get(i);
            if (existingItem.getId() == item.getId()) {
                // Jika item sudah ada, tambahkan quantity
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                notifyItemChanged(i);
                return;
            }
        }
        
        // Jika item belum ada, tambahkan ke keranjang
        cartItems.add(item);
        notifyItemInserted(cartItems.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
        }
    }

    public int getItemCount() {
        return cartItems.size();
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        return subtotal;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        
        // Set data ke view
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(formatRupiah(item.getPrice()));
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        
        // Set gambar item 
        if (item.getImageResourceId() != 0) {
            holder.itemImageView.setImageResource(item.getImageResourceId());
        }
        
        // Set subtotal item
        double itemSubtotal = item.getPrice() * item.getQuantity();
        holder.subtotalTextView.setText(formatRupiah(itemSubtotal));
        
        // Set listener untuk tombol tambah
        holder.increaseButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.quantityTextView.setText(String.valueOf(newQuantity));
            holder.subtotalTextView.setText(formatRupiah(item.getPrice() * newQuantity));
            if (listener != null) {
                listener.onQuantityChanged(position, newQuantity);
            }
        });
        
        // Set listener untuk tombol kurang
        holder.decreaseButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                holder.quantityTextView.setText(String.valueOf(newQuantity));
                holder.subtotalTextView.setText(formatRupiah(item.getPrice() * newQuantity));
                if (listener != null) {
                    listener.onQuantityChanged(position, newQuantity);
                }
            } else {
                // Hapus item jika quantity menjadi 0
                if (listener != null) {
                    listener.onItemRemoved(position);
                }
            }
        });
        
        // Set listener untuk tombol hapus
        holder.removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemoved(position);
            }
        });
    }

    private String formatRupiah(double price) {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return formatRupiah.format(price);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        TextView subtotalTextView;
        MaterialButton increaseButton;
        MaterialButton decreaseButton;
        ImageView removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.item_name);
            priceTextView = itemView.findViewById(R.id.item_price);
            quantityTextView = itemView.findViewById(R.id.quantity_text);
            subtotalTextView = itemView.findViewById(R.id.item_subtotal);
            increaseButton = itemView.findViewById(R.id.increase_button);
            decreaseButton = itemView.findViewById(R.id.decrease_button);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
} 