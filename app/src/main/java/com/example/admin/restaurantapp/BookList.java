package com.example.admin.restaurantapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class BookList extends AppCompatActivity {

    private ArrayList<Book> bookings = new ArrayList<>();

    public int restaurantId;
    public static final String PREFERENCE_BOOK_KEY      = "rest-id";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
        * -------------------------------------------------------------------
        * Read 'booking' table
        * -------------------------------------------------------------------
        */

        recyclerView = (RecyclerView) findViewById(R.id.bookList);

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
                        booking.setRestaurantId(Integer.valueOf(cursor_booking.getString(3)));

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

                        booking.setName(cursor_restaurant.getString(2));
                        booking.setIcon(cursor_restaurant.getString(3));

                        bookings.add(booking);

                        cursor_restaurant.close();

                        next_booking = cursor_booking.moveToNext();
                    }

                    // Initialize adapter
                    adapter = new BookAdapter(getApplicationContext(), bookings);

                    // Set adapter to ListView
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    Log.d("Debug", "Catch error: " + e.toString());
                }

                /*
                * -------------------------------------------------------------------
                * Show recycler view
                * -------------------------------------------------------------------
                */

                // Initialize RecyclerView of restaurantList
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

            // No booking data
            } else {
                Toast.makeText(this, "No booking data.", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
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