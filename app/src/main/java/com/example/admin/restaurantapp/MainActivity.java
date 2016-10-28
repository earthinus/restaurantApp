package com.example.admin.restaurantapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO : 1. Show launch screen (Mai)
        // 1. Set timer (as temporally, instead of loading)
        //      call First_list after **sec automatically.

        // TODO : 2. Show Restaurant List (Mai)
        // 1. Initialize array data of restaurant list
        //    Array restaurants:
        //      Array restaurant:
        //          restaurantId,
        //          restaurantName,
        //          restaurantDetailText,
        //          restaurantReview,
        //          restaurantIcon,
        //          restaurantMainVisual
        //          Array menu:
        //              menuListId,
        //              menuName,
        //              menuPrice,
        //              menuImage
        // 2. Set the array to ListView
        // 3. Set onClick function to ListView's item (Intent: RestaurantId)
        // 4. Set NavBar menu of "Favorite", "Reserved"

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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_book) {
            Log.d("Tag", "Pressed book menu");
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Log.d("Tag", "Pressed favorite menu");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
