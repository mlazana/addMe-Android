package com.addme.addmeapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;



public class Scan extends AppCompatActivity {

    private Button scanButton;
    private Button myQrButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_profile:
                        Intent intent0 = new Intent(Scan.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_scan:
                        break;

                    case R.id.ic_people:

                        Intent intent1 = new Intent(Scan.this, Contacts.class);
                        startActivity(intent1);
                        break;

                }


                return false;
            }
        });

        scanButton = (Button) findViewById(R.id.scan_btn);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanQrCode();
            }
        });

        myQrButton = (Button) findViewById(R.id.myqr_btn);
        myQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyQrCode();
            }
        });
    }
    private void openMyQrCode() {
        Intent intent = new Intent(this, MyQrCode.class);
        startActivity(intent);
    }

    private void openScanQrCode() {
        Intent intent = new Intent(this, ScanQrCode.class);
        startActivity(intent);
    }
}