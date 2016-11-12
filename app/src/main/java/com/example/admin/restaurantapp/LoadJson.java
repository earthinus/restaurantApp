package com.example.admin.restaurantapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.admin.restaurantapp.RestaurantList.resources;
import static com.example.admin.restaurantapp.RestaurantList.restaurants;

public class LoadJson extends AppCompatActivity {

    RequestQueue queue;

    public void getResponse(String json_url, Context context, RecyclerView.Adapter adapter) {
        Log.d("Debug", "Start getResponse()");

        queue = Volley.newRequestQueue(context);

        final RecyclerView.Adapter finalAdapter = adapter;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                json_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        new LoadJson().setResponse(response, restaurants, finalAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        if (error instanceof NoConnectionError)
//                            Toast.makeText(view, "No internet available", Toast.LENGTH_SHORT).show();
//
//                        else if(error instanceof TimeoutError)
//                            Toast.makeText(RestaurantList.this, "The request was timeout", Toast.LENGTH_SHORT).show();
//
//                        else if (error instanceof ServerError)
//                            Toast.makeText(RestaurantList.this, "Invalid username/password", Toast.LENGTH_SHORT).show();

                        Log.d("Debug", "[onErrorResponse] ErrorMsg: " + error.toString());
                    }
                });

        queue.add(jsonRequest);
        }

    public void setResponse(JSONObject response, ArrayList<Restaurant> arrayList, RecyclerView.Adapter adapter) {

        // Set response to ArrayList
        TypedArray icons = resources.obtainTypedArray(R.array.icons);

        for (int i = 0; i < 2; i++) {

            String review = resources.getStringArray(R.array.reviews)[i];

            try {
                String place_id = response.getJSONArray("results").getJSONObject(i).getString("place_id");

                Restaurant restaurant = new Restaurant();
                restaurant.setIcon(icons.getDrawable(i));
                restaurant.setName(place_id);
                restaurant.setReview(review);
                arrayList.add(restaurant);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }
}
