package com.addme.addmeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;

public class Connection extends AppCompatActivity {

    public TextView fullname;
    public Button button_back;
    public TextView number_of_connections;
    public static String[] keys = {
            "facebook",
            "github",
            "instagram",
            "snapchat",
            "twitter",
            "viber",
            "whatsapp"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        Bundle extras = getIntent().getExtras();
        final String userId;
        userId = extras.getString("userId");
        // and get whatever type user account id is

        fullname = (TextView) findViewById(R.id.full_name);
        button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Connection.this, Contacts.class);
                startActivity(intent);

            }

        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("users").child(userId);
        setDataToView(ref1);

        //Social icons list
        final Map<String, Integer> social_icons = new HashMap<String, Integer>();
        social_icons.put("facebook", R.drawable.ic_facebook);
        social_icons.put("github", R.drawable.ic_github_sign);
        social_icons.put("instagram", R.drawable.ic_instagram);
        social_icons.put("snapchat", R.drawable.ic_snapchat);
        social_icons.put("twitter", R.drawable.ic_twitter);
        social_icons.put("viber", R.drawable.ic_viber);
        social_icons.put("whatsapp", R.drawable.ic_whatsapp);


        //Open database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> social_names = new HashMap<String, String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(userId)) {
                        System.out.println("IM HEREEEEEE!!!!!");
                        User u = ds.getValue(User.class);
                        social_names.put("facebook", u.getFacebook());
                        social_names.put("github", u.getGithub());
                        social_names.put("instagram", u.getInstagram());
                        social_names.put("snapchat", u.getSnapchat());
                        social_names.put("twitter", u.getTwitter());
                        social_names.put("viber", u.getViber());
                        social_names.put("whatsapp", u.getWhatsapp());
                        System.out.println(social_names);
                    }
                }

                //The parent layout
                LinearLayout parent = (LinearLayout) findViewById(R.id.social_media_list);
                for (String key : keys) {
                    if (!social_names.get(key).isEmpty()) {
                        RelativeLayout social_item = ConstructSocialItem(social_names.get(key), social_icons.get(key));
                        parent.addView(social_item);
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Retrieve the number of connections
        number_of_connections = (TextView) findViewById(R.id.connections);
        DatabaseReference ref2 = ref.child("connections");
        getNumberOfCon(ref2);

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
                        Intent intent = new Intent(Connection.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.ic_scan:
                        Intent intent1 = new Intent(Connection.this, Scan.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_people:
                        Intent intent2= new Intent(Connection.this, Contacts.class);
                        startActivity(intent2);
                        break;

                }


                return false;
            }
        });



    }

    private RelativeLayout ConstructSocialItem(String name, int social_icon){

        //RelativeLayout
        RelativeLayout social_media = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rlparams.setMargins(dpTopx(20),dpTopx(15),dpTopx(20),0 );
        social_media.setLayoutParams(rlparams);

        //Image View
        ImageView social_media_icon = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(dpTopx(60),dpTopx(60));
        social_media_icon.setLayoutParams(imgparams);
        social_media_icon.setImageResource(social_icon);

        //Relative Layout
        RelativeLayout social_media_name = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rl2params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                dpTopx(50)
        );
        rl2params.setMargins(dpTopx(60),dpTopx(5),0,0);
        social_media_name.setLayoutParams(rl2params);
        social_media_name.setBackgroundResource(R.drawable.rectangle_inside);

        //Text View
        TextView social_name = new TextView(getApplicationContext());
        LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        tv_params.setMargins(0,dpTopx(10),0,dpTopx(10));
        social_name.setLayoutParams(tv_params);
        social_name.setGravity(Gravity.CENTER);
        social_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        social_name.setTextColor(getResources().getColor(R.color.color_text_social));
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext() ,R.font.roboto);
        social_name.setTypeface(typeface);
        social_name.setText(name);

        //construct layout
        social_media_name.addView(social_name);
        social_media.addView(social_media_icon);
        social_media.addView(social_media_name);

        return social_media;
    }

    private int dpTopx(int dp){
        Context context = getApplicationContext();
        float d = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * d);
        return px;
    }

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

    /**
     * This method finds the number of Connections
     * @param ref2
     */
    private void getNumberOfCon (DatabaseReference ref2){

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long x = dataSnapshot.getChildrenCount();
                String text = "Connection";

                //I remove one connection, the dummy connection which is the current user
                x = x - 1;

                if (x == 1){
                    number_of_connections.setText(x + " " + text);
                } else {
                    number_of_connections.setText(x + " " + text + "s");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
