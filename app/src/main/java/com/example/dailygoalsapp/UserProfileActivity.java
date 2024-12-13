package com.example.dailygoalsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private Spinner heightSpinner, weightSpinner, genderSpinner, frequencySpinner;
    private Button submitButton;
    private UserProfileDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // 綁定 UI 元素
        heightSpinner = findViewById(R.id.heightSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        frequencySpinner = findViewById(R.id.frequencySpinner);
        submitButton = findViewById(R.id.submitButton);

        dbHelper = new UserProfileDatabaseHelper(this);

        // 設置 Spinner 選項
        setupSpinners();

        // 設置提交按鈕的點擊事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 獲取 Spinner 中選擇的值
                String height = heightSpinner.getSelectedItem().toString();
                String weight = weightSpinner.getSelectedItem().toString();
                String gender = genderSpinner.getSelectedItem().toString();
                String exerciseFrequency = frequencySpinner.getSelectedItem().toString();

                // 儲存使用者資料
                dbHelper.insertUserProfile(height, weight, gender, exerciseFrequency);

                // 顯示提示並返回主頁面
                Toast.makeText(UserProfileActivity.this, "感謝填寫！", Toast.LENGTH_SHORT).show();

                // 跳轉到主頁面
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 返回主頁面並結束此頁面
            }
        });
    }

    private void setupSpinners() {
        // 身高選項
        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this,
                R.array.height_options, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);

        // 體重選項
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_options, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        // 性別選項
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // 運動頻率選項
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.exercise_frequency_options, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);
    }
}
