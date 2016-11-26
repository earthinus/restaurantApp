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

public class FavoriteList extends AppCompatActivity {

    private ArrayList<Favorite> favorites = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
        * -------------------------------------------------------------------
        * Read 'favorite' table
        * -------------------------------------------------------------------
        */

        DBHelper dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor_favorite;
        Cursor cursor_restaurant;
        try {
            cursor_favorite = dbHelper.getAllRecords(DBHelper.TABLE_NAME_FAVORITE);

            if (cursor_favorite.getCount() > 0) {
                // Debug
                System.out.println("Count: " + cursor_favorite.getCount());

                while (cursor_favorite.moveToNext()) {

                    String restaurantId = cursor_favorite.getString(2);

                    Favorite favorite = new Favorite();

                    /*
                    * -------------------------------------------------------------------
                    * Read 'restaurant' table by using restaurantId
                    * -------------------------------------------------------------------
                    */

                    try {
                        // Get specific record from 'restaurants' table by using restaurantId
                        cursor_restaurant = dbHelper.getSpecificRecords(DBHelper.TABLE_NAME_RESTAURANT, DBHelper.NO, new String[]{restaurantId});

                        cursor_restaurant.moveToFirst();

                        favorite.setPlace_id(cursor_restaurant.getString(1));
                        favorite.setName(cursor_restaurant.getString(2));
                        favorite.setIcon(cursor_restaurant.getString(3));

                        cursor_restaurant.close();

                    } catch (Exception e) {
                        Log.d("Debug", "Catch error: " + e.toString());
                    }

                    favorites.add(favorite);
                }

                /*
                * -------------------------------------------------------------------
                * Show recycler view
                * -------------------------------------------------------------------
                */

                RecyclerView.Adapter adapter = new FavoriteAdapter(this, favorites);

                recyclerView = (RecyclerView) findViewById(R.id.favList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);

            // No booking data
            } else {
                Toast.makeText(this, "No favorite data.", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            }
            cursor_favorite.close();

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