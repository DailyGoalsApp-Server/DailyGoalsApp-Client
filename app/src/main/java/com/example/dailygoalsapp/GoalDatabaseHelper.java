package com.example.dailygoalsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GoalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "daily_goals.db";
    private static final int DATABASE_VERSION = 4; // 確保資料庫升級
    private static final String TABLE_NAME = "goals";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_GOAL = "goal";
    private static final String COLUMN_COMPLETION_TIME = "completion_time"; // 新增完成時間欄位
    private static final String COLUMN_COMPLETION_DATE = "completion_date"; // 新增完成日期欄位

    public GoalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GOAL + " TEXT, " +
                COLUMN_COMPLETION_TIME + " TEXT, " +
                COLUMN_COMPLETION_DATE + " TEXT)"; // 創建表時新增完成日期欄位
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_COMPLETION_DATE + " TEXT");
        }
    }

    // 插入完成的目標和完成時間及日期
    public void insertGoal(String goal, String completionTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL, goal);
        values.put(COLUMN_COMPLETION_TIME, completionTime);
        values.put(COLUMN_COMPLETION_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())); // 當前日期
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 獲取所有完成的目標
    public ArrayList<String> getCompletedGoals() {
        ArrayList<String> completedGoals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT goal, completion_time FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                completedGoals.add(cursor.getString(1) +"\n"+ cursor.getString(0) );
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return completedGoals;
    }

    // 獲取最新的完成日期
    public String getLastCompletionDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT completion_date FROM " + TABLE_NAME + " ORDER BY completion_date DESC LIMIT 1", null);
        String lastDate = "";
        if (cursor.moveToFirst()) {
            lastDate = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return lastDate;
    }

    // 獲取連續完成天數
    public int getStreakCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT completion_date FROM " + TABLE_NAME + " ORDER BY completion_date DESC", null);
        int streakCount = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date previousDate = null;
        while (cursor.moveToNext()) {
            try {
                Date currentDate = sdf.parse(cursor.getString(0));
                if (previousDate != null) {
                    long diff = previousDate.getTime() - currentDate.getTime();
                    if (diff <= 24 * 60 * 60 * 1000L) { // 如果是連續日期
                        streakCount++;
                    } else {
                        break;
                    }
                }
                previousDate = currentDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return streakCount;
    }

    public void deleteAllCompletedGoals() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
