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
    private final String[] finalfullnames;

    public CustomContactList(Activity context, String[] finalfullnames) {
        super(context, R.layout.contactlist, finalfullnames);
        this.context = context;
        this.finalfullnames = finalfullnames;
    }

    public String[] getFinalfullnames() {
        return finalfullnames;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.contactlist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(finalfullnames[position]);
        return rowView;
    }
}