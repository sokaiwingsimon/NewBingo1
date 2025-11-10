package com.example.newbingo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnEnter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnEnter = findViewById(R.id.btn_enter);

        prefs = getSharedPreferences("login.xml", MODE_PRIVATE);

        // 設定預設帳號密碼
        if (!prefs.contains("username")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", "Tommy");
            editor.putInt("password", 123456);
            editor.apply();
        }

        btnEnter.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            String savedUser = prefs.getString("username", "");
            int savedPass = prefs.getInt("password", 0);

            if (user.equals(savedUser) && pass.equals(String.valueOf(savedPass))) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Login Failed")
                        .setMessage("Your data is incorrect!! Enter again")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}