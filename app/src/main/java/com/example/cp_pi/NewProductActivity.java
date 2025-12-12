package com.example.cp_pi;

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
import java.io.File;
import java.io.FileOutputStream;

public class NewProductActivity extends AppCompatActivity {

    EditText etName, etPrice;
    ImageView img;
    Button btnChoose, btnSave;

    String savedImagePath = null;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        getSupportActionBar().hide();

        etName = findViewById(R.id.inputName);
        etPrice = findViewById(R.id.inputPrice);
        //img = findViewById(R.id.newImage);
        btnChoose = findViewById(R.id.btnChoosePhoto);
        btnSave = findViewById(R.id.btnSave);

        db = new DBHelper(this);

        btnChoose.setOnClickListener(v -> openGallery());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            double price = Double.parseDouble(etPrice.getText().toString());
            db.insertProduct(name, price, savedImagePath);
            Toast.makeText(this, "Добавлено", Toast.LENGTH_SHORT).show();
            finish();
        });
    }


    void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }


    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == 1 && res == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bmp);

                File f = new File(getFilesDir(), "img" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();

                savedImagePath = f.getAbsolutePath();
            } catch (Exception ignored) {}
        }
    }
}