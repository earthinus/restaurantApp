package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.Holder> {

    private Context context;
    private ArrayList<Restaurant> restaurants;

    RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView  name, rating;
        View itemView;

        Holder(View itemView) {
            super(itemView);
            this.thumb  = (ImageView) itemView.findViewById(R.id.thumb);
            this.name   = (TextView)  itemView.findViewById(R.id.name);
            this.rating = (TextView)  itemView.findViewById(R.id.rating);
            this.itemView = itemView;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_list, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        final int pos = position;

        Picasso.with(context).load(restaurants.get(position).getThumb()).placeholder(R.drawable.progress).resize(100, 100).centerCrop().into(holder.thumb);
        holder.name.setText(restaurants.get(position).getName());
        holder.rating.setText(restaurants.get(position).getRating());

        // Set onClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent
                Intent intent = new Intent(context, RestaurantDetail.class);
                intent.putExtra(RestaurantList.EXTRA_RESTAURANT_ID + ".place_id", restaurants.get(pos).getId());

                // Start Activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
