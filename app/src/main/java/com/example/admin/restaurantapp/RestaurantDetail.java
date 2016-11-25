package com.example.admin.restaurantapp;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Scenario of this class
 *
 * 1. Get place_id through intent of clickListener of RestaurantList
 *
 * 2. Load restaurant info from 'restaurants' table by using the place_id
 *
 * 3. Start IntentService {@link MyIntentService#onHandleIntent} to request to 'detail' API with place_id
 *
 * 4. Receive response of API through BroadcastReceiver
 *
 * 5. Set RecyclerView of reviews
 *
 * 6. Save restaurant detail info on 'restaurants' table
 *    and save reviews on 'reviews' table
 *
 * 7. If user booked, save the booking info on 'booking' table
 *
>>>>>>> 6d1c02d3719cb8bd7dfb9d0a808d56195879534e
 */

public class RestaurantDetail extends AppCompatActivity implements OnMapReadyCallback{

    //TODO
    // View Objects
    private TextView textView_interNationalPhoneNumber, textView_website;
    private EditText txtDate, txtTime, txtName, txtNum, txtEmail;
    public  Button favListButton, bookListButton;
    private Button DatePickButton, TimePickButton, BookButton;
    public  View reservation;
    private AlertDialog alertDialog;

    // Values
    private int restaurant_id = 0; // default
    private String name, placeId, interNationalPhoneNumber, website;
    private double lat, lng;

    public  Context context;
    private Menu mainMenu;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Review> reviewsArrayList;
    private JSONObject response;
    private DBHelper dbHelper;
    private BroadcastReceiver broadcastReceiver;
    private GoogleMap mMap;
    private HashMap<String, String> hashMap_booking = new HashMap<>();
    private AlertDialog.Builder builder;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);

        context = getApplicationContext();

        // View Objects
        ImageView imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        TextView textView_rating = (TextView) findViewById(R.id.textView_rating);
        textView_interNationalPhoneNumber = (TextView) findViewById(R.id.textView_interNationalPhoneNumber);
        textView_website = (TextView) findViewById(R.id.textView_website);
        FloatingActionButton menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        FloatingActionButton menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);
        FloatingActionButton menu3 = (FloatingActionButton) findViewById(R.id.subFloatingMenu3);

        // ArrayList
        reviewsArrayList = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*
        * -------------------------------------------------------------------
        * Get place_id from intent of previous activity
        * -------------------------------------------------------------------
        */

        Intent intent = getIntent();
        if (intent != null) {
            placeId = intent.getStringExtra(RestaurantList.EXTRA_RESTAURANT_ID + ".place_id");

        } else {
            Log.d("Debug", "RestaurantDetail: intent is null.");
        }

        /*
        * -------------------------------------------------------------------
        * Get restaurant data from DB by using placeId
        * -------------------------------------------------------------------
        */

        dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        try {
            cursor = db.query(
                    DBHelper.TABLE_NAME_RESTAURANT,     // Table name
                    null,                               // columns
                    DBHelper.PLACE_ID + " = ?",         // Selection
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
                name = cursor.getString(2);
                textView_name.setText(name);

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
                            hashMap_review = new HashMap<>();

                    switch (status) {

                        case "OK":

                            String formatted_address = (result.has(DBHelper.FORMATTED_ADDRESS)) ? result.getString(DBHelper.FORMATTED_ADDRESS) : "",
                                    place_level = (result.has(DBHelper.PRICE_LEVEL)) ? result.getString(DBHelper.PRICE_LEVEL) : "",
                                    url = (result.has(DBHelper.URL)) ? result.getString(DBHelper.URL) : "";
                            website = (result.has(DBHelper.WEBSITE)) ? result.getString(DBHelper.WEBSITE) : "";
                            interNationalPhoneNumber = (result.has(DBHelper.INTERNATIONAL_PHONE_NUMBER)) ? result.getString(DBHelper.INTERNATIONAL_PHONE_NUMBER) : "";
                            lat = Double.valueOf(result.getJSONObject("geometry").getJSONObject("location").has(DBHelper.LOCATION_LAT) ? result.getJSONObject("geometry").getJSONObject("location").getString(DBHelper.LOCATION_LAT) : "0.0");
                            lng = Double.valueOf(result.getJSONObject("geometry").getJSONObject("location").has(DBHelper.LOCATION_LNG) ? result.getJSONObject("geometry").getJSONObject("location").getString(DBHelper.LOCATION_LNG) : "0.0");
                            boolean opening_hours = (result.getJSONObject(DBHelper.OPENING_HOURS).has("open_now")) && result.getJSONObject(DBHelper.OPENING_HOURS).getBoolean("open_now"); // TODO : separate each weekdays later

                            /*
                            * -------------------------------------------------------------------
                            * Show to View
                            * -------------------------------------------------------------------
                            */

                            // Open or Close
                            String openingCondition;
                            if (opening_hours) openingCondition = "open now";
                            else openingCondition = "close now";

                            ((TextView) findViewById(R.id.textView_formatted_address)).setText(formatted_address);
                            ((TextView) findViewById(R.id.textView_opening_hours)).setText(openingCondition);

                            if (!place_level.equals(""))
                                ((TextView) findViewById(R.id.textView_priceLevel)).setText(place_level);
                            else findViewById(R.id.textView_priceLevel).setVisibility(View.GONE);
                            textView_interNationalPhoneNumber.setText(interNationalPhoneNumber);
                            //((TextView) findViewById(R.id.url)).setText(url);
                            textView_website.setText(website);

                            // GoogleMap
                            showMap();

                            // ClickListener
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

                            /*
                            * -------------------------------------------------------------------
                            * Insert additional restaurant info to Database
                            * -------------------------------------------------------------------
                            */

                            hashMap_restaurant.put(DBHelper.LOCATION_LAT, String.valueOf(lat));
                            hashMap_restaurant.put(DBHelper.LOCATION_LNG, String.valueOf(lng));
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

                                    String text = (reviews.getJSONObject(i).has("text")) ? reviews.getJSONObject(i).getString("text") : "",
                                            author_name = (reviews.getJSONObject(i).has("author_name")) ? reviews.getJSONObject(i).getString("author_name") : "",
                                            author_url = (reviews.getJSONObject(i).has("author_url")) ? reviews.getJSONObject(i).getString("author_url") : "",
                                            profile_photo_url = (reviews.getJSONObject(i).has("profile_photo_url")) ? "https:" + reviews.getJSONObject(i).getString("profile_photo_url") : "http://pictogram2.com/p/p0146/i/m.png";
                                    int rating = (reviews.getJSONObject(i).has("rating")) ? reviews.getJSONObject(i).getInt("rating") : 0,
                                            time = reviews.getJSONObject(i).getInt("time");

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

        // Booking button
        LayoutInflater inflater = (LayoutInflater) RestaurantDetail.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        reservation = inflater.inflate(R.layout.reservation_form, null);
        builder = new AlertDialog.Builder(RestaurantDetail.this).setView(R.layout.reservation_form);

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = builder.show();
                txtDate = (EditText) alertDialog.findViewById(R.id.in_date);
                txtTime = (EditText) alertDialog.findViewById(R.id.in_time);
                txtName = (EditText) alertDialog.findViewById(R.id.in_name);
                txtEmail = (EditText) alertDialog.findViewById(R.id.in_email);
                txtNum = (EditText) alertDialog.findViewById(R.id.in_num);
                DatePickButton = (Button) alertDialog.findViewById(R.id.btn_date);
                TimePickButton = (Button) alertDialog.findViewById(R.id.btn_time);
                BookButton = (Button) alertDialog.findViewById(R.id.book_button);
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog(view);
                    }
                };
                DatePickButton.setOnClickListener(clickListener);
                TimePickButton.setOnClickListener(clickListener);
                BookButton.setOnClickListener(clickListener);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void showMap() {
        LatLng restaurantPosition = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(restaurantPosition).title(name).draggable(true));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantPosition, 15));
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
                Intent intent2 = new Intent(this, BookingList.class);
                startActivity(intent2);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // reservation form buttons function
    public void openDialog(View v) {

        String bookDate = txtDate.getText().toString().trim();
        String bookTime = txtTime.getText().toString().trim();
        String bookName = txtName.getText().toString().trim();
        String bookEmail = txtEmail.getText().toString().trim();
        String bookNum = txtNum.getText().toString().trim();

        txtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        txtDate.addTextChangedListener(textWatcher);
        txtName.addTextChangedListener(textWatcher);
        txtNum.addTextChangedListener(textWatcher);
        txtTime.addTextChangedListener(textWatcher);
        txtEmail.addTextChangedListener(textWatcher);

        // Choose booking date
        if (v == DatePickButton) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear  = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay   = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(RestaurantDetail.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        //choose book time
        if (v == TimePickButton) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int mHour   = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(RestaurantDetail.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        // Show notification or error message
        if (v == BookButton) {

            String mailAddress =
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                            +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                            +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            if (!(bookDate.equals("") || bookTime.equals("") || bookName.equals("") || bookNum.equals("") || bookEmail.equals(""))) {
                showNotification(v);

                txtNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                txtDate.setText("");
                txtName.setText("");
                txtTime.setText("");
                txtEmail.setText("");
                txtNum.setText("");

                /*
                * -------------------------------------------------------------------
                * Send Booking Data to BookingList
                * -------------------------------------------------------------------
                */

                hashMap_booking.put(DBHelper.BOOKING_DATE, String.valueOf(bookDate));
                hashMap_booking.put(DBHelper.BOOKING_TIME, String.valueOf(bookTime));
                hashMap_booking.put(DBHelper.BOOKING_NAME, String.valueOf(bookName));
                hashMap_booking.put(DBHelper.BOOKING_EMAIL, String.valueOf(bookEmail));
                hashMap_booking.put(DBHelper.BOOKING_PEOPLE, String.valueOf(bookNum));
                hashMap_booking.put(DBHelper.PLACE_ID, String.valueOf(placeId));
                hashMap_booking.put(DBHelper.RESTAURANT_NO, String.valueOf(restaurant_id));

                // insert the hashMap to the booking table
                dbHelper.insertRecord(DBHelper.TABLE_NAME_BOOKING, hashMap_booking);

                // Close the dialog
                alertDialog.dismiss();

            } else {

                // Validation
                if (bookDate.equals("") || !(bookDate.matches(dateFormat.toString()))) {
                    txtDate.setError(null);
                    txtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.stat_notify_error, 0);
                }
                if (bookTime.equals("") || !(bookTime.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"))) {
                    txtTime.setError(null);
                    txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.stat_notify_error, 0);
                }
                if ((bookName.equals("") || !(bookName.matches("[a-zA-Z]+")))) {
                    txtName.setError(null);
                    txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.stat_notify_error, 0);
                }
                if (bookEmail.equals("") || !(bookEmail.matches(mailAddress))) {
                    txtEmail.setError(null);
                    txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.stat_notify_error, 0);
                }
                if (bookNum.equals("")) {
                    txtNum.setError(null);
                    txtNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.stat_notify_error, 0);
                }
            }
        }
    }

    // confirm if there is a blank form or not
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            txtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txtNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void showNotification(View v) {

        Intent intent = new Intent(this, BookingList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentText("Booking was Completed!")
                        .setContentTitle("Thank you for using our app.Please confirm your book from BookingList.")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(3000, notification);
    }
}