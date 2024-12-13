package com.example.dailygoalsapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 檢查基本資料是否存在
        UserProfileDatabaseHelper userProfileDbHelper = new UserProfileDatabaseHelper(this);
        UserProfile userProfile = userProfileDbHelper.getUserProfile();

        if (userProfile == null) {
            // 如果基本資料不存在，跳轉到基本資料頁面
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            // 基本資料已存在，初始化主功能
            setContentView(R.layout.activity_main);

            // 預設顯示 "目標" Fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new GoalsFragment())
                    .commit();

            // 取得 BottomNavigationView
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            // 設置 BottomNavigationView 的選項選擇監聽器
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment selectedFragment;

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_completed_goals) {
                    selectedFragment = new CompletedGoalsFragment();
                } else if (itemId == R.id.navigation_goals) {
                    selectedFragment = new GoalsFragment();
                } else {
                    selectedFragment = new SettingsFragment();
                }

                // 替換 Fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();

                return true;
            });
        }
    }
}
