package com.example.dailygoalsapp;

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

public class GoalsFragment extends Fragment {

    private final OkHttpClient client = new OkHttpClient();
    private UserProfile userProfile;
    private TextView goalTextView;
    private Button changeGoalButton, completeGoalButton;
    private GoalDatabaseHelper dbHelper;
    private static final String TAG = "GoalsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 載入 Fragment 的佈局
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        goalTextView = view.findViewById(R.id.goalTextView);
        changeGoalButton = view.findViewById(R.id.changeGoalButton);
        completeGoalButton = view.findViewById(R.id.completeGoalButton);
        dbHelper = new GoalDatabaseHelper(getContext());

        // 取得 UserProfile
        UserProfileDatabaseHelper userProfileDbHelper = new UserProfileDatabaseHelper(getActivity());
        userProfile = userProfileDbHelper.getUserProfile();

        // 設置 changeGoalButton 按鈕的點擊事件
        changeGoalButton.setOnClickListener(v -> fetchUserTask());

        // 設置 completeGoalButton 按鈕的點擊事件
        completeGoalButton.setOnClickListener(v -> {
            String completedGoal = goalTextView.getText().toString();
            dbHelper.insertGoal(completedGoal);
            goalTextView.setText("");
            fetchUserTask();
        });

        // 在啟動時載入初始任務
        fetchUserTask();

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
                            String hint = jsonResponse.getString("hints");

                            // 更新 UI 或儲存回應資料
                            getActivity().runOnUiThread(() -> {
                                // 更新顯示任務的 TextView
                                goalTextView.setText(task);

                                // 可選：顯示提示
                                // TextView hintTextView = getView().findViewById(R.id.hintTextView);
                                // hintTextView.setText(hint);
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
