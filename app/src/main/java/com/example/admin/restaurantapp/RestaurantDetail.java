package com.example.admin.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantDetail extends AppCompatActivity {

    TextView textView_restaurantDetail;
    ImageView imageView_restaurantMainVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize variables
        int restaurantId = 0;

        // Get intent
        Intent intent = getIntent();
        if (intent != null) {
            restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, 0);

        } else {
            Log.d("Debug", "getExtra: NG");
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        // Initialize each object
        //textView_restaurantName   = (TextView) findViewById(R.id.textView_restaurantName);
        textView_restaurantDetail = (TextView) findViewById(R.id.textView_restaurantDetail);
        imageView_restaurantMainVisual = (ImageView) findViewById(R.id.imageView_restaurantMainVisual);

        // Set each object
        setTitle(getResources().getStringArray(R.array.restaurants)[restaurantId]);
        textView_restaurantDetail.setText(getResources().getStringArray(R.array.details)[restaurantId]);
        int[] mainVisuals = {
                R.drawable.lg_osteriasaviovolpe,
                R.drawable.lg_guuotokomaegastown,
                R.drawable.lg_tuccraftkitchen,
                R.drawable.lg_ancorawaterfrontdining,
                R.drawable.lg_thekegsteakhouse,
                R.drawable.lg_nightingale,
                R.drawable.lg_osteriasaviovolpe,
                R.drawable.lg_guuotokomaegastown,
                R.drawable.lg_tuccraftkitchen,
                R.drawable.lg_ancorawaterfrontdining,
                R.drawable.lg_thekegsteakhouse,
                R.drawable.lg_nightingale
        };
        imageView_restaurantMainVisual.setImageResource(mainVisuals[restaurantId]);
        //textView_restaurantDetail.setText(getResources().getStringArray(R.array.details)[restaurantId]);
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
