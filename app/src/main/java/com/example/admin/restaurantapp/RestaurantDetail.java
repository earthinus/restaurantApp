package com.example.admin.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Scenario of this class
 *
 * 1. Get place_id through intent
 *
 * 2. Load restaurant info of the place_id
 *
 * 3. Start Service {@link MyIntentService#onHandleIntent}
 *
 * 4. Receive Broadcast
 *
 * 5. Set ArrayList of review
 *
 * 6. Save to database
 *
 */

public class RestaurantDetail extends AppCompatActivity {

    ImageView imageView_photo;
    TextView textView_name,
             textView_rating,
             textView_interNationalPhoneNumber,
             textView_website;
    FloatingActionButton menu1, menu2, menu3;
    Button favListButton, bookListButton;
    String placeId,
            interNationalPhoneNumber,
            website;
    private TextView mDate;
    private Menu mainMenu;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<Review> reviewsArrayList;
    JSONObject response;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Context context;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);
        context = getApplicationContext();

        // View Objects
        imageView_photo = (ImageView) findViewById(R.id.imageView_photo); // TODO Picassoで、CardViewのhorizontalScroll表示にする
        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_rating = (TextView) findViewById(R.id.textView_rating);
        mDate = (TextView) findViewById(R.id.date);
        mDateClickListener();
        textView_interNationalPhoneNumber = (TextView) findViewById(R.id.interNationalPhoneNumber);
        textView_website = (TextView) findViewById(R.id.interNationalPhoneNumber);
        menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);
        menu3 = (FloatingActionButton) findViewById(R.id.subFloatingMenu3);

        // ArrayList
        reviewsArrayList = new ArrayList<>();

        /*
        * -------------------------------------------------------------------
        * Get place_id from intent of previous activity
        * -------------------------------------------------------------------
        */

        Intent intent = getIntent();
        if (intent != null) {
            placeId = intent.getStringExtra(RestaurantList.EXTRA_RESTAURANT_ID + "restaurantId");

        } else {
            Log.d("Debug", "RestaurantDetail: intent is null.");
        }

        /*
        * -------------------------------------------------------------------
        * Get restaurant data from DB by using placeId
        * -------------------------------------------------------------------
        */

        dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        db = dbHelper.getReadableDatabase();

        int restaurant_id = 0; // default

        Cursor cursor;
        try {
            cursor = db.query(
                    DBHelper.TABLE_NAME_RESTAURANT,     // Table name
                    null,                               // columns
                    DBHelper.PLACE_ID + " = ?" ,        // Selection
                    new String[]{placeId},              // SelectionArgs
                    null,                               // groupBy
                    null,                               // Having
                    null                                // orderBy
            );

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                // Put nearby json data into View objects
                Picasso.with(this).load(cursor.getString(3)).fit().placeholder(R.drawable.progress).into(imageView_photo);
                textView_name.setText(cursor.getString(2));
                textView_rating.setText(cursor.getString(4));

                // Keep RestaurantID to insert to reviews table
                System.out.println("rowID: " + cursor.getString(0));
                restaurant_id = Integer.valueOf(cursor.getString(0));
            }
            cursor.close();

        } catch (Exception e) {
            Log.d("Debug", "Catch error: " + e.toString());

        } finally {
            db.close();
        }

        /*
        * -------------------------------------------------------------------
        * Start IntentService to request to detail API
        * -------------------------------------------------------------------
        */

        Intent intent_service = new Intent(this, MyIntentService.class);
        intent_service.putExtra("referrer", "RestaurantDetail");
        intent_service.putExtra("placeid", placeId);
        this.startService(intent_service);

        /*
        * -------------------------------------------------------------------
        * Get Json (by Broadcast Receiver)
        * -------------------------------------------------------------------
        */

        final int final_restaurant_id = restaurant_id;

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    // Convert to JSONObject
                    response = new JSONObject(intent.getStringExtra(MyIntentService.BROADCAST_KEY_DETAIL));
                    Log.d("Debug", "Response: " + response);
                    String status = response.getString("status");
                    JSONObject result = response.getJSONObject("result");

                    HashMap<String, String> hashMap_restaurant = new HashMap<>(),
                                            hashMap_review     = new HashMap<>();

                    switch (status) {

                        case "OK":

                            String formatted_address        = (result.has(DBHelper.FORMATTED_ADDRESS)) ? result.getString(DBHelper.FORMATTED_ADDRESS) : "",
                                   place_level              = (result.has(DBHelper.PRICE_LEVEL)) ? result.getString(DBHelper.PRICE_LEVEL) : "",
                                   url                      = (result.has(DBHelper.URL)) ? result.getString(DBHelper.URL) : "";
                            website                         = (result.has(DBHelper.WEBSITE)) ? result.getString(DBHelper.WEBSITE) : "";
                            interNationalPhoneNumber        = (result.has(DBHelper.INTERNATIONAL_PHONE_NUMBER)) ? result.getString(DBHelper.INTERNATIONAL_PHONE_NUMBER) : "";
                            boolean opening_hours           = (result.getJSONObject(DBHelper.OPENING_HOURS).has("open_now")) && result.getJSONObject(DBHelper.OPENING_HOURS).getBoolean("open_now"); // TODO : separate each weekdays later

                            /*
                            * -------------------------------------------------------------------
                            * Show to View
                            * -------------------------------------------------------------------
                            */

                            // Open or Close
                            String openingCondition;
                            if (opening_hours) openingCondition = "open now";
                            else               openingCondition = "close now";

                            ((TextView) findViewById(R.id.formatted_address)).setText(formatted_address);
                            ((TextView) findViewById(R.id.opening_hours)).setText(openingCondition);

                            if (!place_level.equals("")) ((TextView) findViewById(R.id.priceLevel)).setText(place_level);
                            else findViewById(R.id.priceLevel).setVisibility(View.GONE);
                            textView_interNationalPhoneNumber.setText(interNationalPhoneNumber);
                            //((TextView) findViewById(R.id.url)).setText(url);
                            textView_website.setText(website);

                            /*
                            * -------------------------------------------------------------------
                            * Insert additional restaurant info to Database
                            * -------------------------------------------------------------------
                            */

                            hashMap_restaurant.put(DBHelper.LOCATION_LAT, result.getJSONObject("geometry").getJSONObject("location").getString(DBHelper.LOCATION_LAT));
                            hashMap_restaurant.put(DBHelper.LOCATION_LNG, result.getJSONObject("geometry").getJSONObject("location").getString(DBHelper.LOCATION_LNG));
                            hashMap_restaurant.put(DBHelper.FORMATTED_ADDRESS, formatted_address);
                            hashMap_restaurant.put(DBHelper.PRICE_LEVEL, (result.has(DBHelper.PRICE_LEVEL) ? result.getString(DBHelper.PRICE_LEVEL) : ""));
                            hashMap_restaurant.put(DBHelper.INTERNATIONAL_PHONE_NUMBER, interNationalPhoneNumber);
                            hashMap_restaurant.put(DBHelper.OPENING_HOURS, String.valueOf(opening_hours));
                            hashMap_restaurant.put(DBHelper.URL, url);
                            hashMap_restaurant.put(DBHelper.WEBSITE, website);
                            dbHelper.updateRestaurantsTableRow(placeId, hashMap_restaurant);

                            if (result.has("reviews")) {

                                JSONArray reviews = result.getJSONArray("reviews");

                                Review review;

                                // Set reviewsArrayList
                                for (int i = 0; i < reviews.length(); i++) {

                                    String text              = (reviews.getJSONObject(i).has("text")) ? reviews.getJSONObject(i).getString("text") : "",
                                           author_name       = (reviews.getJSONObject(i).has("author_name")) ? reviews.getJSONObject(i).getString("author_name") : "",
                                           author_url        = (reviews.getJSONObject(i).has("author_url")) ? reviews.getJSONObject(i).getString("author_url") : "",
                                           profile_photo_url = (reviews.getJSONObject(i).has("profile_photo_url")) ? "https:" + reviews.getJSONObject(i).getString("profile_photo_url") : "http://pictogram2.com/p/p0146/i/m.png";
                                    int    rating            = (reviews.getJSONObject(i).has("rating")) ? reviews.getJSONObject(i).getInt("rating") : 0,
                                           time              = reviews.getJSONObject(i).getInt("time");

                                    review = new Review(text, author_name, author_url, profile_photo_url, rating, time, final_restaurant_id);
                                    reviewsArrayList.add(review);

                                    /*
                                    * -------------------------------------------------------------------
                                    * Insert reviews to Database
                                    * -------------------------------------------------------------------
                                    */

                                    hashMap_review.put(DBHelper.REVIEW_TEXT, text);
                                    hashMap_review.put(DBHelper.REVIEW_AUTHOR_NAME, author_name);
                                    hashMap_review.put(DBHelper.REVIEW_AUTHOR_URL, author_url);
                                    hashMap_review.put(DBHelper.REVIEW_PROFILE_PHOTO_URL, profile_photo_url);
                                    hashMap_review.put(DBHelper.REVIEW_RATING, String.valueOf(rating));
                                    hashMap_review.put(DBHelper.REVIEW_TIME, String.valueOf(time));
                                    hashMap_review.put(DBHelper.RESTAURANT_NO, String.valueOf(final_restaurant_id));
                                    dbHelper.insertRecord(DBHelper.TABLE_NAME_REVIEW, hashMap_review);
                                }

                                // Adapter
                                adapter = new ReviewAdapter(context, reviewsArrayList);

                                // RecyclerView
                                recyclerView = (RecyclerView) findViewById(R.id.review_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.setAdapter(adapter);
                            }
                            break;

                        case "ZERO_RESULTS":
                            Toast.makeText(context, "Zero", Toast.LENGTH_SHORT).show(); // TODO : change message later
                            break;

                        case "REQUEST_DENIED":
                        default:
                            Toast.makeText(context, "Your request was denied.", Toast.LENGTH_SHORT).show(); // TODO : change message later
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
        * -------------------------------------------------------------------
        * Set ClickListener of fab buttons
        * -------------------------------------------------------------------
        */

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this, " Review Icon clicked ", Toast.LENGTH_LONG).show();
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this, "Added to Favorite list", Toast.LENGTH_LONG).show();
                Intent intent_favoriteList = new Intent(getApplicationContext(), FavoriteList.class);
                intent_favoriteList.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, placeId);
                Log.d("Debug", "Start activity");
                startActivity(intent_favoriteList);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this, "Added to Book list", Toast.LENGTH_LONG).show();
                Intent intent_bookList = new Intent(getApplicationContext(), BookList.class);
                intent_bookList.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, placeId);
                startActivity(intent_bookList);
            }
        });

        textView_interNationalPhoneNumber.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + interNationalPhoneNumber)));
                    }
                }
        );

        textView_website.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
                    }
                }
        );


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MyIntentService.INTENT_FILTER_RESTAURANT_DETAIL));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        Log.d("Debug", "broadcastReceiver was deleted.");
        super.onDestroy();
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

    private void mDateClickListener() {
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = mDate.getText().toString();

                int year;
                int month;
                int dayOfMonth;
                if (TextUtils.isEmpty(date)) {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    year = Integer.valueOf(date.substring(0, 4));
                    month = Integer.valueOf(date.substring(5, 7));
                    month = month - 1;
                    dayOfMonth = Integer.valueOf(date.substring(8, 10));
                }
                showDatePickerDialog(year, month, dayOfMonth);
            }
        });
    }

    // Set function of backButton on ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        favListButton = (Button) findViewById(R.id.action_favorite);
        bookListButton = (Button) findViewById(R.id.action_book);
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

    public void showDatePickerDialog(int year, int month, int dayOfMonth) {
        DatePickerDialogFragment dialog = DatePickerDialogFragment.newInstance(year, month, dayOfMonth);
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDate(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        mDate.setText(sdf.format(cal.getTime()));
    }
}