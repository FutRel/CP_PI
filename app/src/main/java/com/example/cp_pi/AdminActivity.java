package com.example.cp_pi;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnLogout, btnImport, btnExport;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recycler = findViewById(R.id.recyclerProducts);
        btnLogout = findViewById(R.id.btnLogout);
        btnImport = findViewById(R.id.btnImport);
        btnExport = findViewById(R.id.btnExport);
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

        btnExport.setOnClickListener(v -> exportData());

        btnImport.setOnClickListener(v -> {
            // Заглушка для импорта
            Toast.makeText(this, "Функция импорта будет доступна в следующей версии", Toast.LENGTH_SHORT).show();
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

    private void exportData() {
        try {
            // Создаем имя файла с текущей датой и временем
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fileName = "database_backup_" + sdf.format(new Date()) + ".txt";

            // Путь к папке Downloads
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            // ЭКСПОРТ ТАБЛИЦЫ USERS
            writer.write("=== ТАБЛИЦА: users ===\n");
            writer.write("ID\tИмя пользователя\tПароль\tРоль\n");
            writer.write("----------------------------------------\n");

            Cursor usersCursor = db.getReadableDatabase().rawQuery("SELECT * FROM users", null);
            int userCount = 0;

            while (usersCursor.moveToNext()) {
                writer.write(
                        usersCursor.getInt(0) + "\t" +           // id
                                usersCursor.getString(1) + "\t" +        // username
                                usersCursor.getString(2) + "\t" +        // password
                                usersCursor.getString(3) + "\n"          // role
                );
                userCount++;
            }
            usersCursor.close();

            writer.write("\nВсего пользователей: " + userCount + "\n\n");

            // ЭКСПОРТ ТАБЛИЦЫ PRODUCTS
            writer.write("=== ТАБЛИЦА: products ===\n");
            writer.write("ID\tНазвание\tЦена\tИзображение\tID продавца\n");
            writer.write("------------------------------------------------\n");

            Cursor productsCursor = db.getReadableDatabase().rawQuery("SELECT * FROM products", null);
            int productCount = 0;

            while (productsCursor.moveToNext()) {
                writer.write(
                        productsCursor.getInt(0) + "\t" +        // id
                                productsCursor.getString(1) + "\t" +     // name
                                productsCursor.getDouble(2) + "\t" +     // price
                                productsCursor.getString(3) + "\t" +     // image
                                productsCursor.getInt(4) + "\n"          // seller_id
                );
                productCount++;
            }
            productsCursor.close();

            writer.write("\nВсего товаров: " + productCount + "\n");
            writer.write("\nДата экспорта: " + new Date().toString());

            writer.close();
            fos.close();

            if (userCount == 0 && productCount == 0) {
                Toast.makeText(this, "Экспорт завершен. Файл пуст (нет данных в БД)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Данные экспортированы в папку Downloads:" + fileName,
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при экспорте: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}