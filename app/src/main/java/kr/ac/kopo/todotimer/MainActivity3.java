package kr.ac.kopo.todotimer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#1C1C1E"));
        setContentView(R.layout.activity_main3);

        EditText etName = findViewById(R.id.editRoutineName);
        NumberPicker pickerHour = findViewById(R.id.pickerHour);
        NumberPicker pickerMin = findViewById(R.id.pickerMin);
        Button btn = findViewById(R.id.btnSubmitRoutine);


        etName.setTextColor(Color.WHITE);
        etName.setHintTextColor(Color.parseColor("#8E8E93"));
        GradientDrawable etBg = new GradientDrawable();
        etBg.setColor(Color.parseColor("#2C2C2E"));
        etBg.setCornerRadius(20f);
        etName.setBackground(etBg);
        etName.setPadding(40, 40, 40, 40);

        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(23);
        pickerMin.setMinValue(0);
        pickerMin.setMaxValue(59);
        pickerMin.setValue(30);


        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9F0A")));
        btn.setTextColor(Color.WHITE);
        btn.setText("START ROUTINE");

        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setCornerRadius(30f);
        btn.setBackground(btnBg);

        btn.setOnClickListener(v -> {
            String name = etName.getText().toString();
            int totalMinutes = (pickerHour.getValue() * 60) + pickerMin.getValue();
            if (name.isEmpty() || totalMinutes == 0) {
                Toast.makeText(this, "Check your routine!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("time", totalMinutes);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}