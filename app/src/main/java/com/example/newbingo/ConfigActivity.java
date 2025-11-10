package com.example.newbingo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ConfigActivity extends AppCompatActivity {

    private EditText etOldUser, etOldPass, etNewUser, etNewPass;
    private Button btnConfirm;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbar_config);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prefs = getSharedPreferences("login.xml", MODE_PRIVATE);
        etOldUser = findViewById(R.id.et_existing_username);
        etOldPass = findViewById(R.id.et_existing_password);
        etNewUser = findViewById(R.id.et_new_username);
        etNewPass = findViewById(R.id.et_new_password);
        btnConfirm = findViewById(R.id.btn_confirm);

        etOldUser.setText(prefs.getString("username", ""));
        etOldPass.setText(String.valueOf(prefs.getInt("password", 0)));

        btnConfirm.setOnClickListener(v -> validate());
    }

    private void validate() {
        String oldU = etOldUser.getText().toString().trim();
        String oldP = etOldPass.getText().toString().trim();
        String newU = etNewUser.getText().toString().trim();
        String newP = etNewPass.getText().toString().trim();

        String savedU = prefs.getString("username", "");
        int savedP = prefs.getInt("password", 0);

        if (!oldU.equals(savedU) || !oldP.equals(String.valueOf(savedP))) {
            alert("Existing username/password is not correct!");
            return;
        }

        if (newU.isEmpty() || newP.isEmpty()) {
            alert("New username/password is not enter! Data is remind unchanged");
            return;
        }

        prefs.edit().putString("username", newU).putInt("password", Integer.parseInt(newP)).apply();
        alert("New username/password is confirmed! Data is updated");

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 2000);
    }

    private void alert(String msg) {
        new AlertDialog.Builder(this).setMessage(msg).setPositiveButton("OK", null).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}