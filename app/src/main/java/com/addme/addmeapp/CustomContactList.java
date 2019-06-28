package com.addme.addmeapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomContactList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] finaluserId;

    public CustomContactList(Activity context, String[] finaluserId) {
        super(context, R.layout.contactlist, finaluserId);
        this.context = context;
        this.finaluserId = finaluserId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.contactlist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(finaluserId[position]);
        return rowView;
    }
}