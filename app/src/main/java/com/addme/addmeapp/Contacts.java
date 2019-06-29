package com.addme.addmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private FirebaseAuth auth;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        auth = FirebaseAuth.getInstance();

        /**
         * Parse the FireBase Realtime Database again, having the userIds
         * and set the fullnames into an ArrayList
         */
        readData(new FirebaseCallBack() {
            @Override
            public void onCallback(final ArrayList<String> userIds) {
                Log.d("Start Testing", userIds.toString());

                final String[] finaluserIds = userIds.toArray(new String[userIds.size()]);
                final ArrayList<String> fullnames = new ArrayList<>();

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (String s: finaluserIds) {
                                if (ds.getKey().equals(s)) {
                                    // here I add the names to the string
                                    fullnames.add(ds.child("fullname").getValue().toString());
                                }
                            }
                        }
                        createListAdapter(fullnames, finaluserIds);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


        });

        // Bottom Navigation View
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_profile:
                        Intent intent1 = new Intent(Contacts.this, MainActivity.class);
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

    /**
     * This method creates my list adapter. Get as a parameter the fullnames as a list
     * and creates a ListView which contains each connection's fullname
     * @param fullnames
     * @param finaluserIds
     */
    private void createListAdapter(ArrayList<String> fullnames, final String[] finaluserIds) {

        //Convert the ArrayList into an Array as String[];
        final String[] finalfullnames = fullnames.toArray(new String[fullnames.size()]);
        CustomContactList listAdapter = new
                CustomContactList(Contacts.this, finalfullnames);
        // The listview should be setted here
        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(Contacts.this, Connection.class);
                String userId = finaluserIds[position];
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    /**
    * This method is implementing the Firebase Callback in order to collect
     * all the userIds needed so as to find the full names of the connections
     */
    private void readData(final FirebaseCallBack firebaseCallBack ) {

        FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
        DatabaseReference myRef1 = myRef.child("connections");

        final ArrayList<String> userIds = new ArrayList<>();


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userId = ds.getKey();
                    userIds.add(userId);

                }
                firebaseCallBack.onCallback(userIds);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());

            }
        };
        myRef1.addListenerForSingleValueEvent(valueEventListener);

    }

    /**
     * Set FirebaseCallBack interface
     */
    private interface FirebaseCallBack {
        void onCallback(ArrayList<String> userIds);
    }

}