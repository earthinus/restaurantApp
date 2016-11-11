package com.example.admin.restaurantapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    Button favListButton,bookListButton;
    private Menu mainMenu;

    public final String API_KEY_ANDROID = "AIzaSyBoVEOktMAbvYcZ9D-M8W0x7sSvELFxx6M";
    public final String API_KEY_IP_ADDRESS = "AIzaSyBi-mlLpsIjVeqCroiK3nqtquNTTMq85EE";

    public String GOOGLE_PLACE_ID = "ChIJN1t_tDeuEmsRUsoyG83frY4";

    public String JSON_PATH = "https://maps.googleapis.com/maps/api/place/details/json?"
            + "placeid=" + GOOGLE_PLACE_ID
            + "&key=" + API_KEY_IP_ADDRESS;

    //    public static final String JSON_PATH = "http://services.hanselandpetal.com/feeds/flowers.json";

    RequestQueue queue;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

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

    // ArrayList of restaurants
    ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        // Initialize the request queue
        queue = Volley.newRequestQueue(getApplicationContext());

        // Initialize Recycler of restaurantList
        recyclerView = (RecyclerView) findViewById(R.id.restaurantList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        restaurants = new ArrayList<>();

        // Initialize adapter
        adapter = new RestaurantAdapter(this, restaurants);

        loadJson(new View(this));

        // Set adapter to ListView
        recyclerView.setAdapter(adapter);
    }

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

    public void loadJson(View v) {
        Log.d("Debug", "Start loadJson()");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSON_PATH,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TypedArray icons = getResources().obtainTypedArray(R.array.icons);

                            for (int i = 0; i < 1; i++) {

                                String review = getResources().getStringArray(R.array.reviews)[i];

                                try {
                                    String name = response.getJSONObject("result").getString("name");

                                    Restaurant restaurant = new Restaurant();
                                    restaurant.setIcon(icons.getDrawable(i));
                                    restaurant.setName(name);
                                    restaurant.setReview(review);
                                    restaurants.add(restaurant);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError)
                            Toast.makeText(RestaurantList.this, "No internet available", Toast.LENGTH_SHORT).show();

                        else if(error instanceof TimeoutError)
                            Toast.makeText(RestaurantList.this, "The request was timeout", Toast.LENGTH_SHORT).show();

                        else if (error instanceof ServerError)
                            Toast.makeText(RestaurantList.this, "Invalid username/password", Toast.LENGTH_SHORT).show();

                        Log.d("Debug", "[onErrorResponse] ErrorMsg: " + error.toString());
                    }
                });

        queue.add(jsonRequest);
    }
}
