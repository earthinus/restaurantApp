package com.example.admin.restaurantapp;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.restaurantapp.R;

import java.util.ArrayList;

public class FirstList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_list);
        final ListView myList = (ListView) findViewById(R.id.myList);
        final ArrayList<User> users = new ArrayList<>();

        String[] names = {
                "Sushi Restaurant Japan",
                "Korean Cousin",
                "Chinese Dining",
        };
        String[] review = {
                "★★☆☆☆",
                "★★★★☆",
                "★☆☆☆☆",
        };

        for (int i = 0; i < names.length; i++) {
            User user = new User();
            user.setName(names[i]);
            user.setReview(review[i]);
            users.add(user);
        }

        final UserAdapter adapter = new UserAdapter(this,0,users);
        myList.setAdapter(adapter);
    }

    public class UserAdapter extends ArrayAdapter<User>{
        private LayoutInflater layoutInflater;

        public UserAdapter(Context c,int id,ArrayList<User> users){
            super(c,id,users) ;
            this.layoutInflater = (LayoutInflater) c.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
        }

        @Override
        public View getView(int pos,View convertView,ViewGroup parent){

            ViewHolder holder;
            if(convertView == null){
                convertView = layoutInflater.inflate(
                        R.layout.item_list,
                        parent,
                        false
                );
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.review = (TextView) convertView.findViewById(R.id.review);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            User user = (User) getItem(pos);
            holder.name.setText(user.getName());
            holder.review.setText(user.getReview());

            return convertView;
        }
    }

    static class ViewHolder{
        TextView name;
        TextView review;
    }

    public class User{
        private String name;
        private String review;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }
}
