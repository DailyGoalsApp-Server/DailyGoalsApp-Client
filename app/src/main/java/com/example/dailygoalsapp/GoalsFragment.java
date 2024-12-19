package com.example.dailygoalsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GoalsFragment extends Fragment {

    private final OkHttpClient client = new OkHttpClient();
    private UserProfile userProfile;
    private TextView goalTextView, hintTextView;
    private Button changeGoalButton, completeGoalButton;
    private GoalDatabaseHelper dbHelper;
    private static final String TAG = "GoalsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 載入 Fragment 的佈局
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        goalTextView = view.findViewById(R.id.goalTextView);
        hintTextView = view.findViewById(R.id.hintTextView);
        changeGoalButton = view.findViewById(R.id.changeGoalButton);
        completeGoalButton = view.findViewById(R.id.completeGoalButton);
        dbHelper = new GoalDatabaseHelper(getContext());

        // 取得 UserProfile
        UserProfileDatabaseHelper userProfileDbHelper = new UserProfileDatabaseHelper(getActivity());
        userProfile = userProfileDbHelper.getUserProfile();

        // 從 SharedPreferences 讀取已儲存的目標
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GoalPrefs", Context.MODE_PRIVATE);
        String savedGoal = sharedPreferences.getString("currentGoal", "");
        goalTextView.setText(savedGoal);
        hintTextView.setText(sharedPreferences.getString("currentHint", ""));

        // 設置 changeGoalButton 按鈕的點擊事件
        changeGoalButton.setOnClickListener(v -> fetchUserTask());

        // 設置 completeGoalButton 按鈕的點擊事件
        completeGoalButton.setOnClickListener(v -> {
            String completedGoal = goalTextView.getText().toString();
            String completionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            dbHelper.insertGoal(completedGoal, completionTime);
            goalTextView.setText("");
            hintTextView.setText("");
            fetchUserTask();
        });

        // 如果沒有已儲存的目標，載入初始任務
        if (savedGoal.isEmpty()) {
            fetchUserTask();
        }

        return view;
    }

    private void fetchUserTask() {
        new Thread(() -> {
            try {
                // 建立 JSON 資料
                JSONObject json = new JSONObject();
                json.put("sex", userProfile.getGender());
                json.put("age_range", userProfile.getAgeRange());
                json.put("intensity", userProfile.getExerciseIntensity());
                json.put("height_range", userProfile.getHeight());
                json.put("weight_range", userProfile.getWeight());

                RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
                Log.d(TAG, "我傳的" + json);

                // 設置請求
                Request request = new Request.Builder()
                        .url("https://dailygoalsapp-server.onrender.com/generate")
                        .post(body)
                        .build();

                // 發送請求
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Request Failed: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        // 在這裡處理伺服器回應
                        String responseBody = response.body().string();
                        Log.d(TAG, "Response: " + responseBody); // 使用 Logcat 打印伺服器回傳的 JSON

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String task = jsonResponse.getString("task");
                            String hintsString = jsonResponse.getString("hints");

                            // 移除不需要的字符並使用 split(",") 分割 hints 字符串
                            hintsString = hintsString.replace("\"", "").replace("[", "").replace("]", "");
                            String[] hintsArray = hintsString.split(",");
                            StringBuilder hintsBuilder = new StringBuilder();
                            for (String hint : hintsArray) {
                                hintsBuilder.append("● ").append(hint.trim()).append("\n");
                            }
                            String hints = hintsBuilder.toString().trim();

                            // 更新 UI 或儲存回應資料
                            getActivity().runOnUiThread(() -> {
                                // 更新顯示任務的 TextView
                                goalTextView.setText(task);
                                hintTextView.setText(hints);

                                // 儲存目標到 SharedPreferences
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GoalPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("currentGoal", task);
                                editor.putString("currentHint", hints);
                                editor.apply();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Request Error: " + e.getMessage());
            }
        }).start();
    }
}
