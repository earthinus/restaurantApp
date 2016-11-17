package com.example.admin.restaurantapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    Button favListButton, bookListButton;
    private Menu mainMenu;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<Restaurant> restaurants;
    JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        Intent intent = getIntent();

        restaurants = new ArrayList<>();

        try {
            String stringResponse = intent.getStringExtra("splash_jsonResponse");

            response = new JSONObject(stringResponse);

            JSONArray results = response.getJSONArray("results");
            Log.d("Debug", "results.length(): " + String.valueOf(results.length())); // TODO : delete this later

            for (int i = 0; i < results.length(); i++) {

                // photosUrl default
                String[] photoUrls = new String[1];

                // Set photosUrl
                if(results.getJSONObject(i).has("photos")) {

                    JSONArray results_photos = results.getJSONObject(i).getJSONArray("photos");
                    photoUrls = new String[results_photos.length()];

                    for (int j = 0; j < results_photos.length(); j++)
                        photoUrls[j] =
                            getResources().getString(R.string.jsonUrl_photo)
                            + "photoreference=" + results.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("photo_reference")
                            + "&maxwidth=400"
                            + "&maxheight=400"
                            + "&key=" + getResources().getString(R.string.API_KEY_IP_ADDRESS);
                // No photo
                } else
                    photoUrls[0] = results.getJSONObject(i).getString("icon");

                String thumb = photoUrls[0];
                Log.d("Debug", "photoURL: " + "i:" + i + " " + photoUrls[0]);
                String name   = results.getJSONObject(i).getString("name");
                String rating = "★" + results.getJSONObject(i).getString("rating");
                String id     = results.getJSONObject(i).getString("place_id");
                restaurants.add(new Restaurant(thumb, name, rating, id));
            }

            recyclerView = (RecyclerView) findViewById(R.id.restaurantList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new RestaurantAdapter(this, restaurants);

            // Set adapter to RecyclerView
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
