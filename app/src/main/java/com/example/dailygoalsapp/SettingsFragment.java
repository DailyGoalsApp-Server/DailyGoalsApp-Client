package com.example.dailygoalsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private UserProfileDatabaseHelper userProfileDbHelper;
    private GoalDatabaseHelper goalDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        userProfileDbHelper = new UserProfileDatabaseHelper(getContext());
        goalDbHelper = new GoalDatabaseHelper(getContext());

        Button viewProfileButton = view.findViewById(R.id.viewProfileButton);
        Button deleteProfileButton = view.findViewById(R.id.deleteProfileButton);
        Button deleteGoalsButton = view.findViewById(R.id.deleteGoalsButton);
        Button viewCreditsButton = view.findViewById(R.id.viewCreditsButton);

        viewProfileButton.setOnClickListener(v -> showProfile());
        deleteProfileButton.setOnClickListener(v -> confirmAndDeleteProfile());
        deleteGoalsButton.setOnClickListener(v -> confirmAndDeleteGoals());
        viewCreditsButton.setOnClickListener(v -> showCredits());

        return view;
    }

    private void showProfile() {
        UserProfile profile = userProfileDbHelper.getUserProfile();
        if (profile != null) {
            String message = "身高: " + profile.getHeightRange() +
                    "\n體重: " + profile.getWeightRange() +
                    "\n性別: " + profile.getGender() +
                    "\n運動頻率: " + profile.getExerciseIntensity();
            new AlertDialog.Builder(getContext())
                    .setTitle("基本資料")
                    .setMessage(message)
                    .setPositiveButton("確定", null)
                    .show();
        }
    }

    private void confirmAndDeleteProfile() {
        new AlertDialog.Builder(getContext())
                .setTitle("確認刪除")
                .setMessage("確定要刪除基本資料嗎？")
                .setPositiveButton("確定", (dialog, which) -> {
                    userProfileDbHelper.deleteAllData();
                    getActivity().finish(); // 關閉應用
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void confirmAndDeleteGoals() {
        new AlertDialog.Builder(getContext())
                .setTitle("確認刪除")
                .setMessage("確定要刪除所有已完成目標嗎？")
                .setPositiveButton("確定", (dialog, which) -> {
                    goalDbHelper.deleteAllCompletedGoals();
                    getActivity().finish(); // 關閉應用
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showCredits() {
        new AlertDialog.Builder(getContext())
                .setTitle("製作人員")
                .setMessage("陳佩琦：主程式設計\n吳芋嫻：後端程式設計\n許依茹：程式UI設計")
                .setPositiveButton("確定", null)
                .show();
    }
}
