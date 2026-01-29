package com.example.cp_pi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "users_products.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT DEFAULT 'user')");
        db.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, image TEXT, seller_id INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public Boolean insertUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("role", role);
        return db.insert("users", null, cv) != -1;
    }

    public User checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, username, role FROM users WHERE username=? AND password=?",
                new String[]{username, password});
        if (c.moveToFirst()) {
            return new User(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2)
            );
        }
        return null;
    }

    public List<Product> getProductsBySeller(int sellerId) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM products WHERE seller_id=?",
                new String[]{String.valueOf(sellerId)});

        while (c.moveToNext()) {
            list.add(new Product(
                    c.getInt(0),
                    c.getString(1),
                    c.getDouble(2),
                    c.getString(3),
                    sellerId
            ));
        }
        return list;
    }

    public List<Product> getProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM products", null);


        while (c.moveToNext()) {
            list.add(new Product(
                    c.getInt(0),
                    c.getString(1),
                    c.getDouble(2),
                    c.getString(3),
                    c.getInt(4)
            ));
        }
        return list;
    }

    public Boolean insertProduct(String name, double price, String image, int sellerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("image", image);
        cv.put("seller_id", sellerId);
        return db.insert("products", null, cv) != -1;
    }

    public Boolean deleteProduct(int productId, int sellerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products", "id=? AND seller_id=?",
                new String[]{String.valueOf(productId), String.valueOf(sellerId)}) > 0;
    }

    public Boolean updateProduct(int productId, String name, double price, String imagePath, int sellerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);

        if (imagePath != null && !imagePath.isEmpty()) {
            cv.put("image", imagePath);
        }

        return db.update("products", cv, "id=? AND seller_id=?",
                new String[]{String.valueOf(productId), String.valueOf(sellerId)}) > 0;
    }
}