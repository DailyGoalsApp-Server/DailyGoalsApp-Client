package com.example.dailygoalsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CompletedGoalsAdapter extends RecyclerView.Adapter<CompletedGoalsAdapter.ViewHolder> {

    private ArrayList<String> completedGoals;

    public CompletedGoalsAdapter(ArrayList<String> completedGoals) {
        this.completedGoals = completedGoals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String goal = completedGoals.get(position);
        holder.goalTextView.setText(goal);
    }

    @Override
    public int getItemCount() {
        return completedGoals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView goalTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            goalTextView = itemView.findViewById(R.id.goalTextView);
        }
    }
}
