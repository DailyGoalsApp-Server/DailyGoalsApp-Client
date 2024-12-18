package com.example.dailygoalsapp;

public class UserProfile {
    private String height;
    private String weight;
    private String gender;
    private String exerciseFrequency;
    private String exerciseIntensity;
    private String ageRange;

    // 預設建構子
    public UserProfile() {}

    // 帶參數的建構子
    public UserProfile(String height, String weight, String gender, String exerciseFrequency, String exerciseIntensity,String ageRange) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.exerciseFrequency = exerciseFrequency;
        this.exerciseIntensity = exerciseIntensity;
        this.ageRange = ageRange;
    }

    // Getter 和 Setter 方法
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(String exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getExerciseIntensity() {
        return exerciseIntensity;
    }

    public void setExerciseIntensity(String exerciseIntensity) {
        this.exerciseIntensity = exerciseIntensity;
    }



    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getHeightRange() {
        return height;
    }


    public String getWeightRange() {
        return weight;
    }

    public void setWeightRange(String weightRange) {
        this.weight = weight;
    }
}
