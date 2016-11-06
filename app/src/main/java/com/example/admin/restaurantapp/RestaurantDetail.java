package com.example.admin.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

public class RestaurantDetail extends AppCompatActivity {

    TextView textView_restaurantDetail;
    ImageView imageView_restaurantMainVisual;
    FloatingActionButton menu1, menu2, menu3;
    int restaurantId = 0;

    private Menu mainMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        mainMenu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        final int action = event.getAction();
        final int keyCode = event.getKeyCode();
        if (action == KeyEvent.ACTION_UP) {
            //show menu
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                if (mainMenu != null) {
                    mainMenu.performIdentifierAction(R.id.overflow_options, 0);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get intent
        final Intent intent = getIntent();
        if (intent != null) {
            restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, 0);

        } else {
            Log.d("Debug", "getExtra: NG");
        }

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


        // Fab

        menu1 = (FloatingActionButton)findViewById(R.id.subFloatingMenu1) ;
        menu2 = (FloatingActionButton)findViewById(R.id.subFloatingMenu2) ;
        menu3 = (FloatingActionButton)findViewById(R.id.subFloatingMenu3) ;

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this , " Review Icon clicked ", Toast.LENGTH_LONG).show();
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this , "Added to Favorite list", Toast.LENGTH_LONG).show();
                Intent intent_favoriteList = new Intent(getApplicationContext(), FavoriteList.class);
                intent_favoriteList.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, restaurantId);
                Log.d("Debug", "Start activity");
                startActivity(intent_favoriteList);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this , "Added to Book list", Toast.LENGTH_LONG).show();
                Intent intent_bookList = new Intent(getApplicationContext(), BookList.class);
                intent_bookList.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, restaurantId);
                startActivity(intent_bookList);
            }
        });
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
