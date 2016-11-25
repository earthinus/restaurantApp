package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

class BookAdapter extends RecyclerView.Adapter<BookAdapter.Holder> {

    private Context context;
    private ArrayList<Book> books;

    BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView  name, date, people;
        View itemView;

        Holder(View itemView) {
            super(itemView);
            this.icon     = (ImageView) itemView.findViewById(R.id.bookingList_icon);
            this.name     = (TextView)  itemView.findViewById(R.id.bookingList_name);
            this.date     = (TextView)  itemView.findViewById(R.id.bookingList_date);
            this.people   = (TextView)  itemView.findViewById(R.id.bookingList_people);
            this.itemView = itemView;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, viewGroup, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final int pos = position;
        Picasso.with(context).load(books.get(position).getIcon()).fit().into(holder.icon);
        holder.name.setText(books.get(position).getName());
        holder.date.setText(books.get(position).getBooking_date() + " " + books.get(position).getBooking_time());
        holder.people.setText(books.get(position).getBooking_people() + "person");

        // Click (= open restaurant detail page)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent
                Intent intent = new Intent(context, RestaurantDetail.class);
                intent.putExtra(RestaurantList.EXTRA_RESTAURANT_ID + ".place_id", books.get(pos).getPlace_id());
                context.startActivity(intent);
            }
        });

        // LongClick (= remove selected book item)
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Get restaurant's name
                String restaurantName = String.valueOf(books.get(pos).getName());

                DialogInterface.OnClickListener posL = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

                // Show AlertDialog TODO : Dialog表示がうまくいかないのであとまわし
                //alertDialog.create().show();

                // TODO : Remove the booking from 'bookings' table

                // Remove ArrayList
                books.remove(pos);
                notifyItemRemoved(pos);

                // Show Toast
                Toast.makeText(context, "\"" + restaurantName + "\" was removed.", Toast.LENGTH_SHORT).show();

                Log.d("Debug", "pos: " + pos);
                //Log.d("Debug", "restaurantId: " + restaurantId);
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