package com.example.admin.restaurantapp;

import android.icu.text.SimpleDateFormat;

class Review {

    private String text,
                   author_name,
                   author_url,
                   profile_photo_url;
    private double rating;
    private int    time,
                   restaurant_id; // = primary key of the restaurant in restaurants table


    Review(String text, String author_name, String author_url, String profile_photo_url, double rating, int time, int restaurant_id) {
        this.text = text;
        this.author_name = author_name;
        this.author_url = author_url;
        this.profile_photo_url = profile_photo_url;
        this.rating = rating;
        this.time = time;
        this.restaurant_id = restaurant_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}
