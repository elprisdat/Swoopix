package com.example.swoopixapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {

    private List<PromoItem> promoItems;
    private Context context;

    public PromoAdapter(Context context, List<PromoItem> promoItems) {
        this.context = context;
        this.promoItems = promoItems;
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo, parent, false);
        return new PromoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        PromoItem item = promoItems.get(position);

        holder.promoTitle.setText(item.getTitle());
        holder.promoDescription.setText(item.getDescription());
        holder.promoDate.setText("Berlaku s/d " + item.getValidUntil());
        holder.promoImage.setImageResource(item.getImageResourceId());

        // Tampilkan badge "Baru" hanya jika promo baru
        if (item.isNew()) {
            holder.promoBadge.setVisibility(View.VISIBLE);
        } else {
            holder.promoBadge.setVisibility(View.GONE);
        }

        // Set listener untuk tombol
        holder.promoButton.setOnClickListener(v -> {
            Toast.makeText(context, "Promo " + item.getTitle() + " telah digunakan", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return promoItems != null ? promoItems.size() : 0;
    }

    static class PromoViewHolder extends RecyclerView.ViewHolder {
        ImageView promoImage;
        TextView promoTitle;
        TextView promoDescription;
        TextView promoDate;
        TextView promoBadge;
        MaterialButton promoButton;

        public PromoViewHolder(@NonNull View itemView) {
            super(itemView);
            promoImage = itemView.findViewById(R.id.promo_image);
            promoTitle = itemView.findViewById(R.id.promo_title);
            promoDescription = itemView.findViewById(R.id.promo_description);
            promoDate = itemView.findViewById(R.id.promo_date);
            promoBadge = itemView.findViewById(R.id.promo_badge);
            promoButton = itemView.findViewById(R.id.promo_button);
        }
    }

    // Model data untuk item promo
    public static class PromoItem {
        private String title;
        private String description;
        private String validUntil;
        private int imageResourceId;
        private boolean isNew;

        public PromoItem(String title, String description, String validUntil, int imageResourceId, boolean isNew) {
            this.title = title;
            this.description = description;
            this.validUntil = validUntil;
            this.imageResourceId = imageResourceId;
            this.isNew = isNew;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getValidUntil() {
            return validUntil;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }

        public boolean isNew() {
            return isNew;
        }
    }
} 