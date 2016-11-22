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