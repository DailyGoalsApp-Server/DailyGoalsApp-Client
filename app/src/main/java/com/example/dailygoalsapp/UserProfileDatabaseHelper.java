package com.example.dailygoalsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserProfileDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_profile.db";
    private static final int DATABASE_VERSION = 3; // 更新資料庫版本
    private static final String TABLE_NAME = "user_profile";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_EXERCISE_INTENSITY = "exercise_intensity"; // 新增運動強度欄位
    private static final String COLUMN_AGE = "age"; // 新增年齡欄位

    public UserProfileDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HEIGHT + " TEXT, " +
                COLUMN_WEIGHT + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_EXERCISE_INTENSITY + " TEXT, " +
                COLUMN_AGE + " INTEGER)"; // 創建表時新增年齡欄位
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_EXERCISE_INTENSITY + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_AGE + " INTEGER");
        }
    }

    // 插入使用者資料
    public void insertUserProfile(String height, String weight, String gender, String exerciseIntensity, String ageRange) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_EXERCISE_INTENSITY, exerciseIntensity);
        values.put(COLUMN_AGE, ageRange); // 插入年齡資料
        db.insert(TABLE_NAME, null, values);
    }

    // 獲取用戶資料
    public UserProfile getUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // 使用 getColumnIndex 確保欄位存在，否則返回錯誤
            int heightIndex = cursor.getColumnIndex(COLUMN_HEIGHT);
            int weightIndex = cursor.getColumnIndex(COLUMN_WEIGHT);
            int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);
            int exerciseIntensityIndex = cursor.getColumnIndex(COLUMN_EXERCISE_INTENSITY);
            int ageIndex = cursor.getColumnIndex(COLUMN_AGE);

            if (heightIndex == -1 || weightIndex == -1 || genderIndex == -1  || exerciseIntensityIndex == -1 || ageIndex == -1) {
                cursor.close();
                throw new IllegalArgumentException("資料表中缺少必要的欄位");
            }

            String height = cursor.getString(heightIndex);
            String weight = cursor.getString(weightIndex);
            String gender = cursor.getString(genderIndex);
            String exerciseIntensity = cursor.getString(exerciseIntensityIndex);
            String ageRange = cursor.getString(ageIndex);
            cursor.close();

            return new UserProfile(height, weight, gender, exerciseIntensity, ageRange);
        }

        cursor.close();
        return null; // 若沒有資料則返回 null
    }

    // 檢查用戶資料是否存在
    public boolean isUserProfileExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM user_profile");
        db.close();
    }
}
