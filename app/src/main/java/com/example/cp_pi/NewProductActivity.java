package com.example.cp_pi;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.File;
import java.io.FileOutputStream;

public class NewProductActivity extends AppCompatActivity {

    EditText etName, etPrice;
    ImageView img;
    Button btnChoose, btnSave, btnBk;

    String savedImagePath = null;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etName = findViewById(R.id.inputName);
        etPrice = findViewById(R.id.inputPrice);
        img = findViewById(R.id.newImage);
        btnChoose = findViewById(R.id.btnChoosePhoto);
        btnSave = findViewById(R.id.btnSave);
        btnBk = findViewById(R.id.btnBk);

        db = new DBHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int sellerId = sharedPref.getInt("user_id", -1);

        btnChoose.setOnClickListener(v -> openGallery());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Введите название товара", Toast.LENGTH_SHORT).show();
                return;
            }

            if (priceStr.isEmpty()) {
                Toast.makeText(this, "Введите цену товара", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);

                boolean success = db.insertProduct(name, price, savedImagePath, sellerId);

                if (success) {
                    Toast.makeText(this, "Товар успешно добавлен", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Ошибка добавления товара", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Введите корректную цену", Toast.LENGTH_SHORT).show();
            }
        });

        btnBk.setOnClickListener(view -> {
            startActivity(new Intent(NewProductActivity.this, HomeActivity.class));
        });
    }


    void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }


    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

        if (req == 1 && res == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bmp);

                // сохраняем bitmap во внутреннюю память
                File file = new File(getFilesDir(), "img_" + System.currentTimeMillis() + ".png");
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                savedImagePath = file.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}