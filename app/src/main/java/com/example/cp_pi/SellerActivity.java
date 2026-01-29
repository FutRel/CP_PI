package com.example.cp_pi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import java.util.List;

public class SellerActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAdd, btnLogout;
    DBHelper db;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        recycler = findViewById(R.id.recyclerProducts);
        btnAdd = findViewById(R.id.btnAddProduct);
        btnLogout = findViewById(R.id.btnLogout);
        db = new DBHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        sellerId = sharedPref.getInt("user_id", -1);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewProductActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPref2 = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref2.edit();
            editor.clear();
            editor.apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    void loadProducts() {
        List<Product> products = db.getProductsBySeller(sellerId);
        recycler.setAdapter(new SellerProductAdapter(products, db, sellerId, this));
    }
}