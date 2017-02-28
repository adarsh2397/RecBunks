package com.example.adarshhonawad.recbunks;

/**
 * Created by Adarsh Honawad on 01-07-2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bunkrecords.db";
    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_BUNKS = "bunks";
    public static final String COLUMN_MAXBUNKS = "maxbunks";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_RECORDS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SUBJECT + " TEXT, " +
                COLUMN_BUNKS + " INTEGER, " +
                COLUMN_MAXBUNKS + " INTEGER" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    //ADD A NEW RECORD TO THE DATABASE
    public long addRecord(Records records){
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT,records.get_subjectName());
        values.put(COLUMN_BUNKS,records.get_noOfBunks());
        values.put(COLUMN_MAXBUNKS,records.get_maxBunks());
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_RECORDS,null,values);
        db.close();
        return id;
    }

    //DELETE A RECORD FROM THE DATABASE
    public void deleteRecord(int recordId){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_RECORDS + " WHERE " + COLUMN_ID + " = " + recordId;
        db.execSQL(query);
        db.close();
    }

    public Cursor getCursor(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor myCursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDS,null);

        myCursor.moveToFirst();
        return myCursor;
    }

    public void increaseBunks(int recordId){

        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_RECORDS + " SET " + COLUMN_BUNKS + " = " + COLUMN_BUNKS + " + 1 WHERE " +
                COLUMN_ID + " = " + recordId ;
        db.execSQL(query);
        db.close();
    }

    public void updateRecord(int recordId, String subjectName,int currBunks,int maxBunks){
        SQLiteDatabase db = getWritableDatabase();
        String query1 = "UPDATE " + TABLE_RECORDS + " SET " + COLUMN_SUBJECT + " = " + "\"" + subjectName + "\"" + " WHERE " +
                COLUMN_ID + " = " + recordId;
        String query2 = "UPDATE " + TABLE_RECORDS + " SET " + COLUMN_BUNKS + " = " + currBunks + " WHERE " +
                COLUMN_ID + " = " + recordId;
        String query3 = "UPDATE " + TABLE_RECORDS + " SET " + COLUMN_MAXBUNKS + " = " + maxBunks + " WHERE " +
                COLUMN_ID + " = " + recordId;

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.close();
    }

    public void deleteEverything(){
        SQLiteDatabase db = getWritableDatabase();

        String query = "DELETE FROM " + TABLE_RECORDS + " WHERE 1";
        db.execSQL(query);
        db.close();
    }

}
