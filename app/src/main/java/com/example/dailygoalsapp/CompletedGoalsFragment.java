package com.example.dailygoalsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CompletedGoalsFragment extends Fragment {

    private GoalDatabaseHelper dbHelper;
    private ImageView streakImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_goals, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.completedGoalsRecyclerView);
        streakImageView = view.findViewById(R.id.streakImageView);

        dbHelper = new GoalDatabaseHelper(getContext());
        ArrayList<String> completedGoals = dbHelper.getCompletedGoals();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CompletedGoalsAdapter adapter = new CompletedGoalsAdapter(completedGoals);
        recyclerView.setAdapter(adapter);

        int consecutiveDays = calculateConsecutiveDays(completedGoals);
        updateStreakImage(consecutiveDays);

        return view;
    }

    private int calculateConsecutiveDays(ArrayList<String> completedGoals) {
        int consecutiveDays = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long previousDayMillis = -1;

        for (String goal : completedGoals) {
            String[] parts = goal.split("\n");
            String dateString = parts[0].split(" ")[0]; // 提取日期部分
            try {
                Date date = dateFormat.parse(dateString);
                long dateMillis = date.getTime();

                if (previousDayMillis != -1) {
                    long difference = dateMillis - previousDayMillis;
                    if (difference == 86400000) { // 86400000 毫秒 = 1 天
                        consecutiveDays++;
                    } else if (difference > 86400000) {
                        consecutiveDays = 1; // 有缺失的天數，重置計數
                    }
                } else {
                    consecutiveDays = 1; // 初始化計數
                }

                previousDayMillis = dateMillis;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return consecutiveDays;
    }

    private void updateStreakImage(int consecutiveDays) {
        switch (consecutiveDays) {
            case 0:
                streakImageView.setBackgroundResource(R.drawable.checkin_day_0);
                break;
            case 1:
                streakImageView.setBackgroundResource(R.drawable.checkin_day_1);
                break;
            case 2:
                streakImageView.setBackgroundResource(R.drawable.checkin_day_2);
                break;
            case 3:
                streakImageView.setBackgroundResource(R.drawable.checkin_day_3);
                break;
            case 4:
                streakImageView.setBackgroundResource(R.drawable.checkin_day_4);
                break;
            default:
                streakImageView.setBackgroundResource(R.drawable.checkin_day_5);
                break;
        }
    }
}
