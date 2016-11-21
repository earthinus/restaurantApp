package com.example.admin.restaurantapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.StringTokenizer;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Holder> {

    private Context context;
    private ArrayList<Review> reviews;

    ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView  author_name,
                  rating,
                  author_url,
                  text,
                  time;
        ImageView profile_photo;
        View      itemView;

        Holder(View itemView) {
            super(itemView);
            this.author_name   = (TextView)  itemView.findViewById(R.id.textView_authorName);
            this.rating        = (TextView)  itemView.findViewById(R.id.textView_rating);
            this.text          = (TextView)  itemView.findViewById(R.id.textView_text);
            this.time          = (TextView)  itemView.findViewById(R.id.textView_time);
            this.profile_photo = (ImageView) itemView.findViewById(R.id.imageView_profilePhoto);
        }

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.review_list_item, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Picasso.with(context).load(reviews.get(position).getProfile_photo_url()).fit().into(holder.profile_photo);
        holder.author_name.setText(reviews.get(position).getAuthor_name());
        holder.rating.setText(String.valueOf(reviews.get(position).getRating()));
        holder.text.setText(reviews.get(position).getText());
        holder.time.setText(String.valueOf(reviews.get(position).getTime()));

        holder.profile_photo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO : Set link to profile photo
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
