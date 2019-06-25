package com.addme.addmeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentSocialEnter extends Fragment {

    private EditText inputUsername;
    private Button btnSubmit;

    public FragmentSocialEnter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String social = getArguments().getString("social");
        int icon = getArguments().getInt("icon");

        View rootview = inflater.inflate(R.layout.fragment_social_enter, container, false);

        ImageView social_icon = (ImageView) rootview.findViewById(R.id.social_media_icon);
        social_icon.setImageResource(icon);

        inputUsername = (EditText) rootview.findViewById(R.id.social_media_uname);
        btnSubmit = (Button) rootview.findViewById(R.id.submit_button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
                System.out.println(username);
                ref.child(social).setValue(username);

                String cap = social.substring(0,1 ).toUpperCase() + social.substring(1);

                Intent intent = new Intent();
                intent.putExtra("social", cap);
                intent.setClass(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);


            }
        });

        // Inflate the layout for this fragment
        return rootview;
    }
}
