package com.example.admin.restaurantapp;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable {
    private String response;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(response);
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR
            = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    public MyParcelable(Parcel in) {
        response = in.readString();
    }

    public MyParcelable(String name) {
        this.response = name;
    }
}