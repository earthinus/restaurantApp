package com.example.admin.restaurantapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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

import static com.example.admin.restaurantapp.MainActivity.context;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Location, radius TODO : Change this static location to dynamic
        String LOCATION = "49.2845258,-123.1145378";
        String RADIUS = "500";

        // API URL
        String jsonUrl_nearbySearch = getResources().getString(R.string.jsonUrl_nearbySearch)
                + "location=" + LOCATION
                + "&lang=en"
                + "&type=restaurant"
                + "&radius=" + RADIUS
                + "&key=" + getResources().getString(R.string.API_KEY_IP_ADDRESS);

        Log.d("Debug", "Start requesting json. JsonURL: " + jsonUrl_nearbySearch);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new JsonObjectRequest(
                Request.Method.GET,
                jsonUrl_nearbySearch,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            String jsonResponse = response.toString();

                            switch (status) {
                                case "OK":
                                    new MainActivity().hideProgressDialog(new View(context));

                                    // Send Broadcast
                                    Intent intent = new Intent()
                                            .setAction("com.example.admin.restaurantapp")
                                            .putExtra("broadcast_nearbySearch", jsonResponse);
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

                        new MainActivity().hideProgressDialog(new View(context));

                        if (error instanceof NoConnectionError)
                            Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();

                        else if(error instanceof TimeoutError)
                            Toast.makeText(context, "The request was timeout", Toast.LENGTH_SHORT).show();

                        else if (error instanceof ServerError)
                            Toast.makeText(context, "Invalid username/password", Toast.LENGTH_SHORT).show();

                        Log.d("Debug", "[onErrorResponse] ErrorMsg: " + error.toString());
                    }
                })
        );
    }
}
