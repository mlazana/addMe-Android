package com.addme.addmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        TextView title = (TextView) findViewById(R.id.profileTitle);
        title.setText("This is Profile");
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
                        Intent intent0 = new Intent(Profile.this, Scan.class);
                        startActivity(intent0);
                        break;
                    case R.id.ic_people:
                        Intent intent1 = new Intent(Profile.this, Contacts.class);
                        startActivity(intent1);
                        break;

                }


                return false;
            }
        });
    }
}