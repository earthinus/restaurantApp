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

    public int restaurantId;
    private ArrayList<Book> bookings = new ArrayList<>();
    private RecyclerView recyclerView;
    public static final String PREFERENCE_BOOK_KEY      = "rest-id";

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

        DBHelper dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor_booking;
        Cursor cursor_restaurant;
        try {
            cursor_booking = dbHelper.getAllRecords(DBHelper.TABLE_NAME_BOOKING);

            // If there is a booking data
            if (cursor_booking.getCount() > 0) {
                // Debug
                System.out.println("Count: " + cursor_booking.getCount());

                while (cursor_booking.moveToNext()) {

                    // Define restaurantId for get specific restaurant's data from restaurant table

                    String restaurantId = cursor_booking.getString(7);

                    // Get data (data, people) from booking table
                    Book booking = new Book();
                    booking.setBooking_date(cursor_booking.getString(1));
                    booking.setBooking_time(cursor_booking.getString(2));
                    booking.setBooking_name(cursor_booking.getString(3));
                    booking.setBooking_email(cursor_booking.getString(4));
                    booking.setBooking_people(Integer.valueOf(cursor_booking.getString(5)));
                    booking.setRestaurantId(Integer.valueOf(restaurantId));

                    /*
                    * -------------------------------------------------------------------
                    * Read 'restaurant' table by using restaurantId
                    * -------------------------------------------------------------------
                    */

                    try {

                        // Get data (name, icon, placeId) from restaurants table
                        cursor_restaurant = db.query(
                                DBHelper.TABLE_NAME_RESTAURANT, // Table name
                                null,                           // columns
                                DBHelper.NO + " = ?" ,          // Selection
                                new String[]{restaurantId},     // SelectionArgs
                                null,                           // groupBy
                                null,                           // Having
                                null                            // orderBy
                        );

                        cursor_restaurant.moveToFirst();

                        booking.setPlace_id(cursor_restaurant.getString(1));
                        booking.setName(cursor_restaurant.getString(2));
                        booking.setIcon(cursor_restaurant.getString(3));

                        cursor_restaurant.close();

                    } catch (Exception e) {
                        Log.d("Debug", "Catch error: " + e.toString());

                    }

                    bookings.add(booking);
                }

                /*
                * -------------------------------------------------------------------
                * Show recycler view
                * -------------------------------------------------------------------
                */

                // Adapter
                RecyclerView.Adapter adapter = new BookAdapter(this, bookings);

                // RecyclerView
                recyclerView = (RecyclerView) findViewById(R.id.bookList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);

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