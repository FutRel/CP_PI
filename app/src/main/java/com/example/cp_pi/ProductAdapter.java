package com.example.cp_pi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Holder> {

    List<Product> list;

    public ProductAdapter(List<Product> list) { this.list = list; }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder h, int pos) {
        Product p = list.get(pos);
        h.name.setText(p.name);
        h.price.setText(p.price + " ₽");

        if (p.image != null) {
            Bitmap bmp = BitmapFactory.decodeFile(p.image);
            h.image.setImageBitmap(bmp);
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        Holder(View v) {
            super(v);
            name = v.findViewById(R.id.txtName);
            price = v.findViewById(R.id.txtPrice);
            image = v.findViewById(R.id.imgProduct);
        }
    }
}