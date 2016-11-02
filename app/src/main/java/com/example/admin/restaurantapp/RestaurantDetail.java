package com.example.admin.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantDetail extends AppCompatActivity {

    TextView textView_restaurantName;
    TextView textView_restaurantDetail;
    ImageView imageView_restaurantMainVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);

        Intent intent = getIntent();

        if (intent != null) {
            int restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, 0);
            String restaurantName = intent.getStringExtra(RestaurantList.EXTRA_RESTAURANT_NAME);
            Log.d("Debug", "getExtra(id): " + restaurantId);
            Log.d("Debug", "getExtra(name): " + restaurantName);

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

        textView_restaurantName   = (TextView) findViewById(R.id.textView_restaurantName);
        textView_restaurantDetail = (TextView) findViewById(R.id.textView_restaurantDetail);
        imageView_restaurantMainVisual = (ImageView) findViewById(R.id.imageView_restaurantMainVisual);
    }
}
