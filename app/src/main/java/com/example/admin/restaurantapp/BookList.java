package com.example.admin.restaurantapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class BookList extends AppCompatActivity {

    public int restaurantId = 0;
    public final String PREFERENCE_FILENAME = "Book-list";

    private ArrayList<Book> books = new ArrayList<>();
    Book book;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

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
            getSharedPreferences("Book-list", MODE_PRIVATE).edit().putInt("rest-id" + restaurantId, restaurantId).apply();
            //Log.d("Debug", "getExtra: " + restaurantId);

        } else {
            Log.d("Debug", "getExtra: NG");
        }

        // Initialize Recycler of restaurantList
        recyclerView = (RecyclerView) findViewById(R.id.bookList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Data-sets
        int[] icons = new int[]{
                R.drawable.icon_osteriasaviovolpe,
                R.drawable.icon_guuotokomaegastown,
                R.drawable.icon_tuccraftkitchen,
                R.drawable.icon_ancorawaterfrontdining,
                R.drawable.icon_thekegsteakhouse,
                R.drawable.icon_nightingale,
                R.drawable.icon_osteriasaviovolpe,
                R.drawable.icon_guuotokomaegastown,
                R.drawable.icon_tuccraftkitchen,
                R.drawable.icon_ancorawaterfrontdining,
                R.drawable.icon_thekegsteakhouse,
                R.drawable.icon_nightingale
        };
        String[] names = getResources().getStringArray(R.array.restaurants);
        String[] dates = {"2016/12/01 8:00 PM"};

        // Set each restaurant's data to ArrayList
        for (int i = 0; i < names.length; i++) {
            if (getSharedPreferences("Book-list", MODE_PRIVATE).getInt("rest-id" + i, -1) != -1) {
                book = new Book();
                book.setIcon(BitmapFactory.decodeResource(getResources(), icons[i]));
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

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}