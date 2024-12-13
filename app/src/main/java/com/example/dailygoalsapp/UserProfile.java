package com.example.dailygoalsapp;

public class UserProfile {
    private String height;
    private String weight;
    private String gender;
    private String exerciseFrequency;

    public UserProfile(String height, String weight, String gender, String exerciseFrequency) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getGender() {
        return gender;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }
}