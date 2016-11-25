package com.example.admin.restaurantapp;

public class Book {

    String  icon,
            name,
            booking_time,
            booking_date,
            booking_name,
            booking_email,
            place_id;
    int     booking_people,
            restaurantId;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_name() {
        return booking_name;
    }

    public void setBooking_name(String booking_name) {
        this.booking_name = booking_name;
    }

    public String getBooking_email() {
        return booking_email;
    }

    public void setBooking_email(String booking_email) {
        this.booking_email = booking_email;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public int getBooking_people() {
        return booking_people;
    }

    public void setBooking_people(int booking_people) {
        this.booking_people = booking_people;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
