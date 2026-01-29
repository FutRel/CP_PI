package com.example.cp_pi;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.Holder> {

    List<Product> products;
    DBHelper db;
    int sellerId;
    Context context;

    public SellerProductAdapter(List<Product> products, DBHelper db, int sellerId, Context context) {
        this.products = products;
        this.db = db;
        this.sellerId = sellerId;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_seller, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        Product p = products.get(position);

        h.name.setText(p.name);
        h.price.setText(p.price + " ₽");

        if (p.image != null) {
            h.image.setImageBitmap(BitmapFactory.decodeFile(p.image));
        }

        // Кнопка удаления
        h.btnDelete.setOnClickListener(v -> {
            if (db.deleteProduct(p.id, sellerId)) {
                Toast.makeText(context, "Товар удален", Toast.LENGTH_SHORT).show();
                products.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Ошибка удаления", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка редактирования
        h.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("product_id", p.id);
            intent.putExtra("product_name", p.name);
            intent.putExtra("product_price", p.price);
            intent.putExtra("product_image", p.image);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;
        Button btnDelete, btnEdit;

        public Holder(View v) {
            super(v);
            image = v.findViewById(R.id.itemImage);
            name = v.findViewById(R.id.itemName);
            price = v.findViewById(R.id.itemPrice);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit = v.findViewById(R.id.btnEdit);
        }
    }
}