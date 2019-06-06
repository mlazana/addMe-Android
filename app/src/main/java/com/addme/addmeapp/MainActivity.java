package com.addme.addmeapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {

    private ImageButton popup;
    private TextView fullname;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_profile:
                        break;
                    case R.id.ic_scan:
                        Intent intent1 = new Intent(MainActivity.this, Scan.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_people:
                        Intent intent2= new Intent(MainActivity.this, Contacts.class);
                        startActivity(intent2);
                        break;

                }


                return false;
            }
        });

        //get current user

        fullname = (TextView) findViewById(R.id.full_name);
        auth = FirebaseAuth.getInstance();
        FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
        //get the data for the current user
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
        setDataToView(ref);


        popup = (ImageButton) findViewById(R.id.popup);
        popup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popupm = new PopupMenu(MainActivity.this, popup);
                //Inflating the Popup using xml file
                popupm.getMenuInflater().inflate(R.menu.popup_menu, popupm.getMenu());

                //registering popup with OnMenuItemClickListener
                popupm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent1);
                        return true;
                    }
                });

                popupm.show();//showing popup menu
            }
        });//closing the setOnClickListener method

    }

    /**
     * This method displays ONLY the fullname
     * @param ref an instance for the data of the current user
     */
    private void setDataToView(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                fullname.setText(u.getFullname());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}