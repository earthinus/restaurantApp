package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.Holder> {

    private Context context;
    private ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView  review;
        View itemView;

        Holder(View itemView) {
            super(itemView);
            this.icon   = (ImageView) itemView.findViewById(R.id.icon);
            this.name   = (TextView)  itemView.findViewById(R.id.name);
            this.review = (TextView)  itemView.findViewById(R.id.review);
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
        holder.icon.setImageDrawable(restaurants.get(position).getIcon());
        holder.name.setText(restaurants.get(position).getName());
        holder.review.setText(restaurants.get(position).getReview());

        // Set onClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent
                Intent intent = new Intent(context, RestaurantDetail.class);
                intent.putExtra(new RestaurantList().EXTRA_RESTAURANT_ID, pos);

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
