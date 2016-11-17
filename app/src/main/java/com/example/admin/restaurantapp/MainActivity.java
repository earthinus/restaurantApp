package com.example.admin.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Scenario of this class
 *
 * @author Mai
 *
 * 1. Set tag of progressBar
 *          {@link #progressBar}
 *
 * 2. Start Service
 *          {@link MyIntentService#onHandleIntent}
 *
 * 3. Receive Broadcast
 *
 * 4. Start Activity of RestaurantList
 *
 */

public class MainActivity extends AppCompatActivity {

    public static Context context;

    ProgressBar progressBar;
    IntentFilter filter;
    BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        // TODO : あとで消す
        context = this;

        // Show Logo
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setTag("progressBar");

        // Start Service
        Intent intent_service = new Intent(this, MyIntentService.class);
        this.startService(intent_service);

        // Receive broadcast
        filter = new IntentFilter("com.example.admin.restaurantapp");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Receive json response
                String response = intent.getStringExtra("broadcast_nearbySearch");

                Log.d("Debug", "Response: " + response);

                // Define intent
                Intent intent_restaurantList = new Intent(context, RestaurantList.class)
                        .putExtra("splash_jsonResponse", response);

                // Start RestaurantList Activity
                startActivity(intent_restaurantList);
            }
        };

        // TODO : 3. Show Restaurant Detail (Maki)
        // 1. Load array data by using restaurant's id
        // 2. Set the array to each object
        // 3. Set onClick function to "reservation" fab button
        //      Show toast "This restaurant was added to favorite"
        // 4. Set onClick function to "favorite" fab
        //      Change the color or icon of fab
        //      Set onClick function for removing from favorite list

        // TODO : 4. Show Book List (Maki)
        // 1. Load book list data
        // 2. Set ListView
        // 3. Set onLongClickListener to remove item from ListView
        //      Show dialog to confirm removing
        //      Remove data from array of favorite list
        //      Show toast "The selected item was removed."

        // TODO : 5. Show Favorite List (Maki)
        // 1. Load item data by use
        // 2. Set ListView
        // 3. Set onLongClickListener to remove item from ListView
        //      Show dialog to confirm removing
        //      Remove data from array of favorite list
        //      Show toast "The selected item was removed."

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(broadcastReceiver, filter);
    }

    public void hideProgressDialog(View view) {
        Log.d("Debug", "Start hideProgressDialog");
        view.findViewWithTag("progressBar");
    }
}