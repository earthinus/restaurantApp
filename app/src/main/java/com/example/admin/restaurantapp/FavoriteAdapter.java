package com.example.admin.restaurantapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    private Context context;
    private ArrayList<Favorite> favorites;
    private int restaurantId = new BookList().restaurantId;

    FavoriteAdapter(Context context, ArrayList<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView  name;
        View itemView;

        Holder(View itemView) {
            super(itemView);
            this.icon     = (ImageView) itemView.findViewById(R.id.icon);
            this.name     = (TextView)  itemView.findViewById(R.id.name);
            this.itemView = itemView;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_list_item, viewGroup, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final int pos = position;
        holder.icon.setImageDrawable(favorites.get(position).getIcon());
        holder.name.setText(favorites.get(position).getName());

        // Click (= open restaurant detail page)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent
                Intent intent = new Intent(context, RestaurantDetail.class);
                intent.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, restaurantId);

                // Start Activity
                context.startActivity(intent);
            }
        });

        // LongClick (= remove selected favorite item)
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Get restaurant's name
                String restaurantName = context.getResources().getStringArray(R.array.restaurants)[restaurantId];

                // Get sharedPreference
                SharedPreferences preferences = context.getSharedPreferences(new FavoriteList().PREFERENCE_FAVORITE_FILENAME, MODE_PRIVATE);

                // Remove sharedPreference
                preferences.edit().remove("rest-id" + restaurantId).apply();

                // Remove ArrayList
                favorites.remove(pos);
                notifyItemRemoved(pos);

                // Show Toast
                Toast.makeText(context, "\"" + restaurantName + "\" was removed.", Toast.LENGTH_SHORT).show();

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
