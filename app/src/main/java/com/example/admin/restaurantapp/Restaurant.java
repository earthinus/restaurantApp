package com.example.admin.restaurantapp;

public class Restaurant {

    private String thumb;
    private String name;
    private String rating;
    private String id;

    public Restaurant(String thumb, String name, String rating, String id) {
        this.thumb = thumb;
        this.name = name;
        this.rating = rating;
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String icon) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String review) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
