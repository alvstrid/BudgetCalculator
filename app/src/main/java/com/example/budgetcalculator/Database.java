package com.example.budgetcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.TextView;

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

        String[] categories2 = {"Food", "Transportation", "Household", "Health", "Clothes", "Other"};
        for(int i = 0; i < categories2.length; i++) {
            ContentValues values = new ContentValues();
            values.put(CATEGORIES_COL2, categories2[i]);
            long result = db.insert(TABLE_SUMS, null, values);
        }
    }

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

        String query  = " UPDATE " + TABLE_SUMS + " SET " +  CATEGORIES_COL3 + " = " + CATEGORIES_COL3 + "+" + amount + " WHERE CATEGORIES = '" + category + "'";

        Log.d(TAG, "Summing the column query: " + query);

        db.execSQL(query);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItem(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES +
                " WHERE " + EXPENSES_COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSum(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM FROM " + TABLE_SUMS;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        return data;
    }

    public String getSum2(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + CATEGORIES_COL3 + ") from " + TABLE_SUMS + ";", null);
        c.moveToFirst();
        return c.getString(0);

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
    public void updateName(String newName,String newAmount,String newCategory, int id, String oldName, String oldAmount, String oldCategory){
        SQLiteDatabase db = this.getWritableDatabase();

        String query  = " UPDATE " + TABLE_SUMS + " SET " +  CATEGORIES_COL3 + " = " + CATEGORIES_COL3 + "-" + oldAmount + "+" + newAmount + " WHERE CATEGORIES = '" + oldCategory + "'";
        Log.d(TAG, "Updating the column query: " + query);
        db.execSQL(query);

        query = "UPDATE " + TABLE_EXPENSES + " SET " + EXPENSES_COL2 + " = '" + newName +"',"
                + EXPENSES_COL3 + " = '" + newAmount + "',"
                + EXPENSES_COL5 + " = '" + newCategory
                + "' WHERE " + EXPENSES_COL1 + " = '" + id + "'"
                + " AND " + EXPENSES_COL2 + " = '" + oldName + "'";
        db.execSQL(query);

    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name, String amount, String category){
        SQLiteDatabase db = this.getWritableDatabase();

        String query  = " UPDATE " + TABLE_SUMS + " SET " +  CATEGORIES_COL3 + " = " + CATEGORIES_COL3 + "-" + amount + " WHERE CATEGORIES = '" + category + "'";

        Log.d(TAG, "Substracting the column query: " + query);
        db.execSQL(query);

        query = "DELETE FROM " + TABLE_EXPENSES + " WHERE "
                + EXPENSES_COL1 + " = '" + id + "'" +
                " AND " + EXPENSES_COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);

    }

}


