package com.example.newbingo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.*;
import java.util.Collections;
import java.util.List;
// UI elements

public class MainActivity extends AppCompatActivity {

    private TextView tvCalledNumber;
    private Button btnNewGame;
    private ImageView ivConfig;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isGameRunning = false;
    private List<Integer> calledNumbers = new ArrayList<>();
    private Set<Integer> marked = new HashSet<>();


    private final int[][] cells = {
            {R.id.tv_b7, R.id.tv_b26, R.id.tv_b40, R.id.tv_b58, R.id.tv_b73},
            {R.id.tv_i14, R.id.tv_i22, R.id.tv_i34, R.id.tv_i55, R.id.tv_i68},
            {R.id.tv_n4, R.id.tv_n24, R.id.tv_free, R.id.tv_n46, R.id.tv_n72},
            {R.id.tv_g9, R.id.tv_g20, R.id.tv_g36, R.id.tv_g52, R.id.tv_g74},
            {R.id.tv_o6, R.id.tv_o28, R.id.tv_o35, R.id.tv_o49, R.id.tv_o64}
    };

    private final int[] numbers = {7,26,40,58,73,14,22,34,55,68,4,24,46,72,9,20,36,52,74,6,28,35,49,64};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        tvCalledNumber = findViewById(R.id.tv_called_number);
        btnNewGame    = findViewById(R.id.btn_new_game);
        ivConfig      = findViewById(R.id.iv_config);

        ivConfig.setOnClickListener(v -> startActivity(new Intent(this, ConfigActivity.class)));

        // 修正點：使用 if-else，避免 void 回傳
        btnNewGame.setOnClickListener(v -> {
            if (isGameRunning) {
                stopGame();
            } else {
                startGame();
            }
        });

        // FREE 格子預先標記
        findViewById(R.id.tv_free).setBackgroundColor(Color.parseColor("#FF5252"));
    }

    /* ---------- 以下方法不變 ---------- */
    private void startGame() {
        resetBoard();
        calledNumbers.clear();
        marked.clear();
        isGameRunning = true;
        btnNewGame.setText("Stop Game");
        tvCalledNumber.setVisibility(TextView.VISIBLE);
        tvCalledNumber.setText("Ready...");
        handler.postDelayed(this::callNumber, 2000);
    }

    private void stopGame() {
        isGameRunning = false;
        btnNewGame.setText("New Game");
        handler.removeCallbacksAndMessages(null);
        tvCalledNumber.setText("Stopped");
    }

    private void resetBoard() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                int id = cells[r][c];
                if (id != R.id.tv_free) {
                    findViewById(id).setBackgroundColor(Color.parseColor("#FFEBEE"));
                }
            }
        }
    }

    private void callNumber() {
        if (!isGameRunning) return;
        List<Integer> remain = new ArrayList<>();
        for (int n : numbers) if (!calledNumbers.contains(n)) remain.add(n);
        if (remain.isEmpty()) { tvCalledNumber.setText("All called!"); stopGame(); return; }

        Collections.shuffle(remain);
        int num = remain.get(0);
        calledNumbers.add(num);
        tvCalledNumber.setText(getLetter(num) + " " + num);
        markCell(num);
        if (checkBingo()) { showWin(); stopGame(); return; }
        handler.postDelayed(this::callNumber, 3000);
    }

    private String getLetter(int n) {
        if (n <= 15) return "B";
        if (n <= 30) return "I";
        if (n <= 45) return "N";
        if (n <= 60) return "G";
        return "O";
    }

    private void markCell(int num) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                TextView tv = findViewById(cells[r][c]);
                if (tv.getText().toString().equals(String.valueOf(num))) {
                    tv.setBackgroundColor(Color.parseColor("#FF5252"));
                    marked.add(r * 5 + c);
                    return;
                }
            }
        }
    }

    private boolean checkBingo() {
        for (int i = 0; i < 5; i++) {
            if (marked.containsAll(Set.of(i*5, i*5+1, i*5+2, i*5+3, i*5+4))) return true;
            if (marked.containsAll(Set.of(i, 5+i, 10+i, 15+i, 20+i))) return true;
        }
        if (marked.containsAll(Set.of(0,6,12,18,24))) return true;
        if (marked.containsAll(Set.of(4,8,12,16,20))) return true;
        return false;
    }

    private void showWin() {
        new AlertDialog.Builder(this)
                .setTitle("BINGO!")
                .setMessage("You Win!")
                .setPositiveButton("Play Again", (d,w) -> startGame())
                .setNegativeButton("Exit", (d,w) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Logout?")
                    .setPositiveButton("Yes", (d,w) -> {
                        getSharedPreferences("login.xml", MODE_PRIVATE).edit().clear().apply();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
