package com.example.chenxiyu.datepicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class MyDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "schedule";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_DATE = "day";
    public static final String CONTACTS_COLUMN_TIME = "time_start";
    public static final String CONTACTS_COLUMN_DURATION = "duration";
    public static final String CONTACTS_COLUMN_EVENT = "event";

    //private HashMap hp;

    public MyDB(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table schedule " +
                        "(id integer primary key, name text,day text,time_start text, duration integer,event text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertEvent(String name, String day, String time, Integer duration, String event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("day", day);
        contentValues.put("time_start", time);
        contentValues.put("duration", duration);
        contentValues.put("event", event);
        db.insert("schedule", null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedule", null );
        return res;
    }

    public Cursor getByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedule where name="+name+"", null );
        return res;
    }

    public Cursor getById(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedule where id="+id+"", null );
        return res;
    }

    public boolean updateEvent(Integer _id,String name, String day, String time, Integer duration, String event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("day", day);
        contentValues.put("time_start", time);
        contentValues.put("duration", duration);
        contentValues.put("event", event);
        db.update("schedule", contentValues, "id = ?", new String[] {Integer.toString(_id)});
        return true;
    }

    public Integer deleteEvent(Integer _id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("schedule","id = ? ", new String[] { Integer.toString(_id) });
    }
}
