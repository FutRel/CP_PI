package com.example.cp_pi;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnLogin, btnToReg;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etUser = findViewById(R.id.loginUser);
        etPass = findViewById(R.id.loginPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnToReg = findViewById(R.id.btnToRegister);
        db = new DBHelper(this);

        btnLogin.setOnClickListener(v -> {
            String u = etUser.getText().toString();
            String p = etPass.getText().toString();

            String h = PasswordUtils.hash(p);
            User user = db.checkLogin(u, h);

            if (user != null) {
                Toast.makeText(this, "Успешный вход", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("user_id", user.getId());
                editor.putString("username", user.getUsername());
                editor.putString("role", user.getRole());
                editor.apply();

                if (user.isSeller()) {
                    startActivity(new Intent(MainActivity.this, SellerActivity.class));
                } else if (user.isAdmin()) {
                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show();
            }
        });

        btnToReg.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }
}