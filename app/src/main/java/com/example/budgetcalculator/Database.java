package com.example.budgetcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.R;

public class Database extends SQLiteOpenHelper {

    private static final String TAG = "Database";
    public static final String DATABASE_NAME = "Expenses.db";
    private static final String TABLE_EXPENSES = "expenses_table";
    private static final String TABLE_SUMS = "sums_table";
    private static final String EXPENSES_COL1 = "ID";
    private static final String EXPENSES_COL2 = "EXPENSE";
    private static final String EXPENSES_COL3 = "AMOUNT";
    private static final String EXPENSES_COL4 = "DATE";
    private static final String EXPENSES_COL5 = "CATEGORY";
    private static final String CATEGORIES_COL2 = "CATEGORIES";
    private static final String CATEGORIES_COL3 = "SUM";

    SQLiteDatabase db = this.getWritableDatabase();

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db =  this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, EXPENSE TEXT, AMOUNT NUM, DATE TEXT, CATEGORY TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_SUMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORIES TEXT, SUM NUM DEFAULT 0)";
        db.execSQL(createTable);
        db.execSQL(createTable2);

        //NEEDS FIX!!!!!!!!!!!!!!!!!!!!!!!!
        String[] categories2 = {"Food", "Transportation", "Household", "Health", "Clothes", "Other"};
        for(int i = 0; i < categories2.length; i++) {
            ContentValues values = new ContentValues();
            values.put(CATEGORIES_COL2, categories2[i]);
            long result = db.insert(TABLE_SUMS, null, values);
        }
    }

   /* public boolean addCategory(String category) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORIES_COL2, category);
        long result = db.insert(TABLE_SUMS, null, values);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    } */

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public boolean addData(String expense,  String amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXPENSES_COL2, expense);
        contentValues.put(EXPENSES_COL3, amount);
        contentValues.put(EXPENSES_COL4, date);
        contentValues.put(EXPENSES_COL5, category);

        long result = db.insert(TABLE_EXPENSES, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public double calculateSum(String category) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(AMOUNT) FROM " + TABLE_EXPENSES + " WHERE CATEGORY like '%" + category + "%' " , null);
        c.moveToFirst();
        double i = c.getDouble(0);
        c.close();

        Cursor cursor = null;
        String sql ="SELECT CATEGORIES FROM "+TABLE_SUMS+" WHERE CATEGORIES="+category;
        cursor= db.rawQuery(sql,null);

        cursor.close();
        return i;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + EXPENSES_COL1 + " FROM " + TABLE_EXPENSES +
                " WHERE " + EXPENSES_COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_EXPENSES + " SET " + EXPENSES_COL2 +
                " = '" + newName + "' WHERE " + EXPENSES_COL1 + " = '" + id + "'" +
                " AND " + EXPENSES_COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_EXPENSES + " WHERE "
                + EXPENSES_COL1 + " = '" + id + "'" +
                " AND " + EXPENSES_COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);

    }


}


