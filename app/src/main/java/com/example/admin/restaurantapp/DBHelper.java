package com.example.admin.restaurantapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME    = "Restaurant";
    static final String TABLE_NAME = "restaurant";
    static final int DB_VERSION = 8;

    DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // TODO : Delete this later
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);

        // Create table
        sqLiteDatabase.execSQL(
            "create table " + TABLE_NAME +
                " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "PLACE_ID TEXT," +
                    "NAME TEXT," +
                    "THUMB TEXT," +
                    "RATING TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);

        // Recreate
        onCreate(sqLiteDatabase);
    }

    boolean insertRecord(HashMap<String, String> data) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("PLACE_ID", data.get("PLACE_ID"));
        values.put("NAME",     data.get("NAME"));
        values.put("THUMB",    data.get("THUMB"));
        values.put("RATING",   data.get("RATING"));

        long result = db.insert(TABLE_NAME, null, values);

        if (result != -1) {
            Log.d("Debug", "insertRecord: successful");
            return true;

        } else {
            Log.d("Debug", "insertRecord: failed");
            return false;
        }
    }

    Cursor getAllRecords() {
        return this.getWritableDatabase().rawQuery("select * from " + TABLE_NAME, null);
    }

    public Cursor getSpecificRecord(int id) {
        return this.getWritableDatabase().rawQuery("select * from " + TABLE_NAME + " where " + TABLE_NAME + ".id=?", new String[]{id + ""});
    }

}