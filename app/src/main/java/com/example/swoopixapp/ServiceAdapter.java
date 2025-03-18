package com.example.swoopixapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private final List<String> menuList;
    private final List<Integer> imageList;
    private final List<String> priceList;
    private final Context context;
    
    // Black & White theme colors
    private final int[] CARD_COLORS = new int[]{
            Color.parseColor("#000000"), // Black
            Color.parseColor("#212121"), // Dark Gray
            Color.parseColor("#424242"), // Medium Gray
            Color.parseColor("#616161"), // Light Gray
            Color.parseColor("#000000"), // Black
            Color.parseColor("#212121")  // Dark Gray
    };

    public ServiceAdapter(Context context, List<String> menuList, List<Integer> imageList, List<String> priceList) {
        this.context = context;
        this.menuList = menuList;
        this.imageList = imageList;
        this.priceList = priceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String menuName = menuList.get(position);
        String price = priceList.get(position);
        Integer imageResource = imageList.get(position);
        
        holder.name.setText(menuName);
        holder.price.setText(price);
        holder.icon.setImageResource(imageResource);
        
        // Set card background color
        int colorIndex = position % CARD_COLORS.length;
        holder.cardView.setCardBackgroundColor(CARD_COLORS[colorIndex]);
        
        // Set white text for better readability on dark backgrounds
        holder.name.setTextColor(Color.WHITE);
        holder.price.setTextColor(Color.WHITE);
        
        // Add ripple effect for better user interaction
        holder.cardView.setClickable(true);
        holder.cardView.setFocusable(true);
        
        // Handle click events
        holder.cardView.setOnClickListener(view -> {
            Toast.makeText(context, menuName + " telah ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            
            // Untuk detail produk, bisa navigasi ke activity detail
            Intent intent = new Intent(context, MenuDetail.class);
            intent.putExtra("COFFEE_NAME", menuName);
            intent.putExtra("COFFEE_PRICE", price);
            intent.putExtra("COFFEE_IMAGE", imageResource);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;
        public ImageView icon;
        public MaterialCardView cardView;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.service_name);
            price = view.findViewById(R.id.coffee_price);
            icon = view.findViewById(R.id.service_icon);
            cardView = (MaterialCardView) view.findViewById(R.id.service_card);
        }
    }
} 