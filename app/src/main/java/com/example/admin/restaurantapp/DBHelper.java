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
    static final int DB_VERSION = 9;
    static final String
            NO = "no",
            LOCATION_LAT = "lat",
            LOCATION_LNG = "lng",
            THUMB = "thumb",
            NAME = "name",
            PHOTOS = "photos",
            PLACE_ID = "place_id",
            RATING = "rating",
            PRICE_LEVEL = "price_level",
            FORMATTED_ADDRESS = "formatted_address",
            OPENING_HOURS = "opening_hours",
            INTERNATIONAL_PHONE_NUMBER = "international_phone_number",
            REVIEWS = "reviews",
            REVIEW_AUTHOR_NAME = "author_name",
            REVIEW_TEXT = "text",
            REVIEW_TIME = "time",
            URL = "url",
            WEBSITE = "website"
            ;

    DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create table
        sqLiteDatabase.execSQL(
            "create table " + TABLE_NAME +
                " (" +
                    NO + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    PLACE_ID + " TEXT ," +
                    NAME     + " TEXT ," +
                    THUMB    + " TEXT ," +
                    RATING   + " TEXT ," +
                    REVIEWS  + " TEXT ," +
                    PRICE_LEVEL  + " TEXT ," +
                    FORMATTED_ADDRESS  + " TEXT ," +
                    OPENING_HOURS  + " TEXT ," +
                    LOCATION_LAT  + " TEXT ," +
                    LOCATION_LNG  + " TEXT ," +
//                        REVIEW_AUTHOR_NAME + " TEXT ," +
//                        REVIEW_TEXT + " TEXT ," +
//                        REVIEW_TIME + " TEXT ," +
                    INTERNATIONAL_PHONE_NUMBER + " TEXT ," +
                    URL + " TEXT ," +
                    WEBSITE + " TEXT" +
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

        values.put(PLACE_ID, data.get(PLACE_ID));
        values.put(NAME,     data.get(NAME));
        values.put(THUMB,    data.get(THUMB));
        values.put(RATING,   data.get(RATING));

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