package com.addme.addmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Contacts extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView full_name;
    DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);


        final TextView full_name = (TextView) findViewById(R.id.full_name);
        auth = FirebaseAuth.getInstance();
        FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
        //get the data for the current user
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
        DatabaseReference myRef1 = myRef.child("connections");

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

        final ArrayList<String> connectionUserIdArray = new ArrayList<String>();
        /**
         * This ValueEventListener saves into an array, the userIDs
         * corresponding to the connections of the  current user
         **/
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userId = ds.child("userId").getValue(String.class);
                    connectionUserIdArray.add(userId);
                    Log.d("userId", userId);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());

            }
        };
        myRef1.addListenerForSingleValueEvent(valueEventListener);

        //TODO: Here we have a problem... the size of the array that is returned is equal to zero
        Log.v("Hellooooo!", String.valueOf(connectionUserIdArray.size()));

        for (int i = 0; i < connectionUserIdArray.size(); i++) {
            DatabaseReference connection = FirebaseDatabase.getInstance().getReference("users").child(connectionUserIdArray.get(i));

            connection.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    full_name.setText(u.getFullname());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

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


}