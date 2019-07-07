package com.addme.addmeapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ImageButton settings;
    private TextView fullname;
    private FirebaseAuth auth;
    private FloatingActionButton addButton;
    private TextView number_of_connections;
    public static String[] keys = {
            "facebook",
            "github",
            "instagram",
            "snapchat",
            "twitter",
            "viber",
            "whatsapp"
    };

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        public void run() {

        }
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
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
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

        //Social for edit
        final Map<String, Integer> social_edit = new HashMap<String, Integer>();
        social_edit.put("facebook", R.id.facebook_edit);
        social_edit.put("github", R.id.github_edit);
        social_edit.put("instagram", R.id.instagram_edit);
        social_edit.put("snapchat", R.id.snapchat_edit);
        social_edit.put("twitter", R.id.twitter_edit);
        social_edit.put("viber", R.id.viber_edit);
        social_edit.put("whatsapp", R.id.whatsapp_edit);

        //Open database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Retrieve the social's username for the current user
                final Map<String, String> social_names = new HashMap<String, String>();
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

                ArrayList<String> available_social = new ArrayList<String>();

                //Add every social item which exist in the list of socials
                if (empty == false){
                    for (String key :keys){
                        if(!social_names.get(key).isEmpty()) {
                            RelativeLayout social_item = ConstructSocialItem(social_names.get(key), social_icons.get(key), social_edit.get(key));
                            parent.addView(social_item);
                            available_social.add(key);
                        }
                    }
                }

                //Shows for every social item popupmenu with edit and delete
                for(final String key : available_social) {
                    final RelativeLayout rel_edit = (RelativeLayout) findViewById(social_edit.get(key));
                    rel_edit.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            PopupMenu popupedit = new PopupMenu(MainActivity.this, rel_edit);
                            popupedit.getMenuInflater().inflate(R.menu.popup_menu_myprof, popupedit.getMenu());

                            popupedit.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (item.getItemId() == R.id.delete_social){
                                        DialogInterface.OnClickListener delete_dialog = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        DeleteSocial(key, ref);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        break;
                                                }
                                            }
                                        };

                                        //Build Dialog box for delete confirmation
                                        AlertDialog.Builder delete_popup = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogThemeDelete);
                                        delete_popup.setTitle("Warning!");
                                        delete_popup.setMessage("You are going to delete a social media. Are you sure you want to delete it?");
                                        delete_popup.setPositiveButton("Yes", delete_dialog);
                                        delete_popup.setNegativeButton("No", delete_dialog);
                                        delete_popup.show();

                                    }

                                    if (item.getItemId() == R.id.edit_social){
                                        System.out.println("mpika edo");
                                        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

                                        Bundle parameters = new Bundle();
                                        parameters.putString("social", key);
                                        parameters.putString("old_social_uname", social_names.get(key));
                                        parameters.putInt("social_icon", social_icons.get(key));

                                        FragmentSocialEdit fragment = new FragmentSocialEdit();
                                        fragment.setArguments(parameters);
                                        fragmentTransaction.add(R.id.fragment_container, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }

                                    return false;
                                }
                            });

                            popupedit.show();

                            return false;
                        }
                    });
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

        //This section displays messages from Fragments and Activity
        try {
            Intent intent = getIntent();
            String social;
            //This section displays a textview, when an user adds successfully a social media.
            if (intent.hasExtra("social")) {
                social = intent.getStringExtra("social");
                TextView suc_add_social = findViewById(R.id.succesful_add);
                if (!social.isEmpty()) {
                    suc_add_social.setText("Congratulations! You just added your " + social + " account.");
                }
            }

            String text;

            //This section displays a toast when a user deletes a social media
            if (intent.hasExtra("social_delete")){
                social = intent.getStringExtra("social_delete");
                social = social.substring(0, 1).toUpperCase() + social.substring(1);
                text = "You successfully delete your " + social + " account!";
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }

            //This section displays a toast when a user edits a social media
            if (intent.hasExtra("social_edit")) {
                social = intent.getStringExtra("social_edit");
                text = "You successfully edit your " + social + " account!";
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){

        }

    }

    /**
     * This method deletes the given social
     * @param social
     * @param ref
     */
    private void DeleteSocial (String social, DatabaseReference ref){
        ref.child(social).setValue("");
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("social_delete", social);
        startActivity(intent);
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
     * @param social_id the id for edit
     * @return the relative layout
     */
    private RelativeLayout ConstructSocialItem(String name, int social_icon, int social_id){

        //RelativeLayout
        RelativeLayout social_media = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rlparams.setMargins(dpTopx(20),dpTopx(15),dpTopx(20),0 );

        social_media.setClickable(true);
        social_media.setId(social_id);

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