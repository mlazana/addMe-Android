package com.addme.addmeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FragmentSocialEdit extends Fragment {

    private EditText edit_name;
    private Button btnUpdate;

    public FragmentSocialEdit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String old_social_uname = getArguments().getString("old_social_uname");
        int icon = getArguments().getInt("social_icon");
        final String social = getArguments().getString("social");

        //Arguments from Main Activity
        View rootview = inflater.inflate(R.layout.fragment_social_edit, container, false);

        ImageView social_icon = (ImageView) rootview.findViewById(R.id.social_media_icon);
        social_icon.setImageResource(icon);

        edit_name = (EditText) rootview.findViewById(R.id.social_media_uname);
        edit_name.setHint(old_social_uname);

        btnUpdate = (Button) rootview.findViewById(R.id.update_button);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_username = edit_name.getText().toString();

                if (TextUtils.isEmpty(new_username)){
                    Toast.makeText(getContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                if (old_social_uname.equals(new_username)){
                    DialogInterface.OnClickListener edit_dialog = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    getActivity().startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    //Build Dialog box if the user enters the same username
                    AlertDialog.Builder edit_popup = new AlertDialog.Builder(getActivity(), R.style.AlertDialogThemeDelete);
                    edit_popup.setTitle("Warning!");
                    edit_popup.setMessage("You have enter the same Username. Do you want to continue?");
                    edit_popup.setPositiveButton("Yes", edit_dialog);
                    edit_popup.setNegativeButton("No", edit_dialog);
                    edit_popup.show();
                } else {

                    //Insert username in database
                    FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(use.getUid());
                    ref.child(social).setValue(new_username);

                    //The name of social that the user add
                    String cap = social.substring(0, 1).toUpperCase() + social.substring(1);

                    intent.putExtra("social_edit", cap);
                    getActivity().startActivity(intent);
                }

            }
        });

        // Inflate the layout for this fragment
        return rootview;
    }

}