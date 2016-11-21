package com.example.admin.restaurantapp;

public class Review {

    private String text,
                   author_name,
                   author_url,
                   profile_photo_url,
                   rating;
    private int    time;

    public Review(String text, String author_name, String author_url, String profile_photo_url, String rating, int time) {
        this.text = text;
        this.author_name = author_name;
        this.author_url = author_url;
        this.profile_photo_url = profile_photo_url;
        this.rating = rating;
        this.time = time;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
