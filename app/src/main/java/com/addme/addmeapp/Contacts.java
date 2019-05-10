package com.addme.addmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Contacts extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        TextView title = (TextView) findViewById(R.id.contactsTitle);
        title.setText("This is Contacts");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_profile:
                        Intent intent1 = new Intent(Contacts.this, Profile.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_scan:
                        Intent intent0 = new Intent(Contacts.this, Scan.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_people:
                        break;

                }


                return false;
            }
        });
    }
}