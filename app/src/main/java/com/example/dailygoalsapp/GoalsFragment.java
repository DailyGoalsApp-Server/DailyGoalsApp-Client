package com.example.dailygoalsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Random;

public class GoalsFragment extends Fragment {

    private TextView goalTextView;
    private Button changeGoalButton, completeGoalButton;
    private GoalDatabaseHelper dbHelper;
    private ArrayList<String> goalsList;
    private Random random;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        goalTextView = view.findViewById(R.id.goalTextView);
        changeGoalButton = view.findViewById(R.id.changeGoalButton);
        completeGoalButton = view.findViewById(R.id.completeGoalButton);
        dbHelper = new GoalDatabaseHelper(getContext());
        random = new Random();

        // 固定挑戰清單
        goalsList = new ArrayList<>();
        goalsList.add("跑步一公里");
        goalsList.add("喝300毫升水");
        goalsList.add("走路100步");
        goalsList.add("仰臥起坐15下");
        goalsList.add("開合跳20下");
        goalsList.add("深蹲10下");
        goalsList.add("喝水100毫升");
        goalsList.add("開合跳10下");
        goalsList.add("伏地挺身5下");

        // 初始化隨機目標
        showRandomGoal();

        // 換一個目標按鈕
        changeGoalButton.setOnClickListener(v -> showRandomGoal());

        // 完成目標按鈕
        completeGoalButton.setOnClickListener(v -> {
            String completedGoal = goalTextView.getText().toString();
            dbHelper.insertGoal(completedGoal);
            goalTextView.setText("");
            // 你可以在此添加動畫效果顯示完成的卡片

            showRandomGoal();
        });

        return view;
    }

    // 隨機顯示目標
    private void showRandomGoal() {
        int index = random.nextInt(goalsList.size());
        goalTextView.setText(goalsList.get(index));
    }
}
