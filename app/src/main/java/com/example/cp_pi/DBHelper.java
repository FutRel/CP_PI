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
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public Boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        return db.insert("users", null, cv) != -1;
    }

    public Boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        //test string to operate (test) with db
        //db.execSQL("INSERT INTO products(name, price, image) VALUES ('Мочалка', '120', 'img1.png'), ('Поплавок', '20', 'img2.png'), ('Обувь мужская', '1200', 'img3.png'), ('Трактор', '120000', 'img4.png'), ('Усилитель связи', '5000', 'img5.png')");
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        return c.getCount() > 0;
    }

    public Boolean insertProduct(String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("image", image);
        return db.insert("products", null, cv) != -1;
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
                    c.getString(3)
            ));
        }
        return list;
    }
}