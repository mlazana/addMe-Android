package com.addme.addmeapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import android.support.v4.app.FragmentManager;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ImageButton settings;
    private TextView fullname;
    private FirebaseAuth auth;
    private TextView facebook;
    private FloatingActionButton addButton;
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


        //Social Choose Fragment
        settings = (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);

            }

        });



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

                //Retrieve the social's username for the current user
                Map<String, String> social_names = new HashMap<String, String>();
                User u = dataSnapshot.getValue(User.class);
                social_names.put("facebook", u.getFacebook());
                social_names.put("github", u.getGithub());
                social_names.put("instagram", u.getInstagram());
                social_names.put("snapchat", u.getSnapchat());
                social_names.put("twitter", u.getTwitter());
                social_names.put("viber", u.getViber());
                social_names.put("whatsapp", u.getWhatsapp());

                //Check if the social names list is empty
                Boolean empty = isEmptyListOfSocial(social_names);

                //The parent layout
                LinearLayout parent = (LinearLayout) findViewById(R.id.social_media_list);

                //Add to the parent layout the layer when the list of social is empty
                if (empty == true){
                    parent.addView(EmptySocialList());
                }

                //Add every social item which exist in the list of socials
                if (empty == false){
                    for (String key :keys){
                        if(!social_names.get(key).isEmpty()) {
                            RelativeLayout social_item = ConstructSocialItem(social_names.get(key), social_icons.get(key));
                            parent.addView(social_item);
                        }
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Social Choose Fragment
        addButton = (FloatingActionButton) findViewById(R.id.AddActionButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                FragmentSocialChoose fragment = new FragmentSocialChoose();
                fragmentTransaction.add(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });


        //This section display a textview, when an user adds successfully a social media.
        try {
            Intent intent = getIntent();
            String social = intent.getStringExtra("social");
            TextView suc_add_social = findViewById(R.id.succesful_add);
            if (!social.isEmpty()) {
                suc_add_social.setText("Congratulations! You just added your " + social + " account.");
            }
        } catch (Exception e){

        }

    }

    /**
     * This method checks if the user has not added social
     * You can only call it from onDataChange
     * @return true if the user has not added any social, and false otherwise
     */
    private boolean isEmptyListOfSocial(Map<String, String> social_names){

        Boolean empty = true;
        for (String key : keys){
            if(!social_names.get(key).isEmpty()){
                empty = false;
                break;
            }
        }

        return empty;
    }

    /**
     * This method constructs a layout for every item about every social media
     * @param name the name for the social media
     * @param social_icon the social media icon
     * @return the relative layout
     */
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

    /**
     * This method constructs a layout, for the condition that user has not added social accounts
     * @return a relative layout
     */
    private RelativeLayout EmptySocialList(){

        //Relative Layout
        RelativeLayout rl = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        //TextView
        TextView textview = new TextView(getApplicationContext());
        LinearLayout.LayoutParams textview_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        textview_params.setMargins(0,dpTopx(50),0,0);
        textview.setLayoutParams(textview_params);
        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        textview.setTextColor(getResources().getColor(R.color.colorPrimary));
        textview.setText("Click the plus button on the right top to add your first social media");
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto);
        textview.setTypeface(typeface);

        rl.addView(textview);

        return rl;
    }

    /**
     * This method convert dp to pixels.
     * @param dp
     * @return the pixels
     */
    private int dpTopx(int dp){
        Context context = getApplicationContext();
        float d = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * d);
        return px;
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