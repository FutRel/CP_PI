package com.example.cp_pi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAdd;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recycler = findViewById(R.id.recyclerProducts);
        btnAdd = findViewById(R.id.btnAddProduct);
        db = new DBHelper(this);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, NewProductActivity.class)));
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