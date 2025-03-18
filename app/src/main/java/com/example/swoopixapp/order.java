package com.example.swoopixapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class order extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ConstraintLayout emptyState;
    private TextView subtotalTextView;
    private TextView taxTextView;
    private TextView totalTextView;
    private MaterialButton checkoutButton;
    private Button browseMenuButton;

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

        // Inisialisasi views
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        emptyState = view.findViewById(R.id.empty_state);
        subtotalTextView = view.findViewById(R.id.subtotal_value);
        taxTextView = view.findViewById(R.id.tax_value);
        totalTextView = view.findViewById(R.id.total_value);
        checkoutButton = view.findViewById(R.id.checkout_button);
        browseMenuButton = view.findViewById(R.id.browse_menu_button);

        // Setup RecyclerView dan adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), this);
        recyclerView.setAdapter(cartAdapter);

        // Tambahkan sampel data (ini akan diganti dengan data sebenarnya dari pengguna)
        loadSampleCartItems();

        // Setup button listeners
        checkoutButton.setOnClickListener(v -> {
            if (cartAdapter.getItemCount() > 0) {
                Toast.makeText(getContext(), "Menuju halaman pembayaran...", Toast.LENGTH_SHORT).show();
                // Implementasi navigasi ke halaman checkout di sini
            }
        });

        browseMenuButton.setOnClickListener(v -> {
            // Navigasi ke halaman menu
            if (getActivity() != null) {
                // Gunakan metode publik di MainActivity untuk menavigasi ke Home
                ((MainActivity) getActivity()).navigateToFragment(R.id.navigation_home);
                // Atau alternatif lain gunakan FragmentManager untuk langsung mengganti fragment
                // getActivity().getSupportFragmentManager().beginTransaction()
                //         .replace(R.id.frame_layout, new home())
                //         .commit();
            }
        });

        // Update tampilan keranjang
        updateCartView();
    }

    private void loadSampleCartItems() {
        List<CartItem> sampleItems = new ArrayList<>();
        
        // Tambahkan beberapa item sampel
        sampleItems.add(new CartItem(1, "Caffe Latte", 25000, 1, R.drawable.ic_latte));
        sampleItems.add(new CartItem(2, "Cappuccino", 28000, 2, R.drawable.ic_cappuccino));
        
        // Set item ke adapter
        cartAdapter.setCartItems(sampleItems);
        
        // Update UI keranjang
        updateCartView();
    }

    private void updateCartView() {
        int itemCount = cartAdapter.getItemCount();
        
        if (itemCount == 0) {
            // Tampilkan tampilan keranjang kosong
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            subtotalTextView.setText(formatRupiah(0));
            taxTextView.setText(formatRupiah(0));
            totalTextView.setText(formatRupiah(0));
            checkoutButton.setEnabled(false);
        } else {
            // Tampilkan daftar item dan ringkasan
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            // Hitung subtotal, pajak, dan total
            double subtotal = cartAdapter.getSubtotal();
            double tax = subtotal * TAX_RATE;
            double total = subtotal + tax;
            
            // Update tampilan
            subtotalTextView.setText(formatRupiah(subtotal));
            taxTextView.setText(formatRupiah(tax));
            totalTextView.setText(formatRupiah(total));
            checkoutButton.setEnabled(true);
        }
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        // Update total ketika quantity berubah
        updateCartView();
    }

    @Override
    public void onItemRemoved(int position) {
        // Hapus item dari keranjang
        cartAdapter.removeItem(position);
        
        // Update UI
        updateCartView();
        
        // Tampilkan pesan
        Toast.makeText(getContext(), "Item dihapus dari keranjang", Toast.LENGTH_SHORT).show();
    }

    private String formatRupiah(double price) {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return formatRupiah.format(price);
    }

    // Metode untuk menambahkan item dari luar fragment (akan dipanggil dari fragment lain)
    public void addToCart(CartItem item) {
        if (cartAdapter != null) {
            cartAdapter.addItem(item);
            updateCartView();
            Toast.makeText(getContext(), item.getName() + " ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("order", "Cart adapter is null");
        }
    }
}