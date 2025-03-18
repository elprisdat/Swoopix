package com.example.swoopixapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class notification extends Fragment {
    
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ConstraintLayout emptyState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inisialisasi views
        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        emptyState = view.findViewById(R.id.empty_state);
        
        // Setup recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter();
        recyclerView.setAdapter(adapter);
        
        // Tampilkan data dummy
        loadDummyNotifications();
    }
    
    private void loadDummyNotifications() {
        // Buat data dummy
        List<NotificationItem> notifications = new ArrayList<>();
        
        notifications.add(new NotificationItem(
            "Promo Spesial", 
            "Dapatkan diskon 30% untuk pembelian kopi jenis apapun di hari Senin dan Rabu",
            "1 jam yang lalu",
            NotificationType.PROMO
        ));
        
        notifications.add(new NotificationItem(
            "Pesanan Selesai", 
            "Pesanan #SWP78201 Anda telah selesai. Terima kasih telah berbelanja di Swoopix!",
            "Kemarin, 18:45",
            NotificationType.ORDER
        ));
        
        notifications.add(new NotificationItem(
            "Voucher Baru", 
            "Anda mendapatkan voucher NEWUSER50 dengan potongan 50% (maksimal Rp25.000)",
            "2 hari yang lalu",
            NotificationType.VOUCHER
        ));
        
        notifications.add(new NotificationItem(
            "Poin Reward", 
            "Selamat! Anda telah mengumpulkan 100 poin rewards. Tukarkan dengan minuman gratis!",
            "3 hari yang lalu",
            NotificationType.REWARD
        ));
        
        notifications.add(new NotificationItem(
            "Pesanan Dibatalkan", 
            "Pesanan #SWP77541 Anda telah dibatalkan. Pembayaran akan dikembalikan dalam 24 jam.",
            "5 hari yang lalu",
            NotificationType.ORDER
        ));
        
        // Update adapter
        adapter.setNotifications(notifications);
        
        // Tampilkan recycler view dan sembunyikan empty state
        if (notifications.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }
    
    // Enum untuk tipe notifikasi
    enum NotificationType {
        PROMO, ORDER, VOUCHER, REWARD
    }
    
    // Model class untuk notifikasi
    static class NotificationItem {
        private String title;
        private String message;
        private String time;
        private NotificationType type;
        
        public NotificationItem(String title, String message, String time, NotificationType type) {
            this.title = title;
            this.message = message;
            this.time = time;
            this.type = type;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getTime() {
            return time;
        }
        
        public NotificationType getType() {
            return type;
        }
    }
    
    // Adapter untuk notifikasi
    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
        
        private List<NotificationItem> notifications = new ArrayList<>();
        
        public void setNotifications(List<NotificationItem> notifications) {
            this.notifications = notifications;
            notifyDataSetChanged();
        }
        
        @NonNull
        @Override
        public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
            NotificationItem item = notifications.get(position);
            
            holder.titleTextView.setText(item.getTitle());
            holder.messageTextView.setText(item.getMessage());
            holder.timeTextView.setText(item.getTime());
            
            // Set icon berdasarkan tipe notifikasi
            switch (item.getType()) {
                case PROMO:
                    holder.iconImageView.setImageResource(R.drawable.ic_tag);
                    break;
                case ORDER:
                    holder.iconImageView.setImageResource(R.drawable.ic_cart_outline);
                    break;
                case VOUCHER:
                    holder.iconImageView.setImageResource(R.drawable.ic_voucher);
                    break;
                case REWARD:
                    holder.iconImageView.setImageResource(R.drawable.ic_gift);
                    break;
            }
        }
        
        @Override
        public int getItemCount() {
            return notifications.size();
        }
        
        class NotificationViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView messageTextView;
            TextView timeTextView;
            ImageView iconImageView;
            
            public NotificationViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.notification_title);
                messageTextView = itemView.findViewById(R.id.notification_message);
                timeTextView = itemView.findViewById(R.id.notification_time);
                iconImageView = itemView.findViewById(R.id.notification_icon);
            }
        }
    }
}