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
import android.util.Log;

public class MyDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String SCHEDULE_TABLE_NAME = "schedule";
    public static final String SCHEDULE_COLUMN_ID = "id";
    public static final String SCHEDULE_COLUMN_NAME = "name";
    public static final String SCHEDULE_COLUMN_DATE = "day";
    public static final String SCHEDULE_COLUMN_TIME = "time_start";
    public static final String SCHEDULE_COLUMN_DURATION = "duration";
    public static final String SCHEDULE_COLUMN_EVENT = "event";


    public static final String ACTIVITY_TABLE_NAME = "activity";
    public static final String ACTIVITY_COLUMN_ID = "id";
    public static final String ACTIVITY_COLUMN_EVENT = "event";
    public static final String ACTIVITY_COLUMN_ADDRESS = "address";
    public static final String ACTIVITY_COLUMN_ZIP = "zip";

    //private HashMap hp;

    public MyDB(Context context) {
        super(context, DATABASE_NAME , null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table schedule " +
                        "(id integer primary key, name text not null,day text,time_start text, duration integer,event text)"
        );
        db.execSQL(
                "create table " +ACTIVITY_TABLE_NAME+
                        "(id integer primary key, event text,address text,zip integer)"
        );

        db.execSQL("CREATE INDEX time_start_index on schedule(time_start)");
        //db.execSQL();
        //db.execSQL(insertTrigger());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS schedule");
        db.execSQL("DROP TABLE IF EXISTS activity");
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

    public boolean insertAct(String event, String address, Integer zip){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("address", address);
        contentValues.put("zip", zip);
        db.insert(ACTIVITY_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedule", null );
        return res;
    }

    public Cursor getByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select schedule.event as event from schedule where name=? group by schedule.event order by count(schedule.event) desc limit 10", new String[]{name});
        return res;
    }

    public Cursor getById(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct * from schedule where id="+id+"", null );
        return res;
    }

    public Cursor getByEvent(String event){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select schedule.name as name from schedule where event=? group by schedule.name order by count(schedule.name) desc limit 10", new String[]{event});
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



    public Cursor getByZip(Integer zipcode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from activity where zipcode="+zipcode+"", null );
        return res;
    }

    public Cursor getLikeEvent(String s){
        SQLiteDatabase db = this.getReadableDatabase();
        s = "%" + s+"%";
        Cursor res = db.rawQuery( "select * from activity where address like ?", new String[]{s});
        return res;
    }

    public Cursor getAppox(String search_String){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = getData();
        if(isInteger(search_String,10)){
            //TODO zipcode search
            Cursor res_zip = db.rawQuery("select schedule.event, schedule.day, schedule.duration, activity.address " +
                    "from schedule, activity " +
                    "where schedule.event = activity.event and activity.zip=?", new String[]{search_String});
            Log.e("type: ", "zipcode");
            return res_zip;
        }
        else if (search_String.contains("St") || search_String.contains("Av") || search_String.contains("Street") ||
                search_String.contains("Avenue")){
            //TODO address
            Cursor res_add = db.rawQuery("select schedule.event, schedule.day, schedule.duration, activity.address " +
                    "from schedule, activity " +
                    "where schedule.event = activity.event and activity.address like ?", new String[]{"%"+search_String+"%"});
            Log.e("type: ", "address");
            return res_add;
        }else if(containMonth(search_String) > 0){
            String month = "/"+containMonth(search_String)+"/";
            Cursor res_dat = db.rawQuery("select schedule.event as event, schedule.day as date, schedule.duration as duration, activity.address " +
                    "from schedule, activity " +
                    "where schedule.event = activity.event and schedule.day like ?", new String[]{"%"+month+"%"});
            Log.e("type: ", "date");
            return res_dat;
        }



        return res;//change to res
    }

    private static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    private static int containMonth(String s){
        if(s.contains("January") || s.contains("Jan") || s.contains("/1/")){
            return 1;
        }else if(s.contains("February") || s.contains("Jan") || s.contains("/2/")){
            return 2;
        }else if(s.contains("March") || s.contains("mar") || s.contains("/3/")){
            return 3;
        }else if(s.contains("April") || s.contains("Apr") || s.contains("/4/")){
            return 4;
        }else if(s.contains("May") || s.contains("/5/")){
            return 5;
        }else if(s.contains("June") || s.contains("Jun") || s.contains("/6/")){
            return 6;
        }else if(s.contains("July") || s.contains("Jul") || s.contains("/7/")){
            return 7;
        }else if(s.contains("August") || s.contains("Aug") || s.contains("/8/")){
            return 8;
        }else if(s.contains("September") || s.contains("Sep") || s.contains("/9/")){
            return 9;
        }else if(s.contains("October") || s.contains("Oct") || s.contains("/10/")){
            return 10;
        }else if(s.contains("November") || s.contains("Nov") || s.contains("/11/")){
            return 11;
        }else if(s.contains("December") || s.contains("Dec") || s.contains("/12/")){
            return 12;
        }
        return 0;
    }
}
