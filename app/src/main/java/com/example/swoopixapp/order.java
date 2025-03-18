package com.example.swoopixapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class order extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ConstraintLayout emptyState;
    private ConstraintLayout orderSummary;
    private TextView subtotalTextView;
    private TextView taxTextView;
    private TextView totalTextView;
    private MaterialButton checkoutButton;
    private Button browseMenuButton;
    private ImageView emptyCartImage;
    private TextView emptyCartTitle;
    private TextView emptyCartDescription;
    private SharedPreferences sharedPreferences;
    private List<CartItem> cartItems;

    private static final double TAX_RATE = 0.10; // 10% pajak

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("SwoopixPrefs", Context.MODE_PRIVATE);
        
        // Inisialisasi views
        recyclerView = view.findViewById(R.id.cart_recyclerview);
        emptyState = view.findViewById(R.id.empty_state);
        orderSummary = view.findViewById(R.id.order_summary);
        subtotalTextView = view.findViewById(R.id.subtotal_value);
        taxTextView = view.findViewById(R.id.tax_value);
        totalTextView = view.findViewById(R.id.total_value);
        checkoutButton = view.findViewById(R.id.checkout_button);
        browseMenuButton = view.findViewById(R.id.browse_menu_button);
        emptyCartImage = view.findViewById(R.id.empty_cart_image);
        emptyCartTitle = view.findViewById(R.id.empty_cart_title);
        emptyCartDescription = view.findViewById(R.id.empty_cart_description);

        // Setup RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Inisialisasi daftar cart items
        cartItems = new ArrayList<>();
        
        // Buat adapter
        cartAdapter = new CartAdapter(getContext(), cartItems, this);
        recyclerView.setAdapter(cartAdapter);
        
        // Animasi untuk empty state
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        emptyCartImage.startAnimation(fadeIn);
        
        // Delay animation untuk teks
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            emptyCartTitle.setVisibility(View.VISIBLE);
            emptyCartTitle.startAnimation(fadeIn);
        }, 300);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            emptyCartDescription.setVisibility(View.VISIBLE);
            emptyCartDescription.startAnimation(fadeIn);
        }, 500);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            browseMenuButton.setVisibility(View.VISIBLE);
            browseMenuButton.startAnimation(fadeIn);
        }, 700);
        
        // Setup swipe to delete
        setupSwipeToDelete();

        // Setup checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Keranjang kosong! Tambahkan produk terlebih dahulu.", Toast.LENGTH_SHORT).show();
            } else {
                performCheckout();
            }
        });

        // Setup browse menu button
        browseMenuButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToFragment(R.id.navigation_home);
            }
        });

        // Load sample data untuk testing
        loadSampleCartItems();

        // Update tampilan berdasarkan state keranjang
        updateCartView();
    }
    
    /**
     * Mengelola proses checkout
     */
    private void performCheckout() {
        // Animasi loading
        Animation scaleOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        scaleOut.setDuration(300);
        
        checkoutButton.startAnimation(scaleOut);
        checkoutButton.setText("Memproses...");
        checkoutButton.setEnabled(false);
        
        // Simulasi proses checkout
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Toast.makeText(getContext(), "Checkout berhasil! Pesanan Anda sedang diproses.", Toast.LENGTH_SHORT).show();
            
            // Kosongkan keranjang setelah checkout
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            updateCartView();
            
            // Reset state button
            checkoutButton.setText("Checkout");
            checkoutButton.setEnabled(true);
            Animation scaleIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
            scaleIn.setDuration(300);
            checkoutButton.startAnimation(scaleIn);
        }, 1500);
    }
    
    /**
     * Setup swipe to delete untuk item di recycler view
     */
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItem deletedItem = cartItems.get(position);
                
                // Hapus item dari list
                cartItems.remove(position);
                cartAdapter.notifyItemRemoved(position);
                
                // Update tampilan keranjang
                updateCartView();
                
                // Tampilkan snackbar dengan opsi undo
                Snackbar.make(recyclerView, "Item dihapus dari keranjang", Snackbar.LENGTH_LONG)
                        .setAction("BATAL", v -> {
                            // Kembalikan item ke posisi semula
                            cartItems.add(position, deletedItem);
                            cartAdapter.notifyItemInserted(position);
                            updateCartView();
                        })
                        .show();
            }
        };
        
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Memuat data contoh untuk keranjang
     */
    private void loadSampleCartItems() {
        // Sample data untuk demo
        cartItems.add(new CartItem("Cappuccino", "Rp 32.000", 1, R.drawable.ic_cappuccino));
        cartItems.add(new CartItem("Caramel Macchiato", "Rp 38.000", 1, R.drawable.ic_latte));
        cartItems.add(new CartItem("Espresso", "Rp 25.000", 1, R.drawable.ic_espresso));
        
        // Update adapter
        cartAdapter.notifyDataSetChanged();
    }

    /**
     * Update tampilan keranjang berdasarkan status item
     */
    private void updateCartView() {
        if (cartItems.isEmpty()) {
            // Tampilkan empty state dan sembunyikan recycler view dan order summary
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            
            // Menyembunyikan fields di order summary kecuali checkout button
            subtotalTextView.setText("Rp 0");
            taxTextView.setText("Rp 0");
            totalTextView.setText("Rp 0");
            checkoutButton.setEnabled(false);
            checkoutButton.setAlpha(0.5f);
        } else {
            // Tampilkan recycler view dan sembunyikan empty state
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            // Enable checkout button
            checkoutButton.setEnabled(true);
            checkoutButton.setAlpha(1.0f);
            
            // Hitung total harga
            double subtotal = 0;
            for (CartItem item : cartItems) {
                subtotal += item.getTotalPrice();
            }
            
            double tax = subtotal * TAX_RATE;
            double total = subtotal + tax;
            
            // Update text di order summary
            subtotalTextView.setText(formatRupiah(subtotal));
            taxTextView.setText(formatRupiah(tax));
            totalTextView.setText(formatRupiah(total));
        }
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        // Update quantity item di posisi tersebut
        cartItems.get(position).setQuantity(newQuantity);
        
        // Update total harga
        updateCartView();
    }

    @Override
    public void onItemRemoved(int position) {
        // Hapus item dari list
        if (position >= 0 && position < cartItems.size()) {
            CartItem removedItem = cartItems.get(position);
            cartItems.remove(position);
            cartAdapter.notifyItemRemoved(position);
            
            // Update tampilan keranjang
            updateCartView();
            
            // Tampilkan snackbar dengan opsi undo
            Snackbar.make(recyclerView, removedItem.getName() + " dihapus dari keranjang", Snackbar.LENGTH_LONG)
                    .setAction("BATAL", v -> {
                        // Kembalikan item ke posisi semula
                        cartItems.add(position, removedItem);
                        cartAdapter.notifyItemInserted(position);
                        updateCartView();
                    })
                    .show();
        }
    }

    /**
     * Format angka ke format Rupiah
     */
    private String formatRupiah(double price) {
        NumberFormat rupiahFormat = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp " + rupiahFormat.format(price);
    }
}