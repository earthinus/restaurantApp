package com.example.admin.restaurantapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    public int restaurantId;
    public static final String PREFERENCE_FAVORITE_FILENAME = "Favorite-list";
    public static final String PREFERENCE_FAVORITE_KEY      = "rest-id";

    private ArrayList<Favorite> favorites = new ArrayList<>();
    Favorite favorite;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get intent
        final Intent intent = getIntent();
        if (intent != null) {
            restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, -1);
            getSharedPreferences(PREFERENCE_FAVORITE_FILENAME, MODE_PRIVATE).edit().putInt(PREFERENCE_FAVORITE_KEY + restaurantId, restaurantId).apply();

        } else {
            System.out.println("getExtra: NG");
        }

        // Initialize RecyclerView of restaurantList
        recyclerView = (RecyclerView) findViewById(R.id.favList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Data-sets
        TypedArray icons = getResources().obtainTypedArray(R.array.icons);
        icons.recycle(); // recycle() : to release the bitmap pixel's data in native memory to avoid 'OutOfMemoryError'
        String[]   names = getResources().getStringArray(R.array.restaurants);

        // Set each restaurant's data to ArrayList
        for (int i = 0; i < names.length; i++) {
            if (getSharedPreferences(PREFERENCE_FAVORITE_FILENAME, MODE_PRIVATE).getInt(PREFERENCE_FAVORITE_KEY + i, -1) != -1) {
                favorite = new Favorite();
                favorite.setIcon(icons.getDrawable(i));
                favorite.setName(names[i]);
                favorites.add(favorite);
            }
        }

        // Initialize adapter
        adapter = new FavoriteAdapter(getApplicationContext(), favorites);

        // TODO : set EmptyView

        // Set adapter to ListView
        recyclerView.setAdapter(adapter);
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