package com.example.admin.restaurantapp;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import java.util.ArrayList;

public class RestaurantDetail extends AppCompatActivity {

    TextView textView_restaurantDetail;
    ImageView imageView_restaurantMainVisual;
    FloatingActionButton menu1, menu2, menu3;
    Button favListButton, bookListButton;
    String restaurantId;
    private TextView mDate;
    private Menu mainMenu;
    Button DatePickButton, TimePickButton, BookButton;
    EditText txtDate, txtTime, txtName;
    int mYear, mMonth, mDay, mHour, mMinute;

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
            restaurantId = intent.getStringExtra(RestaurantList.EXTRA_RESTAURANT_ID + "restaurantId");

        } else {
            Log.d("Debug", "RestaurantDetail getExtra: NG");
        }

        Toast.makeText(this, "place_id: " + restaurantId, Toast.LENGTH_SHORT).show();

        // Initialize each object
        //textView_restaurantName   = (TextView) findViewById(R.id.textView_restaurantName);
        textView_restaurantDetail = (TextView) findViewById(R.id.textView_restaurantDetail);
        imageView_restaurantMainVisual = (ImageView) findViewById(R.id.imageView_restaurantMainVisual);
        DatePickButton = (Button) findViewById(R.id.btn_date);
        TimePickButton = (Button) findViewById(R.id.btn_time);
        BookButton = (Button) findViewById(R.id.book_button);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        txtName = (EditText) findViewById(R.id.in_name);
        // Set each object
//        setTitle(getResources().getStringArray(R.array.restaurants)[restaurantId]);
//        textView_restaurantDetail.setText(getResources().getStringArray(R.array.details)[restaurantId]);
//        imageView_restaurantMainVisual.setImageResource(mainVisuals[restaurantId]);
        //textView_restaurantDetail.setText(getResources().getStringArray(R.array.details)[restaurantId]);

        // Fab
        menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);
        menu3 = (FloatingActionButton) findViewById(R.id.subFloatingMenu3);

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
                intent_favoriteList.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, restaurantId);
                Log.d("Debug", "Start activity");
                startActivity(intent_favoriteList);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RestaurantDetail.this, "Added to Book list", Toast.LENGTH_LONG).show();
                Intent intent_bookList = new Intent(getApplicationContext(), BookList.class);
                intent_bookList.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, restaurantId);
                startActivity(intent_bookList);
            }
        });

        final ListView review_list = (ListView) findViewById(R.id.review_list);
        final ListView menu_list = (ListView) findViewById(R.id.menu_list);
        final ArrayList<User> users = new ArrayList<>();

        String[] title = {
                "AMAZING!",
                "never",
                "little bit expensive"
        };
        String[] star = {
                "★★★★★",
                "★☆☆☆☆",
                "★★★☆☆"
        };
        String[] detail = {
                "This restaurant is amazing!I should go again.",
                "I saw a cockroach in here.I wont go again.",
                "Taste is well. should be cheaper.",
        };
        String[] name = {
                "Hot Chocolate",
                "Vegetable Curry",
                "Salmon Salad"
        };
        String[] price = {
                "$ 12",
                "$ 20",
                "$ 17"
        };

        for (int i = 0; i < title.length; i++) {
            User user = new User();
            user.setTitle(title[i]);
            user.setStar(star[i]);
            user.setDetail(detail[i]);
            users.add(user);
        }

        final UserAdapter adapter = new UserAdapter(this, 0, users);
        review_list.setAdapter(adapter);
    }

    public class UserAdapter extends ArrayAdapter<User> {
        private LayoutInflater layoutInflater;

        public UserAdapter(Context c, int id, ArrayList<User> users) {
            super(c, id, users);
            this.layoutInflater = (LayoutInflater) c.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.review_list_item,
                        parent,
                        false
                );
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.star = (TextView) convertView.findViewById(R.id.review_star);
                holder.detail = (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            User user = (User) getItem(pos);

            holder.title.setText(user.getTitle());
            holder.star.setText(user.getStar());
            holder.detail.setText(user.getDetail());

            return convertView;
        }
    }

    static class ViewHolder {
        TextView title;
        TextView star;
        TextView detail;
    }

    //user methods
    public class User {
        private String title;
        private String star;
        private String detail;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
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
    public void onButtonClick(View v) {

        if (v == DatePickButton) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == TimePickButton) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        // show notification or error message
        if (v == BookButton) {
            if ((txtDate.getText() != null && txtTime.getText() != null && txtName.getText() != null))
            {
                showNotification(v);
            }else{
                if ((txtDate.getText() == null)) {
                    txtDate.setError("Please chose the date.");
                }else if(txtTime.getText() == null){
                    txtTime.setError("Please chose the time.");
                }else if(txtName.getText() == null){
                    txtName.setError("Please enter your name.");
                }
            }
        }
    }

    public void showNotification(View v) {

        Intent intent = new Intent(this, RestaurantDetail.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentText("Booking Complete!")
                        .setContentTitle("Thank you for using our app.Please confirm your book from Book List.")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }
}



