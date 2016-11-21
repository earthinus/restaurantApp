package com.example.admin.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * Scenario of this class
 *
 * @author Maki
 *
 * 1. Start Service
 *          {@link MyIntentService#onHandleIntent}
 *
 * 2. Receive Broadcast
 *
 * 3. Start Activity of RestaurantList
 *
 */

public class RestaurantDetail extends AppCompatActivity {

    ImageView imageView_photo;
    TextView textView_name,
             textView_rating;
    FloatingActionButton menu1, menu2, menu3;
    Button favListButton, bookListButton;
    String placeId;
    private TextView mDate;
    private Menu mainMenu;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<Review> reviewsArrayList;
    JSONObject response;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Context context;

    IntentFilter filter;
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
        menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);
        menu3 = (FloatingActionButton) findViewById(R.id.subFloatingMenu3);

        // ArrayList
        reviewsArrayList = new ArrayList<>();

        /*
        * -------------------------------------------------------------------
        * Get intent from previous activity
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
        * Get resource of nearbyAPI from DB by using placeId
        * -------------------------------------------------------------------
        */

        dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        db = dbHelper.getReadableDatabase();

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
            }
            cursor.close();

        } catch (Exception e) {
            Log.d("Debug", "Catch error: " + e.toString());

        } finally {
            db.close();
        }

        /*
        * -------------------------------------------------------------------
        * Start IntentService
        * -------------------------------------------------------------------
        */

        Intent intent_service = new Intent(this, MyIntentService.class);
        intent_service.putExtra("referrer", "RestaurantDetail");
        intent_service.putExtra("placeid", placeId);
        this.startService(intent_service);

        /*
        * -------------------------------------------------------------------
        * Broadcast Receiver
        * -------------------------------------------------------------------
        */

        filter = new IntentFilter("com.example.admin.restaurantapp.restaurantdetail");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    // Convert to JSONObject
                    response = new JSONObject(intent.getStringExtra("broadcast_detail"));
                    Log.d("Debug", "Response: " + response);
                    String status = response.getString("status");
                    JSONObject result = response.getJSONObject("result");

                    switch (status) {

                        case "OK":

                            if (result.has("reviews")) {

                                JSONArray reviews = result.getJSONArray("reviews");
                                // Set reviewsArrayList
                                for (int i = 0; i < reviews.length(); i++) {

                                    String text              = (reviews.getJSONObject(i).has("text")) ? reviews.getJSONObject(i).getString("text") : "",
                                           author_name       = (reviews.getJSONObject(i).has("author_name")) ? reviews.getJSONObject(i).getString("author_name") : "",
                                           author_url        = (reviews.getJSONObject(i).has("author_url")) ? reviews.getJSONObject(i).getString("author_url") : "",
                                           profile_photo_url = (reviews.getJSONObject(i).has("profile_photo_url")) ? reviews.getJSONObject(i).getString("profile_photo_url") : "data:image/png;base64,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",
                                           rating            = (reviews.getJSONObject(i).has("rating")) ? reviews.getJSONObject(i).getString("rating") : "";
                                    int    time              = reviews.getJSONObject(i).getInt("time");
                                    reviewsArrayList.add(new Review(text, author_name, author_url, profile_photo_url, rating, time));
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

                // TODO : Save on database of restaurant


                // TODO : Save on database of review
            }
        };

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(broadcastReceiver, filter);
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

    @Override
    protected void onStop() {

        Log.d("Debug", "[onDestroy]");
        dbHelper.deleteDatabase(); // TODO : Delete this command later
        super.onStop();
    }
}