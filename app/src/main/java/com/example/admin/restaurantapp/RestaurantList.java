package com.example.admin.restaurantapp;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Scenario of this class
 *
 * 1. Get intent
 *
 * 2. Set ArrayList of restaurants
 *
 * 3. Save on database
 *
 * 4. Set Adapter to RecyclerView
 *
 */

public class RestaurantList extends AppCompatActivity {
    Button favListButton, bookListButton;
    private Menu mainMenu;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<Restaurant> restaurants;
    JSONObject response;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        // ArrayList
        restaurants = new ArrayList<>();

        // Database
        dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);

        HashMap<String, String> data = new HashMap<>();

        /*
        * -------------------------------------------------------------------
        * Get Json (by intent)
        * -------------------------------------------------------------------
        */

        Intent intent = getIntent();
        try {
            response = new JSONObject(intent.getStringExtra("splash_jsonResponse"));
            String status = response.getString("status");
            JSONArray results = response.getJSONArray("results");

            // Json status OK
            switch (status) {

                case "OK":
                    for (int i = 0; i < results.length(); i++) {

                        // photosUrl default
                        String[] photoUrls = new String[1];

                        // Set photosUrl
                        if (results.getJSONObject(i).has("photos")) {

                            JSONArray results_photos = results.getJSONObject(i).getJSONArray("photos");
                            photoUrls = new String[results_photos.length()];

                            for (int j = 0; j < results_photos.length(); j++)
                                photoUrls[j] =
                                    getResources().getString(R.string.jsonUrl_photo)
                                    + "photoreference=" + results.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("photo_reference")
                                    + "&maxwidth=400"
                                    + "&maxheight=400"
                                    + "&key=" + getResources().getString(R.string.API_KEY_IP_ADDRESS);

                        // If no photo, set icon
                        } else
                            photoUrls[0] = results.getJSONObject(i).getString("icon");

                        Log.d("Debug", "Thumb " + i + " : " + photoUrls[0]);

                        // restaurants ArrayList
                        String thumb    = photoUrls[0],
                               name     = results.getJSONObject(i).getString("name"),
                               rating   = "★" + results.getJSONObject(i).getString("rating"),
                               place_id = results.getJSONObject(i).getString("place_id");
                        restaurants.add(new Restaurant(thumb, name, rating, place_id));

                        // Database insert
                        data.put(DBHelper.PLACE_ID, place_id);
                        data.put(DBHelper.NAME, name);
                        data.put(DBHelper.THUMB, thumb);
                        data.put(DBHelper.RATING, results.getJSONObject(i).getString(DBHelper.RATING)); // the number without star sign
                        dbHelper.insertRecord(DBHelper.TABLE_NAME_RESTAURANT, data);
                    }

                    // Database read // TODO: move this to intent.getExtra of RestaurantDetail later
                    Cursor cursor = dbHelper.getAllRecords();

                    System.out.println("Count: " + cursor.getCount());
                    while (cursor.moveToNext()) {

                        System.out.println(
                            cursor.getString(0) + "\t" +
                            cursor.getString(1) + "\t" +
                            cursor.getString(2) + "\t" +
                            cursor.getString(3) + "\n" +
                            "-------------------------------------"
                        );
                    }
                    cursor.close();

                    // Adapter
                    adapter = new RestaurantAdapter(this, restaurants);

                    // RecyclerView
                    recyclerView = (RecyclerView) findViewById(R.id.restaurantList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                    break;

                case "ZERO_RESULTS":
                    Toast.makeText(this, "Zero", Toast.LENGTH_SHORT).show(); // TODO : change message later
                    break;

                case "REQUEST_DENIED":
                default:
                    Toast.makeText(this, "Your request was denied.", Toast.LENGTH_SHORT).show(); // TODO : change message later
                    break;
            }

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
