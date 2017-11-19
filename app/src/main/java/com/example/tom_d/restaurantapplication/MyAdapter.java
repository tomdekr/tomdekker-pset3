package com.example.tom_d.restaurantapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import  android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.tom_d.restaurantapplication.R.layout.row_layout_2;


class myAdapter extends ArrayAdapter<String> {

    public myAdapter(Context context, String[] values) {
        super(context, row_layout_2, values);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(row_layout_2, parent, false);

        String categories = getItem(position);

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.textView1);

        theTextView.setText(categories);

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.imageView1);

        // We can set a ImageView like this
        theImageView.setImageResource(R.drawable.dot);

        return theView;

    }
}