package com.example.swoopixapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class voucher extends Fragment {

    private RecyclerView recyclerView;
    private VoucherAdapter adapter;
    private ConstraintLayout emptyState;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inisialisasi views
        recyclerView = view.findViewById(R.id.vouchers_recycler_view);
        emptyState = view.findViewById(R.id.empty_state);
        tabLayout = view.findViewById(R.id.tab_layout);
        
        // Setup recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VoucherAdapter();
        recyclerView.setAdapter(adapter);
        
        // Setup tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadVouchers(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Tidak perlu implementasi
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Tidak perlu implementasi
            }
        });
        
        // Load data untuk tab pertama (Tersedia)
        loadVouchers(0);
    }
    
    private void loadVouchers(int tabPosition) {
        List<VoucherItem> vouchers = new ArrayList<>();
        
        // Data dummy berdasarkan tab yang dipilih
        switch (tabPosition) {
            case 0: // Tersedia
                vouchers.add(new VoucherItem(
                    "Diskon 30%",
                    "Berlaku untuk semua minuman",
                    "NEWUSER30",
                    "31 Desember 2023",
                    VoucherStatus.AVAILABLE
                ));
                
                vouchers.add(new VoucherItem(
                    "Gratis Ongkir",
                    "Min. pembelian Rp50.000",
                    "FREESHIP",
                    "30 November 2023",
                    VoucherStatus.AVAILABLE
                ));
                
                vouchers.add(new VoucherItem(
                    "Diskon Rp25.000",
                    "Min. pembelian Rp75.000",
                    "DISC25K",
                    "15 Desember 2023",
                    VoucherStatus.AVAILABLE
                ));
                break;
                
            case 1: // Digunakan
                vouchers.add(new VoucherItem(
                    "Buy 1 Get 1",
                    "Untuk semua jenis Frappuccino",
                    "B1G1FRAPP",
                    "Digunakan pada 10 Oktober 2023",
                    VoucherStatus.USED
                ));
                break;
                
            case 2: // Kadaluarsa
                vouchers.add(new VoucherItem(
                    "Diskon 50%",
                    "Khusus pengguna baru",
                    "WELCOME50",
                    "Berakhir pada 30 September 2023",
                    VoucherStatus.EXPIRED
                ));
                
                vouchers.add(new VoucherItem(
                    "Gratis Topping",
                    "Untuk semua minuman",
                    "FREETOPPING",
                    "Berakhir pada 15 Oktober 2023",
                    VoucherStatus.EXPIRED
                ));
                break;
        }
        
        // Update adapter
        adapter.setVouchers(vouchers);
        
        // Tampilkan recycler view atau empty state
        if (vouchers.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }
    
    // Enum untuk status voucher
    enum VoucherStatus {
        AVAILABLE, USED, EXPIRED
    }
    
    // Model class untuk voucher
    static class VoucherItem {
        private String title;
        private String subtitle;
        private String code;
        private String expiry;
        private VoucherStatus status;
        
        public VoucherItem(String title, String subtitle, String code, String expiry, VoucherStatus status) {
            this.title = title;
            this.subtitle = subtitle;
            this.code = code;
            this.expiry = expiry;
            this.status = status;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getSubtitle() {
            return subtitle;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getExpiry() {
            return expiry;
        }
        
        public VoucherStatus getStatus() {
            return status;
        }
    }
    
    // Adapter untuk voucher
    class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
        
        private List<VoucherItem> vouchers = new ArrayList<>();
        
        public void setVouchers(List<VoucherItem> vouchers) {
            this.vouchers = vouchers;
            notifyDataSetChanged();
        }
        
        @NonNull
        @Override
        public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_voucher, parent, false);
            return new VoucherViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
            VoucherItem item = vouchers.get(position);
            
            holder.titleTextView.setText(item.getTitle());
            holder.subtitleTextView.setText(item.getSubtitle());
            holder.codeTextView.setText(item.getCode());
            holder.expiryTextView.setText(item.getExpiry());
            
            // Handle button visibility dan text berdasarkan status
            if (item.getStatus() == VoucherStatus.AVAILABLE) {
                holder.useButton.setVisibility(View.VISIBLE);
                holder.useButton.setText("Gunakan Sekarang");
                holder.useButton.setEnabled(true);
                holder.copyButton.setVisibility(View.VISIBLE);
            } else if (item.getStatus() == VoucherStatus.USED) {
                holder.useButton.setVisibility(View.VISIBLE);
                holder.useButton.setText("Voucher Telah Digunakan");
                holder.useButton.setEnabled(false);
                holder.copyButton.setVisibility(View.GONE);
            } else { // EXPIRED
                holder.useButton.setVisibility(View.VISIBLE);
                holder.useButton.setText("Voucher Kadaluarsa");
                holder.useButton.setEnabled(false);
                holder.copyButton.setVisibility(View.GONE);
            }
            
            // Set click listener untuk copy button
            holder.copyButton.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Voucher Code", item.getCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Kode voucher disalin", Toast.LENGTH_SHORT).show();
            });
            
            // Set click listener untuk use button
            holder.useButton.setOnClickListener(v -> {
                if (item.getStatus() == VoucherStatus.AVAILABLE) {
                    Toast.makeText(getContext(), "Menggunakan voucher: " + item.getCode(), Toast.LENGTH_SHORT).show();
                    // Navigasi ke halaman order atau implementasi lain
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return vouchers.size();
        }
        
        class VoucherViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView subtitleTextView;
            TextView codeTextView;
            TextView expiryTextView;
            TextView copyButton;
            MaterialButton useButton;
            
            public VoucherViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.voucher_title);
                subtitleTextView = itemView.findViewById(R.id.voucher_subtitle);
                codeTextView = itemView.findViewById(R.id.voucher_code);
                expiryTextView = itemView.findViewById(R.id.expiry_date);
                copyButton = itemView.findViewById(R.id.copy_button);
                useButton = itemView.findViewById(R.id.use_button);
            }
        }
    }
} 