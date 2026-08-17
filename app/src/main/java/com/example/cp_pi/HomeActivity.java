package com.example.cp_pi;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnLogout;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recycler = findViewById(R.id.recyclerProducts);
        btnLogout = findViewById(R.id.btnLogout);
        db = new DBHelper(this);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
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
        List<Product> products = db.getProducts();
        recycler.setAdapter(new ProductAdapter(products));
    }
}