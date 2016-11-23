package com.example.admin.restaurantapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

public class BookList extends AppCompatActivity {

    private ArrayList<Book> bookings = new ArrayList<>();

    public int restaurantId;
    public static final String PREFERENCE_BOOK_FILENAME = "Book-list";
    public static final String PREFERENCE_BOOK_KEY      = "rest-id";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    DBHelper dbHelper;
    SQLiteDatabase db;
    HashMap<String, String > hashMap_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();

        /*
        * -------------------------------------------------------------------
        * If the access came from booking button, insert the booking
        * -------------------------------------------------------------------
        */

        if (intent != null) {
            // TODO : switch from static values to dynamic values
            String booking_time = "2016/12/01 8:00 PM";
            int booking_people = 3;
//            booking_time = String.valueOf(intent.getIntExtra(DBHelper.BOOKING_TIME, -1));
//            booking_people = String.valueOf(intent.getIntExtra(DBHelper.BOOKING_PEOPLE, -1));
            int restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, -1);
            //getSharedPreferences(PREFERENCE_BOOK_FILENAME, MODE_PRIVATE).edit().putInt(PREFERENCE_BOOK_KEY + restaurantId, restaurantId).apply();

            /*
            * -------------------------------------------------------------------
            * Insert a booking to Database
            * -------------------------------------------------------------------
            */

            hashMap_booking = new HashMap<>();

            // Set keys & values to hashMap
            hashMap_booking.put(DBHelper.BOOKING_TIME, booking_time);
            hashMap_booking.put(DBHelper.BOOKING_PEOPLE, String.valueOf(booking_people));
            hashMap_booking.put(DBHelper.RESTAURANT_NO, String.valueOf(restaurantId));

            // insert the hashMap to the booking table
            dbHelper.insertRecord(DBHelper.TABLE_NAME_BOOKING, hashMap_booking);
        }

        // Initialize RecyclerView of restaurantList
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.bookList);
        recyclerView.setLayoutManager(layoutManager);

        /*
        * -------------------------------------------------------------------
        * Read 'booking' table
        * -------------------------------------------------------------------
        */

        dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        db = dbHelper.getReadableDatabase();

        Cursor cursor_booking;
        Cursor cursor_restaurant;
        try {
            cursor_booking = db.query(
                    DBHelper.TABLE_NAME_BOOKING,    // Table name
                    null,                           // columns
                    null,                           // Selection
                    null,                           // SelectionArgs
                    null,                           // groupBy
                    null,                           // Having
                    null                            // orderBy
            );

            // If there is a booking data
            if (cursor_booking.getCount() > 0) {

                /*
                * -------------------------------------------------------------------
                * Read 'restaurant' table by using restaurantId
                * -------------------------------------------------------------------
                */
                try {

                    // Move to first of booking
                    boolean next_booking = cursor_booking.moveToFirst();

                    while (next_booking) {

                        // Get data (data, people) from booking table
                        Book booking = new Book();
                        booking.setDate(cursor_booking.getString(1));
                        booking.setPeople(Integer.valueOf(cursor_booking.getString(2)));

                        // Define restaurantId for get specific restaurant's data from restaurant table
                        String restaurantId = cursor_booking.getString(3);

                        booking.setRestaurantId(Integer.valueOf(restaurantId));

                        // Get data (name, icon, placeId) from restaurants table
                        cursor_restaurant = db.query(
                                DBHelper.TABLE_NAME_RESTAURANT, // Table name
                                null,                           // columns
                                DBHelper.PLACE_ID + " = ?" ,    // Selection
                                new String[]{restaurantId},     // SelectionArgs
                                null,                           // groupBy
                                null,                           // Having
                                null                            // orderBy
                        );

                        booking.setName(cursor_restaurant.getString(2));    // Name
                        booking.setIcon(cursor_restaurant.getString(3));       // Thumb


                        // Define place_id for ClickListener
                        String place_id = cursor_restaurant.getString(1);      // place_id

                        bookings.add(booking);

                        cursor_restaurant.close();

                        next_booking = cursor_booking.moveToNext();
                    }

                    // Initialize adapter
                    adapter = new BookAdapter(getApplicationContext(), bookings);

                    // TODO : set EmptyView

                    // Set adapter to ListView
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    Log.d("Debug", "Catch error: " + e.toString());
                }
            }
            cursor_booking.close();

        } catch (Exception e) {
            Log.d("Debug", "Catch error: " + e.toString());

        } finally {
            db.close();
        }
    }

    // Set function of backButton on ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}