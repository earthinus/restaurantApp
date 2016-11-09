package com.example.admin.restaurantapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

public class BookList extends AppCompatActivity {

    private ArrayList<Book> books = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    public int restaurantId;
    public static final String PREFERENCE_BOOK_FILENAME = "Book-list";
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

        // Get intent
        final Intent intent = getIntent();
        if (intent != null) {
            restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, -1);
            getSharedPreferences(PREFERENCE_BOOK_FILENAME, MODE_PRIVATE).edit().putInt(PREFERENCE_BOOK_KEY + restaurantId, restaurantId).apply();

        } else {
            System.out.println("getExtra: NG");
        }

        // Initialize RecyclerView of restaurantList
        recyclerView = (RecyclerView) findViewById(R.id.bookList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Data-sets
        TypedArray icons = getResources().obtainTypedArray(R.array.icons);
        icons.recycle(); // recycle() : to release the bitmap pixel's data in native memory to avoid 'OutOfMemoryError'
        String[]   names = getResources().getStringArray(R.array.restaurants);
        String[]   dates = {"2016/12/01 8:00 PM"};

        // Set each restaurant's data to ArrayList
        for (int i = 0; i < names.length; i++) {
            if (getSharedPreferences(PREFERENCE_BOOK_FILENAME, MODE_PRIVATE).getInt(PREFERENCE_BOOK_KEY + i, -1) != -1) {
                Book book = new Book();
                book.setIcon(icons.getDrawable(i));
                book.setName(names[i]);
                book.setDate(dates[0]);
                books.add(book);
            }
        }

        // Initialize adapter
        adapter = new BookAdapter(getApplicationContext(), books);

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