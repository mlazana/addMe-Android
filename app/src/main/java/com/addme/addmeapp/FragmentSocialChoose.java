package com.addme.addmeapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.style.ScaleXSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;


public class FragmentSocialChoose extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static String[] keys = {
            "facebook",
            "github",
            "instagram",
            "snapchat",
            "twitter",
            "viber",
            "whatsapp"
    };

    public FragmentSocialChoose() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Requirements to open database
        FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());

        //social icons
        final Map<String, Integer> social_icons = new HashMap<String, Integer>();
        social_icons.put("facebook", R.drawable.ic_facebook);
        social_icons.put("github", R.drawable.ic_github_sign);
        social_icons.put("instagram", R.drawable.ic_instagram);
        social_icons.put("snapchat", R.drawable.ic_snapchat);
        social_icons.put("twitter", R.drawable.ic_twitter);
        social_icons.put("viber", R.drawable.ic_viber);
        social_icons.put("whatsapp", R.drawable.ic_whatsapp);

        //social button ids
        final Map<String, Integer> social_buttons = new HashMap<String, Integer>();
        social_buttons.put("facebook", R.id.facebook_button);
        social_buttons.put("github", R.id.github_button);
        social_buttons.put("instagram", R.id.instagram_button);
        social_buttons.put("snapchat", R.id.snapchat_button);
        social_buttons.put("twitter", R.id.twitter_button);
        social_buttons.put("viber", R.id.viber_button);
        social_buttons.put("whatsapp", R.id.whatsapp_button);

        //Open Database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Retrieve social names
                Map<String, String> social_names = new HashMap<String, String>();
                User u = dataSnapshot.getValue(User.class);
                social_names.put("facebook", u.getFacebook());
                social_names.put("github", u.getGithub());
                social_names.put("instagram", u.getInstagram());
                social_names.put("snapchat", u.getSnapchat());
                social_names.put("twitter", u.getTwitter());
                social_names.put("viber", u.getViber());
                social_names.put("whatsapp", u.getWhatsapp());

                //Relative Layout for text
                RelativeLayout rltext;
                //Parent Relative Layout
                RelativeLayout parent = (RelativeLayout) getView().findViewById(R.id.fragment_choose_container);

                //Check if the social names is full
                Boolean full = isFullListOfSocial(social_names);

                if (full == true){
                    rltext = text(R.color.colorPrimary, "You have been add all the available social media");
                    parent.addView(rltext);
                }


                ArrayList<String> available_social = new ArrayList<String>();

                if (full == false){
                    available_social = available_socials(social_names);
                    rltext = text(R.color.black, "You can choose among the accounts and features you haven't added yet");
                    parent.addView(rltext);
                    ConstructListOfAvailableSocial(available_social, social_icons, social_buttons);
                }

                for(final String key : available_social) {
                    Button socialbutton = (Button) getView().findViewById(social_buttons.get(key));
                    socialbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle parameters = new Bundle();
                            parameters.putString("social", key);
                            parameters.putInt("icon", social_icons.get(key));

                            android.support.v4.app.FragmentManager fm = getFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            FragmentSocialEnter fragment = new FragmentSocialEnter();
                            fragment.setArguments(parameters);
                            fragmentTransaction.add(R.id.fragment_choose, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social_choose, container, false);
    }

    /**
     * This method finds the available social media (That user hasn't add)
     * @param social_names
     * @return an array with values the name of socials that are available to add
     */
    private ArrayList<String> available_socials(Map<String, String> social_names){
        ArrayList<String> socials = new ArrayList<String>();

        for(String key : keys){
            if(social_names.get(key).isEmpty()){
                socials.add(key);
            }
        }

        return socials;
    }

    /**
     * This method constructs the list with all available social media to choose
     * @param available_social
     * @param social_icons
     * @param social_buttons
     */
    private void ConstructListOfAvailableSocial(ArrayList<String> available_social, Map<String, Integer> social_icons, Map<String, Integer> social_buttons){
        RelativeLayout avail_social = (RelativeLayout) getView().findViewById(R.id.social_media_to_choose);
        LinearLayout verticalLinear = VerticalLinear();
        LinearLayout horizontalLinear = HorizontalLinear();
        Button social_buttton;
        int general_counter = 0;
        int horizontal_counter = 0;

        for (String key : available_social){

            general_counter ++;
            horizontal_counter ++;

            if(horizontal_counter % 2 == 0) {
                social_buttton = (Button) SocialButton(dpTopx(35), dpTopx(35), social_icons.get(key), social_buttons.get(key));
            } else {
                social_buttton = (Button) SocialButton(0,0,social_icons.get(key), social_buttons.get(key));
            }

            horizontalLinear.addView(social_buttton);

            if (general_counter % 3 == 0 || general_counter == available_social.size()){
                horizontal_counter = 0;
                verticalLinear.addView(horizontalLinear);
                horizontalLinear = HorizontalLinear();

            }
        }
        avail_social.addView(verticalLinear);
    }

    /**
     * This method checks if the user has been added all the available socials
     * @param social_names
     * @return
     */
    private boolean isFullListOfSocial(Map<String, String> social_names){
        Boolean full = true;
        for (String key : keys){
            if(social_names.get(key).isEmpty()){
                full = false;
                break;
            }
        }

        return full;
    }

    /**
     * This method constructs a relative layout that contains a text
     * @param color The color of the text
     * @param txt The given text
     * @return the relative layout
     */
    private RelativeLayout text (int color, String txt) {

        RelativeLayout rl = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        tv_params.setMargins(0, dpTopx(15),0,0);
        tv.setLayoutParams(tv_params);
        tv.setPadding(dpTopx(10), 0, dpTopx(10), 0);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(color));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        tv.setText(txt);

        rl.addView(tv);

        return rl;
    }

    /**
     * This method constructs the first linear layout vertically
     * @return the linear layout
     */
    private LinearLayout VerticalLinear (){
        LinearLayout lr = new LinearLayout(getContext());
        LinearLayout.LayoutParams rlparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        lr.setOrientation(LinearLayout.VERTICAL);
        lr.setLayoutParams(rlparams);

        return lr;
    }

    /**
     * This method constructs the linear layout horizontally
     * @return a LinearLayout
     */
    private LinearLayout HorizontalLinear(){
        LinearLayout lr = new LinearLayout(getContext());
        LinearLayout.LayoutParams lrparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        lrparams.setMargins(0, dpTopx(15), 0, 0 );
        lr.setLayoutParams(lrparams);
        lr.setOrientation(LinearLayout.HORIZONTAL);
        lr.setGravity(Gravity.CENTER);

        return lr;
    }

    /**
     * This method constructs buttons of social to choose
     * @param margin_right
     * @param margin_left
     * @param icon
     * @return the button
     */
    private Button SocialButton(int margin_right, int margin_left, int icon, int button_id){
        Button but = new Button(getContext());
        RelativeLayout.LayoutParams butparams = new RelativeLayout.LayoutParams(dpTopx(60), dpTopx(60));
        butparams.setMargins(margin_left,0,margin_right, 0);
        but.setAlpha(0.5f);
        but.setClickable(true);
        but.setBackgroundResource(icon);
        but.setId(button_id);
        but.setLayoutParams(butparams);
        return but;
    }

    /**
     * This method convert dp to px
     * @param dp
     * @return the px
     */
    private int dpTopx(int dp){
        Context context = getContext();
        float d = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * d);
        return px;
    }


}
