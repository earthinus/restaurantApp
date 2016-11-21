package com.example.admin.restaurantapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

class DBHelper extends SQLiteOpenHelper {

    static final String
            DB_NAME = "Restaurants",
            TABLE_NAME_RESTAURANT = "restaurants",
            TABLE_NAME_REVIEW = "reviews";
    static final int DB_VERSION = 16;
    static final String
            NO = "no",
            RESTAURANT_NO = "restaurant_no",
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
            REVIEW_AUTHOR_URL = "author_url",
            REVIEW_PROFILE_PHOTO_URL = "profile_photo_url",
            REVIEW_RATING = RATING,
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
            "create table " + TABLE_NAME_RESTAURANT +
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
                    INTERNATIONAL_PHONE_NUMBER + " TEXT ," +
                    URL + " TEXT ," +
                    WEBSITE + " TEXT" +
                ")"
        );

        sqLiteDatabase.execSQL(
            "create table " + TABLE_NAME_REVIEW +
                " (" +
                    NO + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    REVIEW_AUTHOR_NAME + " TEXT ," +
                    REVIEW_AUTHOR_URL + " TEXT ," +
                    REVIEW_PROFILE_PHOTO_URL + " TEXT ," +
                    REVIEW_RATING + " TEXT ," +
                    REVIEW_TEXT + " TEXT ," +
                    REVIEW_TIME + " TEXT ," +
                    RESTAURANT_NO + " TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_RESTAURANT);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_REVIEW);

        // Recreate
        onCreate(sqLiteDatabase);
    }

    public void deleteDatabase() {

        getWritableDatabase().execSQL("drop table if exists " + TABLE_NAME_RESTAURANT);
        getWritableDatabase().execSQL("drop table if exists " + TABLE_NAME_REVIEW);
        Log.d("Debug", "Database was deleted.");

    }

    boolean insertRecord(String target_table, HashMap<String, String> data) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        long result = -1; // Default value

        switch (target_table) {

            // Insert to restaurants table
            case TABLE_NAME_RESTAURANT :
                values.put(PLACE_ID, data.get(PLACE_ID));
                values.put(NAME,     data.get(NAME));
                values.put(THUMB,    data.get(THUMB));
                values.put(RATING,   data.get(RATING));

                result = db.insert(TABLE_NAME_RESTAURANT, null, values);
                break;

            // Insert to reviews table
            case TABLE_NAME_REVIEW :
                values.put(REVIEW_TEXT,                 data.get(REVIEW_TEXT));
                values.put(REVIEW_AUTHOR_NAME,          data.get(REVIEW_AUTHOR_NAME));
                values.put(REVIEW_AUTHOR_URL,           data.get(REVIEW_AUTHOR_URL));
                values.put(REVIEW_PROFILE_PHOTO_URL,    data.get(REVIEW_PROFILE_PHOTO_URL));
                values.put(REVIEW_RATING,               data.get(REVIEW_RATING));
                values.put(REVIEW_TIME,                 data.get(REVIEW_TIME));
                values.put(RESTAURANT_NO,               data.get(RESTAURANT_NO));

                result = db.insert(TABLE_NAME_REVIEW, null, values);
                break;

            default:
                break;
        }

        if (result != -1) {
            Log.d("Debug", "insertRecord: successful");
            return true;

        } else {
            Log.d("Debug", "insertRecord: failed");
            return false;
        }
    }

    Cursor getAllRecords() {
        return this.getWritableDatabase().rawQuery("select * from " + TABLE_NAME_RESTAURANT, null);
    }
}