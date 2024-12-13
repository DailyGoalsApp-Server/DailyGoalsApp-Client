package com.example.dailygoalsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class GoalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "daily_goals.db";
    private static final int DATABASE_VERSION = 2; // 確保資料庫升級
    private static final String TABLE_NAME = "goals";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_GOAL = "goal";

    public GoalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GOAL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 插入完成的目標
    public void insertGoal(String goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL, goal);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 獲取所有完成的目標
    public ArrayList<String> getCompletedGoals() {
        ArrayList<String> completedGoals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT goal FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                completedGoals.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return completedGoals;
    }

    public void deleteAllCompletedGoals() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
