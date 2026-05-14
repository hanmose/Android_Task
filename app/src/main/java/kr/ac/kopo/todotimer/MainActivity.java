package kr.ac.kopo.todotimer;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LinearLayout todoLayout;
    private GridLayout routineGrid;
    private boolean isEditMode = false;
    private final ArrayList<View> deleteButtons = new ArrayList<>();

    private final ActivityResultLauncher<Intent> todoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    addTodoView(result.getData().getStringExtra("name"));
                }
            }
    );

    private final ActivityResultLauncher<Intent> routineLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String name = result.getData().getStringExtra("name");
                    int minutes = result.getData().getIntExtra("time", 0);
                    addRoutineView(name, minutes);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#1C1C1E"));
        setContentView(R.layout.activity_main);

        todoLayout = findViewById(R.id.todoLayout);
        routineGrid = findViewById(R.id.routineGrid);

        setupTopButton(R.id.btnGoTo2, "#FF9F0A");
        setupTopButton(R.id.btnGoTo3, "#FF9F0A");
        setupTopButton(R.id.btnEdit, "#3A3A3C");

        findViewById(R.id.btnGoTo2).setOnClickListener(v ->
                todoLauncher.launch(new Intent(this, MainActivity2.class)));

        findViewById(R.id.btnGoTo3).setOnClickListener(v ->
                routineLauncher.launch(new Intent(this, MainActivity3.class)));

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            btnEdit.setText(isEditMode ? "DONE" : "EDIT");
            btnEdit.setBackgroundTintList(ColorStateList.valueOf(
                    Color.parseColor(isEditMode ? "#30D158" : "#3A3A3C")));
            for (View btn : deleteButtons) {
                btn.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setupTopButton(int id, String colorStr) {
        Button btn = findViewById(id);
        if (btn != null) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorStr)));
            btn.setTextColor(Color.WHITE);
            btn.setAllCaps(false);
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(20f);
            gd.setColor(Color.parseColor(colorStr));
            btn.setBackground(gd);
        }
    }

    private void addTodoView(String name) {
        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(Color.parseColor("#2C2C2E"));
        cardBg.setCornerRadius(25f);

        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(-1, -2);
        rowParams.setMargins(0, 10, 0, 10);
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(35, 35, 35, 35);
        row.setBackground(cardBg);

        CheckBox cb = new CheckBox(this);
        updateCheckBoxStyle(cb, false);
        cb.setOnCheckedChangeListener((bv, isChecked) -> updateCheckBoxStyle(cb, isChecked));

        TextView tv = new TextView(this);
        tv.setText(name);
        tv.setTextSize(17);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(25, 0, 0, 0);
        tv.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));

        TextView btnDel = new TextView(this);
        btnDel.setText("✕");
        btnDel.setTextColor(Color.parseColor("#8E8E93"));
        btnDel.setTextSize(20);
        btnDel.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        btnDel.setOnClickListener(v -> {
            deleteButtons.remove(btnDel);
            todoLayout.removeView(row);
        });

        deleteButtons.add(btnDel);
        row.addView(cb);
        row.addView(tv);
        row.addView(btnDel);
        todoLayout.addView(row);
    }

    private void updateCheckBoxStyle(CheckBox cb, boolean isChecked) {
        GradientDrawable d = new GradientDrawable();
        d.setCornerRadius(12f);
        d.setSize(55, 55);
        if (isChecked) {
            d.setColor(Color.parseColor("#FF9F0A"));
        } else {
            d.setColor(Color.TRANSPARENT);
            d.setStroke(3, Color.parseColor("#636366"));
        }
        cb.setButtonDrawable(d);
    }

    private void addRoutineView(String name, int minutes) {
        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(Color.parseColor("#2C2C2E"));
        cardBg.setCornerRadius(30f);

        LinearLayout box = new LinearLayout(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0; params.height = -2;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
        params.setMargins(12, 12, 12, 12);
        box.setLayoutParams(params);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER_HORIZONTAL);
        box.setPadding(20, 35, 20, 35);
        box.setBackground(cardBg);

        TextView btnDel = new TextView(this);
        btnDel.setText("X");
        btnDel.setTextColor(Color.parseColor("#FF453A"));
        btnDel.setTextSize(11);
        btnDel.setPadding(0, 0, 0, 10);
        btnDel.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        deleteButtons.add(btnDel);

        MainActivity4 timerView = new MainActivity4(this);
        timerView.setLayoutParams(new LinearLayout.LayoutParams(350, 350));

        TextView timeTv = new TextView(this);
        timeTv.setTextColor(Color.parseColor("#30D158"));
        timeTv.setTextSize(20);
        timeTv.setPadding(0, 12, 0, 4);

        TextView nameTv = new TextView(this);
        nameTv.setText(name);
        nameTv.setTextColor(Color.WHITE);
        nameTv.setTextSize(14);

        box.addView(btnDel);
        box.addView(timerView);
        box.addView(timeTv);
        box.addView(nameTv);
        routineGrid.addView(box);

        final CountDownTimer[] timer = new CountDownTimer[1];
        btnDel.setOnClickListener(v -> {
            if (timer[0] != null) timer[0].cancel();
            deleteButtons.remove(btnDel);
            routineGrid.removeView(box);
        });

        startTimer(timerView, timeTv, minutes, name, timer);
    }

    private void startTimer(MainActivity4 tv, TextView ttv, int m, String n, CountDownTimer[] timer) {
        timer[0] = new CountDownTimer(m * 60000L, 100) {
            @SuppressLint("SetTextI18n")
            public void onTick(long ms) {
                tv.setProgress(ms / 3600000.0f);
                ttv.setText(String.format(Locale.getDefault(), "%02d:%02d", ms/60000, (ms%60000)/1000));

                if (ms <= 10000 && ms > 0) {
                    triggerHeartbeat();
                }
            }
            public void onFinish() {
                showSimpleSnackbar(n + " 시간이에요!");
                startTimer(tv, ttv, m, n, timer);
            }
        }.start();
    }

    private void showSimpleSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        if (rootView == null) return;

        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(15f);
        bg.setColor(Color.parseColor("#3A3A3C"));
        snackbarView.setBackground(bg);

        TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (snackTextView != null) {
            snackTextView.setTextColor(Color.WHITE);
            snackTextView.setTextSize(16);
            snackTextView.setGravity(Gravity.CENTER);
        }

        ViewGroup.LayoutParams p = snackbarView.getLayoutParams();
        if (p instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) p).setMargins(40, 0, 40, 70);
            snackbarView.setLayoutParams(p);
        }

        snackbar.show();
    }

    private void triggerHeartbeat() {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(20, 100));
            } else {
                v.vibrate(20);
            }
        }
    }
}