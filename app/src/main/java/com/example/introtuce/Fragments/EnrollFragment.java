package com.example.introtuce.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.introtuce.R;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class EnrollFragment extends Fragment {

    private ImageView mUserImage;
    private TextView mSelectProfilePhoto;
    private EditText mFirstName, mLastName, mAge, mGender, mState, mCountry, mHomeTown, mPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enroll, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        mUserImage = view.findViewById(R.id.user_image);
        mSelectProfilePhoto = view.findViewById(R.id.select_profile_photo);
        mFirstName = view.findViewById(R.id.first_name);
        mLastName = view.findViewById(R.id.last_name);
        mAge = view.findViewById(R.id.age);
        mGender = view.findViewById(R.id.gender);
        mCountry = view.findViewById(R.id.country);
        mHomeTown = view.findViewById(R.id.hometown);
        mPhoneNumber = view.findViewById(R.id.phone_number);
        mState = view.findViewById(R.id.state);

    }
}