package com.example.admin.restaurantapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    Button favListButton,bookListButton;
    private Menu mainMenu;

    static Resources resources;

    static RecyclerView recyclerView;

    static ArrayList<Restaurant> restaurants;

    public final String API_KEY_ANDROID = "AIzaSyBoVEOktMAbvYcZ9D-M8W0x7sSvELFxx6M";
    public final String API_KEY_IP_ADDRESS = "AIzaSyBi-mlLpsIjVeqCroiK3nqtquNTTMq85EE";

    public String GOOGLE_PLACE_ID = "ChIJN1t_tDeuEmsRUsoyG83frY4";
    public String LOCATION = "49.2845258,-123.1145378";
    public String RADIUS = "500";
    static RecyclerView.Adapter adapter;

    // Json URL for Google Place [details]
    public String jsonUrl_details = "https://maps.googleapis.com/maps/api/place/details/json?"
            + "&placeid=" + GOOGLE_PLACE_ID
            + "&key=" + API_KEY_IP_ADDRESS;

    // Json URL for Google Place RadarSearch
    public String jsonUrl_RadarSearch = "https://maps.googleapis.com/maps/api/place/radarsearch/json?"
            + "location=" + LOCATION
            + "&type=restaurant"
            + "&radius=" + RADIUS
            + "&key=" + API_KEY_IP_ADDRESS;

    //    public static final String JSON_PATH = "http://services.hanselandpetal.com/feeds/flowers.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        resources = getResources();

        recyclerView = (RecyclerView) findViewById(R.id.restaurantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurants = new ArrayList<>();

        adapter = new RestaurantAdapter(this, restaurants);

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Get Json response
        new LoadJson().getResponse(jsonUrl_RadarSearch, this, adapter);
    }

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

    public final static String EXTRA_RESTAURANT_ID = "com.example.admin.restaurantapp.id";
    public final static String EXTRA_RESTAURANT_FAV_ID = "com.example.admin.restaurantapp.favid";

    // Set function of backButton on ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        favListButton = (Button)findViewById(R.id.action_favorite);
        bookListButton = (Button)findViewById(R.id.action_book);
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent intent = new Intent(this, FavoriteList.class);
                startActivity(intent);
                break;
            case R.id.action_book:
                Intent intent2 = new Intent(this, BookList.class);
                startActivity(intent2);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
