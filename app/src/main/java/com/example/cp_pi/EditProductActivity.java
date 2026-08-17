package com.example.cp_pi;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.IOException;

public class EditProductActivity extends AppCompatActivity {

    EditText etName, etPrice;
    ImageView imgProduct;
    Button btnChoosePhoto, btnSave, btnCancel;

    DBHelper db;
    int productId;
    int sellerId;
    String currentImagePath = null;
    String selectedImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etName = findViewById(R.id.inputName);
        etPrice = findViewById(R.id.inputPrice);
        imgProduct = findViewById(R.id.newImage);
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        db = new DBHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        sellerId = sharedPref.getInt("user_id", -1);

        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        String name = intent.getStringExtra("product_name");
        double price = intent.getDoubleExtra("product_price", 0.0);
        currentImagePath = intent.getStringExtra("product_image");

        etName.setText(name);
        etPrice.setText(String.valueOf(price));

        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            File imgFile = new File(currentImagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgProduct.setImageBitmap(bitmap);
            }
        }

        btnChoosePhoto.setOnClickListener(v -> openGallery());

        btnSave.setOnClickListener(v -> saveProduct());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imgProduct.setImageBitmap(bitmap);

                File file = new File(getFilesDir(), "img_edit_" + System.currentTimeMillis() + ".png");
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                selectedImagePath = file.getAbsolutePath();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProduct() {
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

            String imagePathToSave = (selectedImagePath != null) ? selectedImagePath : currentImagePath;

            boolean success = db.updateProduct(productId, name, price, imagePathToSave, sellerId);

            if (success) {
                Toast.makeText(this, "Товар успешно обновлен", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка обновления товара", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректную цену", Toast.LENGTH_SHORT).show();
        }
    }
}