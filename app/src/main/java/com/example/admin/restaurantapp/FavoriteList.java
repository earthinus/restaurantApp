package com.example.admin.restaurantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    private ArrayList<Favorite> favorites = new ArrayList<>();
    int restaurantId = 0;
    final String PREFERENCE_FILENAME_FAV = "Favorite-list";

    Favorite favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);

        // Set backButton on ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get intent
        final Intent intent = getIntent();
        if (intent != null) {
            restaurantId = intent.getIntExtra(RestaurantList.EXTRA_RESTAURANT_ID, -1);
            getSharedPreferences(PREFERENCE_FILENAME_FAV, MODE_PRIVATE).edit().putInt("rest-id" + restaurantId, restaurantId).apply();
            Log.d("Debug", "getExtra: " + restaurantId);

        } else {
            Log.d("Debug", "getExtra: NG");
        }

        // Initialize Recycler of restaurantList
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Data-sets
        int[] icons = new int[]{
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
        String[] names = getResources().getStringArray(R.array.restaurants);

        // Set each restaurant's data to ArrayList
        for (int i = 0; i < names.length; i++) {
            if (getSharedPreferences(PREFERENCE_FILENAME_FAV, MODE_PRIVATE).getInt("rest-id" + i, -1) != -1) {
                favorite = new Favorite();
                favorite.setIcon(BitmapFactory.decodeResource(getResources(), icons[i]));
                favorite.setName(names[i]);
                favorites.add(favorite);
            }
        }

        // Initialize adapter
        RecyclerView.Adapter adapter = new FavoriteAdapter(favorites);

        // TODO : set EmptyView

        // Set adapter to ListView
        recyclerView.setAdapter(adapter);
    }

    public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

        private ArrayList<Favorite> favorites;

        FavoriteAdapter(ArrayList<Favorite> favorites){
            this.favorites = favorites;
        }

        class Holder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView  name;
            View row;

            Holder(View row) {
                super(row);
                this.icon   = (ImageView) row.findViewById(R.id.icon);
                this.name   = (TextView)  row.findViewById(R.id.name);
                this.row    = row;
            }
        }

        @Override
        public FavoriteList.FavoriteAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favorite_list_item, parent, false);

            return new FavoriteList.FavoriteAdapter.Holder(v);
        }

        @Override
        public void onBindViewHolder(FavoriteList.FavoriteAdapter.Holder holder, int position) {
            final int pos = position;
            holder.icon.setImageBitmap(favorites.get(position).getIcon());
            holder.name.setText(favorites.get(position).getName());

            // Click (= open restaurant detail page)
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Create intent
                    Intent intent = new Intent(FavoriteList.this, RestaurantDetail.class);
                    intent.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, restaurantId);

                    // Start Activity
                    startActivity(intent);
                }
            });

            // LongClick (= remove selected favorite item)
            holder.row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    // Get restaurant's name
                    String restaurantName = getResources().getStringArray(R.array.restaurants)[restaurantId];

//                    DialogInterface.OnClickListener posL = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            // Get sharedPreference
//                            SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILENAME, MODE_PRIVATE);
//
//                            // Remove sharedPreference
//                            preferences.edit().remove("rest-id" + restaurantId).apply();
//
//                            // Remove ArrayList
//                            favorites.remove(pos);
//                            notifyItemRemoved(pos);
//                        }
//                    };

//                    DialogInterface.OnClickListener negL = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getApplicationContext(), "CANCELED", Toast.LENGTH_SHORT).show();
//                        }
//                    };


                    // Set AlertDialog
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
//                    alertDialog.setTitle("Remove" + restaurantName);
//                    alertDialog.setMessage("Do you remove " + restaurantName + "?");
//                    alertDialog.setPositiveButton("OK", posL);
//                    alertDialog.setNegativeButton("Cancel", negL);

                    // Show AlertDialog
                    //alertDialog.create().show();


                    // TODO : Dialog表示がうまくいかないのであとまわし

                    // Get sharedPreference
                    SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILENAME_FAV, MODE_PRIVATE);

                    // Remove sharedPreference
                    preferences.edit().remove("rest-id" + restaurantId).apply();

                    // Remove ArrayList
                    favorites.remove(pos);
                    notifyItemRemoved(pos);

                    // Show Toast
                    Toast.makeText(getApplicationContext(), "\"" + restaurantName + "\" was removed.", Toast.LENGTH_SHORT).show();

                    Log.d("Debug", "pos: " + pos);
                    Log.d("Debug", "restaurantId: " + restaurantId);

                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {

            return favorites.size();
        }
    }

    // Set function of backButton on ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}