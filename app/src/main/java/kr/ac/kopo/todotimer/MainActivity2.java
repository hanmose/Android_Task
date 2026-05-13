package kr.ac.kopo.todotimer;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 배경을 다크 모드로 강제 설정
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#1C1C1E"));
        setContentView(R.layout.activity_main2);

        EditText et = findViewById(R.id.editTodoName);
        Button btn = findViewById(R.id.btnSubmitTodo);

        // 입력창: 다크 카드 스타일
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.parseColor("#8E8E93"));
        GradientDrawable etBg = new GradientDrawable();
        etBg.setColor(Color.parseColor("#2C2C2E"));
        etBg.setCornerRadius(20f);
        et.setBackground(etBg);
        et.setPadding(40, 40, 40, 40);

        // 버튼: 네온 오렌지 적용 (보라색 박멸)
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9F0A")));
        btn.setTextColor(Color.WHITE);
        btn.setText("ADD TODO");

        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setCornerRadius(30f);
        btn.setBackground(btnBg);

        btn.setOnClickListener(v -> {
            android.content.Intent resultIntent = new android.content.Intent();
            resultIntent.putExtra("name", et.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}