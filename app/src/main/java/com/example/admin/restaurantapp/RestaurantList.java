package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {

    public final static String EXTRA_RESTAURANT_ID = "com.example.admin.restaurantapp.id";

    ListView listView_restaurants;
    String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        // Initialize ListView of restaurantList
        listView_restaurants = (ListView) findViewById(R.id.restaurantList);

        // Initialize ArrayList of restaurants
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        // Define the variables of restaurant's data
//        int[]    icons2   = getResources().getIntArray(R.array.icons);
        int[] icons = {
                R.drawable.icon_osteriasaviovolpe,
                R.drawable.icon_guuotokomaegastown,
                R.drawable.icon_tuccraftkitchen,
                R.drawable.icon_ancorawaterfrontdining,
                R.drawable.icon_thekegsteakhouse,
                R.drawable.icon_nightingale,
                R.drawable.icon_osteriasaviovolpe,
                R.drawable.icon_guuotokomaegastown,
                R.drawable.icon_tuccraftkitchen,
                R.drawable.icon_ancorawaterfrontdining,
                R.drawable.icon_thekegsteakhouse,
                R.drawable.icon_nightingale
        };

        names   = getResources().getStringArray(R.array.restaurants);
        String[] reviews = getResources().getStringArray(R.array.reviews);

        // Set each restaurant's data to ArrayList
        for (int i = 0; i < names.length; i++) {
            Restaurant restaurant = new Restaurant();
            restaurant.setIcon(BitmapFactory.decodeResource(
                    getResources(),
                    icons[i]
            ));
            restaurant.setName(names[i]);
            restaurant.setReview(reviews[i]);
            restaurants.add(restaurant);
        }

        // Initialize adapter
        RestaurantAdapter adapter = new RestaurantAdapter(this, 0, restaurants);

        // Set adapter to ListView
        listView_restaurants.setAdapter(adapter);

        // Set onClickListener
        listView_restaurants.setOnItemClickListener(new ClickEvent());
    }

    public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

        private LayoutInflater layoutInflater;

        RestaurantAdapter(Context c, int id, ArrayList<Restaurant> restaurants){
            super(c, id, restaurants) ;

            this.layoutInflater = (LayoutInflater) c.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null){
                convertView = layoutInflater.inflate(
                        R.layout.item_list,
                        parent,
                        false
                );
                holder        = new ViewHolder();
                holder.icon   = (ImageView) convertView.findViewById(R.id.icon);
                holder.name   = (TextView) convertView.findViewById(R.id.name);
                holder.review = (TextView) convertView.findViewById(R.id.review);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Restaurant user =  getItem(position);

            holder.icon.setImageBitmap(user.getIcon());
            holder.name.setText(user.getName());
            holder.review.setText(user.getReview());

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView review;
    }

    public class Restaurant {
        private Bitmap icon;
        private String name;
        private String review;

        public Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }

    // Set onClick event to ListViewItems
    class ClickEvent implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

            // Create intent
            Intent intent = new Intent(RestaurantList.this, RestaurantDetail.class);
            intent.putExtra(EXTRA_RESTAURANT_ID, position);

            // Start Activity
            startActivity(intent);
        }
    }
}
