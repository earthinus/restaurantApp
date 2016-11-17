package com.example.admin.restaurantapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RestaurantDetail extends AppCompatActivity {

    TextView textView_restaurantDetail;
    ImageView imageView_restaurantMainVisual;
    FloatingActionButton menu1, menu2, menu3;
    Button favListButton, bookListButton;
    String restaurantId;
    private TextView mDate;
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
        mDate = (TextView) findViewById(R.id.date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = mDate.getText().toString();

                int year = 0;
                int month = 0;
                int dayOfMonth = 0;
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



