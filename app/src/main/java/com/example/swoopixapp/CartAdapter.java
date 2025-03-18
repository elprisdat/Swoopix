package com.example.swoopixapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    private final List<CartItem> cartItems;
    private final CartItemListener listener;
    private int lastPosition = -1;

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
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
        
        holder.minusButton.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                item.setQuantity(newQuantity);
                holder.quantityTextView.setText(String.valueOf(newQuantity));
                holder.priceTextView.setText(formatRupiah(item.getTotalPrice()));
                
                if (listener != null) {
                    listener.onQuantityChanged(holder.getAdapterPosition(), newQuantity);
                }
            } else {
                // Jika quantity = 1 dan tombol minus ditekan, hapus item
                if (listener != null) {
                    listener.onItemRemoved(holder.getAdapterPosition());
                }
            }
        });
        
        holder.removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemoved(holder.getAdapterPosition());
            }
        });
        
        // Animasi item saat muncul
        setAnimation(holder.itemView, position);
    }
    
    /**
     * Animasi slide in dari kanan untuk item
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(350);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
    
    /**
     * Format angka ke format Rupiah
     */
    private String formatRupiah(double price) {
        NumberFormat rupiahFormat = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp " + rupiahFormat.format(price);
    }

    // ViewHolder untuk cart item
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTextView;
        final TextView priceTextView;
        final TextView quantityTextView;
        final TextView variantTextView;
        final ImageView itemImageView;
        final ImageButton plusButton;
        final ImageButton minusButton;
        final ImageButton removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            priceTextView = itemView.findViewById(R.id.item_price);
            quantityTextView = itemView.findViewById(R.id.quantity_text);
            variantTextView = itemView.findViewById(R.id.item_variant);
            itemImageView = itemView.findViewById(R.id.item_image);
            plusButton = itemView.findViewById(R.id.btn_increase);
            minusButton = itemView.findViewById(R.id.btn_decrease);
            removeButton = itemView.findViewById(R.id.delete_button);
        }
    }

    // Interface untuk callback
    public interface CartItemListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemRemoved(int position);
    }
} 