package com.example.dailygoalsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserProfileDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_profile.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "user_profile";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_EXERCISE_FREQUENCY = "exercise_frequency";

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
                COLUMN_EXERCISE_FREQUENCY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 插入使用者資料
    public void insertUserProfile(String height, String weight, String gender, String exerciseFrequency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_EXERCISE_FREQUENCY, exerciseFrequency);
        db.insert(TABLE_NAME, null, values);
    }

    // 獲取用戶資料
    public UserProfile getUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String height = cursor.getString(cursor.getColumnIndex(COLUMN_HEIGHT));
            String weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT));
            String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
            String exerciseFrequency = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_FREQUENCY));
            cursor.close();

            return new UserProfile(height, weight, gender, exerciseFrequency);
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