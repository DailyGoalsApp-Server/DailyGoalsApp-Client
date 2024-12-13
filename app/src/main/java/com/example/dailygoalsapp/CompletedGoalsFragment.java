package com.example.dailygoalsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CompletedGoalsFragment extends Fragment {

    private GoalDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_goals, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.completedGoalsRecyclerView);

        dbHelper = new GoalDatabaseHelper(getContext());
        ArrayList<String> completedGoals = dbHelper.getCompletedGoals();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CompletedGoalsAdapter adapter = new CompletedGoalsAdapter(completedGoals);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
