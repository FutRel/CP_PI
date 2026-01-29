package com.example.cp_pi;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

public class RegisterActivity extends AppCompatActivity {

    EditText etUser, etPass;
    RadioGroup radioGroupRole;
    RadioButton radioUser, radioSeller;
    Button btnReg, btnBack;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        etUser = findViewById(R.id.regUser);
        etPass = findViewById(R.id.regPass);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioUser = findViewById(R.id.radioUser);
        radioSeller = findViewById(R.id.radioSeller);
        btnReg = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        db = new DBHelper(this);

        btnReg.setOnClickListener(v -> {
            String u = etUser.getText().toString();
            String p = etPass.getText().toString();
            String role = radioUser.isChecked() ? "user" : "seller";

            String h = PasswordUtils.hash(p);

            if (db.insertUser(u, h, role)) {
                Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка: логин занят", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });
    }
}