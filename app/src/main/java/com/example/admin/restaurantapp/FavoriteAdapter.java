package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    private Context context;
    private ArrayList<Favorite> favorites;

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
        Picasso.with(context).load(favorites.get(position).getIcon()).fit().into(holder.icon);
        holder.name.setText(favorites.get(position).getName());

        // Click (= open restaurant detail page)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent
                Intent intent = new Intent(context, RestaurantDetail.class);
                intent.putExtra(RestaurantList.EXTRA_RESTAURANT_ID, favorites.get(pos).getPlace_id());

                // Start Activity
                context.startActivity(intent);
            }
        });

        // LongClick (= remove selected favorite item)
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Remove ArrayList
                favorites.remove(pos);
                notifyItemRemoved(pos);

                // Show Toast
                Toast.makeText(context, "\"" + favorites.get(pos).getName() + "\" was removed.", Toast.LENGTH_SHORT).show();

                Log.d("Debug", "pos: " + pos);
                Log.d("Debug", "place_id: " + favorites.get(pos).getPlace_id());

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}
