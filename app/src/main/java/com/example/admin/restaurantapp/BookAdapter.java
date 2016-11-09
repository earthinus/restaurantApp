package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Holder> {

    private Context context;
    private ArrayList<Book> books;

    private int restaurantId = new BookList().restaurantId;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView  name;
        TextView  date;
        View itemView;

        public Holder(View itemView) {
            super(itemView);
            this.icon     = (ImageView) itemView.findViewById(R.id.icon);
            this.name     = (TextView)  itemView.findViewById(R.id.name);
            this.date     = (TextView)  itemView.findViewById(R.id.date);
            this.itemView = itemView;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final int pos = position;
        holder.icon.setImageBitmap(books.get(position).getIcon());
        holder.name.setText(books.get(position).getName());
        holder.date.setText(books.get(position).getDate());

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

        // LongClick (= remove selected book item)
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Get restaurant's name
                String restaurantName = context.getResources().getStringArray(R.array.restaurants)[restaurantId];

                DialogInterface.OnClickListener posL = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                            // Get sharedPreference
//                            SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILENAME, MODE_PRIVATE);
//
//                            // Remove sharedPreference
//                            preferences.edit().remove("rest-id" + restaurantId).apply();
//
//                            // Remove ArrayList
//                            books.remove(pos);
//                            notifyItemRemoved(pos);
                    }
                };

                DialogInterface.OnClickListener negL = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "CANCELED", Toast.LENGTH_SHORT).show();
                    }
                };

                // Set AlertDialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Remove" + restaurantName);
                alertDialog.setMessage("Do you remove " + restaurantName + "?");
                alertDialog.setPositiveButton("OK", posL);
                alertDialog.setNegativeButton("Cancel", negL);

                // Show AlertDialog
                //alertDialog.create().show();

                // TODO : Dialog表示がうまくいかないのであとまわし

                // Get sharedPreference
                SharedPreferences preferences = context.getSharedPreferences(new BookList().PREFERENCE_FILENAME, MODE_PRIVATE);

                // Remove sharedPreference
                preferences.edit().remove("rest-id" + restaurantId).apply();

                // Remove ArrayList
                books.remove(pos);
                notifyItemRemoved(pos);

                // Show Toast
                Toast.makeText(context, "\"" + restaurantName + "\" was removed.", Toast.LENGTH_SHORT).show();

                Log.d("Debug", "pos: " + pos);
                Log.d("Debug", "restaurantId: " + restaurantId);
                Log.d("Debug", alertDialog.toString());

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}