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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Contacts extends AppCompatActivity {

    private FirebaseAuth auth;
    ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        final ListView listView = (ListView) findViewById(R.id.listview);
        auth = FirebaseAuth.getInstance();

        readData(new FirebaseCallBack() {
            @Override
            public void onCallback(ArrayList<String> userIds) {
                Log.d("Start Testing", userIds.toString());

                final String[] finaluserIds = userIds.toArray(new String[userIds.size()]);


                CustomContactList listAdapter = new
                        CustomContactList(Contacts.this, finaluserIds);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(Contacts.this, "You Clicked at " +finaluserIds[+ position], Toast.LENGTH_SHORT).show();
                    }
                });


//                FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
//                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
//
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            String userId = ds.getKey();
//
//                            DatabaseReference connection = FirebaseDatabase.getInstance().getReference("users").child(userId);
//                            connection.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    String fullname;
//                                    User u = dataSnapshot.getValue(User.class);
//                                    fullname = u.getFullname();
//
//                                    fullNames.add(fullname);
//                                    Log.v("Edw eisai", fullNames.toString());
//
//                                    createListView(fullNames);
//
//                                }
//
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//
//                            });
//                            Log.d("userId", userId);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });


            }


        });




        //TODO: Each time it shows the last user
        //TODO: There should be a list view that shows every connection

        /**
         * This ValueEventListener saves into an array, the userIDs
         * corresponding to the connections of the  current user
         **/







        //get the data for the current user


//        ListView myListView = (ListView) findViewById(R.id.contact_list);


//
//        // Read from the database
//        myRef1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
//
//                for (String "userId": value.keySet()) {
//
//                    Map<String, Object> currentConnection = (Map<String, Object>) value.get("userId");
//                }
//                Log.d("ion", "Value is: " + value);
//                String id = (String) value.get("userId");
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("ion", "Failed to read value.", error.toException());
//            }
//        });









        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);


        // Bottom Navigation View
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
//    public void createListView(ArrayList<String> fullNames) {
//
//
//        listview.setAdapter(new CustomContactlist(this, fullNames));
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//            }
//        });
//    }

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

    private interface FirebaseCallBack {
        void onCallback(ArrayList<String> userIds);
    }

}