package com.example.admin.restaurantapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AllContactParcelable implements Parcelable {

    private ArrayList<AllContactParcelable> restaurantList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(restaurantList);
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

//    private AllContactParcelable(Parcel in) {
//        restaurantList = in.createTypedArrayList(AllContactParcelable.CREATOR);
//    }
//
//    public AllContactParcelable(ArrayList<AllContactParcelable> contactList) {
//        this.restaurantList = contactList;
//    }
}
