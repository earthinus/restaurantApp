package com.example.admin.restaurantapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


public class DatePickerDialogFragment extends AppCompatActivity implements
        View.OnClickListener {

    Button DatePickButton, TimePickButton, BookButton;
    EditText txtDate, txtTime, txtName;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_restaurant_detail);

        DatePickButton = (Button) findViewById(R.id.btn_date);
        TimePickButton = (Button) findViewById(R.id.btn_time);
        BookButton = (Button) findViewById(R.id.book_button);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        txtName = (EditText) findViewById(R.id.in_name);

        DatePickButton.setOnClickListener(this);
        TimePickButton.setOnClickListener(this);
        BookButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == DatePickButton) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == TimePickButton) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        // show notification or error message
        if (v == BookButton) {
            if ((txtDate.getText() != null && txtTime.getText() != null && txtName.getText() != null))
            {
                showNotification(v);
            }else{
                if ((txtDate.getText() == null)) {
                    txtDate.setError("Please chose the date.");
                }else if(txtTime.getText() == null){
                    txtTime.setError("Please chose the time.");
                }else if(txtName.getText() == null){
                    txtName.setError("Please enter your name.");
                }
            }
        }
    }

    public void showNotification(View v) {

        Intent intent = new Intent(this, RestaurantDetail.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentText("Booking Complete!")
                        .setContentTitle("Thank you for using our app.Please confirm your book from Book List.")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }
}