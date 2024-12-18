package com.example.dailygoalsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "MainActivity";

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

            // 自訂圖片設置
            bottomNavigationView.getMenu().findItem(R.id.navigation_completed_goals).setIcon(R.drawable.listicon_finish);
            bottomNavigationView.getMenu().findItem(R.id.navigation_goals).setIcon(R.drawable.listicon_task);
            bottomNavigationView.getMenu().findItem(R.id.navigation_settings).setIcon(R.drawable.listicon_setting);

            // 設置 itemIconTintList 為 null，顯示自訂圖片顏色
            bottomNavigationView.setItemIconTintList(null);

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

            // 在啟動時載入初始任務
            fetchUserTask(userProfile);
        }
    }

    public void fetchUserTask(UserProfile userProfile) {
        new Thread(() -> {
            try {
                // 建立 JSON 資料
                JSONObject json = new JSONObject();
                json.put("sex", userProfile.getGender());
                json.put("age_range", userProfile.getAgeRange());
                json.put("intensity", userProfile.getExerciseIntensity());
                json.put("height", userProfile.getHeight());
                json.put("weight", userProfile.getWeight());

                RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

                // 設置請求
                Request request = new Request.Builder()
                        .url("https://dailygoalsapp-server.onrender.com/generate")
                        .post(body)
                        .build();

                // 發送請求
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, "Network request failed", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        if (response.body() != null) {
                            String responseBody = response.body().string();
                            Log.d(TAG, "Response: " + responseBody);
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                String task = jsonResponse.getString("task");
                                String hint = jsonResponse.getString("hints");

                                // 更新 UI 或儲存回應資料
                                runOnUiThread(() -> {
                                    // 更新顯示任務的 TextView
                                    TextView goalTextView = findViewById(R.id.goalTextView);
                                    goalTextView.setText(task);

                                    // 可選：顯示提示
                                    // TextView hintTextView = findViewById(R.id.hintTextView);
                                    // hintTextView.setText(hint);
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "Failed to parse JSON", e);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Failed to fetch user task", e);
            }
        }).start();
    }
}
