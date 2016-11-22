package com.example.admin.restaurantapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MyIntentService extends IntentService {

    // Location, radius TODO : Change this static location to dynamic
    String LOCATION = "49.2845258,-123.1145378";
    String RADIUS = "500";
    String jsonUrl_nearbySearch;
    String jsonUrl_detail;
    final static String BROADCAST_KEY_NEARBY = "broadcast_nearbySearch";
    final static String BROADCAST_KEY_DETAIL = "broadcast_detail";
    final static String INTENT_FILTER_MAIN_ACTIVITY = "com.example.admin.restaurantapp.mainactivity";
    final static String INTENT_FILTER_RESTAURANT_DETAIL = "com.example.admin.restaurantapp.restaurantdetail";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String referrer = intent.getStringExtra("referrer");

        switch (referrer) {

            /*
            * -------------------------------------------------------------------
            * Nearby API
            * -------------------------------------------------------------------
            */

            case "MainActivity" :

                // API URL
                jsonUrl_nearbySearch = getResources().getString(R.string.jsonUrl_nearbySearch)
                        + "location=" + LOCATION
                        + "&language=en"
                        + "&type=restaurant"
                        + "&radius=" + RADIUS
                        + "&key=" + getResources().getString(R.string.API_KEY_IP_ADDRESS);

                Log.d("Debug", "JsonURL(nearby) : " + jsonUrl_nearbySearch);

                requestAPI(referrer, jsonUrl_nearbySearch);
                break;

            /*
            * -------------------------------------------------------------------
            * Detail API
            * -------------------------------------------------------------------
            */

            case "RestaurantDetail" :

                // API URL
                jsonUrl_detail = getResources().getString(R.string.jsonUrl_details)
                        + "placeid=" + intent.getStringExtra("placeid")
                        + "&language=en"
                        + "&key=" + getResources().getString(R.string.API_KEY_IP_ADDRESS);

                Log.d("Debug", "JsonURL(details) : " + jsonUrl_detail);

                requestAPI(referrer, jsonUrl_detail);

                break;

            default:
                break;
        }
    }

    /*
    * -------------------------------------------------------------------
    * Request API
    * -------------------------------------------------------------------
    */

    private void requestAPI(final String referrer, String jsonUrl) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(new JsonObjectRequest(
                Request.Method.GET,
                jsonUrl,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Debug", "Json: " + response);

                        try {
                            String status = response.getString("status");

                            String jsonResponse = response.toString();

                            switch (status) {
                                case "OK":

                                    // Send Broadcast
                                    Intent intent = new Intent();

                                    switch (referrer) {

                                        case "MainActivity":
                                            intent.setAction(INTENT_FILTER_MAIN_ACTIVITY);
                                            intent.putExtra(BROADCAST_KEY_NEARBY, jsonResponse);
                                            break;

                                        case "RestaurantDetail":
                                            intent.setAction(INTENT_FILTER_RESTAURANT_DETAIL);
                                            intent.putExtra(BROADCAST_KEY_DETAIL, jsonResponse);
                                            break;

                                        default:
                                            break;
                                    }
                                    sendBroadcast(intent);
                                    break;

                                case "REQUEST_DENIED":
                                default:
                                    Log.d("Debug", "[onResponse] REQUEST_DENIED");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), "No internet available", Toast.LENGTH_SHORT).show();

                        else if (error instanceof TimeoutError)
                            Toast.makeText(getApplicationContext(), "The request was timeout", Toast.LENGTH_SHORT).show();

                        else if (error instanceof ServerError)
                            Toast.makeText(getApplicationContext(), "Invalid username/password", Toast.LENGTH_SHORT).show();

                        Log.d("Debug", "[onErrorResponse] ErrorMsg: " + error.toString());
                    }
                })
        );
    }
}
