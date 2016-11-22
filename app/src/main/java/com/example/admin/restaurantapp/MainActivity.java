package com.example.admin.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Scenario of this class
 *
 * 1. Start Service {@link MyIntentService#onHandleIntent}
 *
 * 2. Receive Broadcast
 *
 * 3. Set intent
 *
 * 4. Start Activity of RestaurantList
 *
 */

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    String response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        /*
        * -------------------------------------------------------------------
        * Start IntentService
        * -------------------------------------------------------------------
        */

        Intent intent_service = new Intent(this, MyIntentService.class);

        //intent_service.putExtra("context", (Serializable) getApplicationContext());
        intent_service.putExtra("referrer", "MainActivity");
        this.startService(intent_service);

        /*
        * -------------------------------------------------------------------
        * Get Json (by Receive Broadcast)
        * -------------------------------------------------------------------
        */

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Receive json response
                response = intent.getStringExtra(MyIntentService.BROADCAST_KEY_NEARBY);

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
        registerReceiver(broadcastReceiver, new IntentFilter(MyIntentService.INTENT_FILTER_MAIN_ACTIVITY));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        Log.d("Debug", "broadcastReceiver was deleted.");
        super.onDestroy();
    }
}