package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //launch screen
        setContentView(R.layout.index);
        Handler handler = new Handler();
        handler.postDelayed(new jumpPage(), 3000);

        // TODO : 3. Show Restaurant Detail (Maki)
        // 1. Load array data by using restaurant's id
        // 2. Set the array to each object
        // 3. Set onClick function to "reservation" fab button
        //      Show toast "This restaurant was added to favorite"
        // 4. Set onClick function to "favorite" fab
        //      Change the color or icon of fab
        //      Set onClick function for removing from favorite list

        // TODO : 4. Show Reservation List (Maki)
        // 1. Load reservation list data
        // 2. Set ListView
        // 3. Set onClickListener to remove item from ListView
        //      Show dialog to confirm removing
        //      Remove data from array of favorite list
        //      Show toast "The selected item was removed."

        // TODO : 5. Show Favorite List (Maki)
        // 1. Load item data by use
        // 2. Set ListView
        // 3. Set onClickListener to remove item from ListView
        //      Show dialog to confirm removing
        //      Remove data from array of favorite list
        //      Show toast "The selected item was removed."

        // TODO : 6. Add notification
        // 1. After **mins, show notification

        // TODO (Advanced version) : 1. Use restaurant API of ZOMATO
        // Reference :: https://developers.zomato.com/api

        // TODO (Advanced version) : 2. Add form to make a reservation
        //

    }

    //splash
    class jumpPage implements Runnable {
        public void run() {
            Intent intent = new Intent(MainActivity.this, RestaurantList.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
}
