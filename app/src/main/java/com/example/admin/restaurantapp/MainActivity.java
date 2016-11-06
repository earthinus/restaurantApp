package com.example.admin.restaurantapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        ImageView logo = (ImageView)findViewById(R.id.logo);

        //launch screen
        Handler handler = new Handler();
        handler.postDelayed(new jumpPage(), 10000);

        //fade in icon
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(10000);
        logo.startAnimation(alpha);

        alpha.setFillAfter(true);

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
