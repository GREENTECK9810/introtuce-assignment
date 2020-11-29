package com.example.introtuce.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransitionImpl;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.introtuce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class EnrollFragment extends Fragment {

    private ImageView mUserImage;
    private Uri imageUri;
    private TextView mSelectProfilePhoto;
    private EditText mFirstName, mLastName, mAge, mGender, mState, mCountry, mHomeTown, mPhoneNumber;
    private Button mAddButton;
    private String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enroll, container, false);
        initializeViews(view);

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });


        return view;
    }

    private void addUser() {

        if(validate()){

            final HashMap<String, Object> map = new HashMap<>();
            final String uid = FirebaseDatabase.getInstance().getReference().child("Users").push().getKey();

            map.put("firstname", mFirstName.getText().toString());
            map.put("lastname", mLastName.getText().toString());
            map.put("age", mAge.getText().toString());
            map.put("gender", mGender.getText().toString());
            map.put("country", mCountry.getText().toString());
            map.put("state", mState.getText().toString());
            map.put("hometown", mHomeTown.getText().toString());
            map.put("phonenumber", mPhoneNumber.getText().toString());
            map.put("uid", uid);


            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getActivity(), "User added successfully", Toast.LENGTH_LONG);

                    //upload profile photo if selected
                    if (imageUri != null){
                        final StorageReference filepath = FirebaseStorage.getInstance().getReference("Profile Photo")
                                .child(System.currentTimeMillis() + getFileExtension(imageUri));

                        StorageTask uploadTask = filepath.putFile(imageUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isSuccessful()){
                                    throw task.getException();
                                }
                                return filepath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloadUri = task.getResult();
                                imageUrl = downloadUri.toString();
                                map.clear();
                                map.put("imageurl", imageUrl);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(map);
                                Toast.makeText(getActivity(), "User added successfully", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        });
                    }else {
                        Toast.makeText(getActivity(), "User added successfully", Toast.LENGTH_SHORT);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                }
            });




        }

    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mFirstName.getText())){
            Toast.makeText(getActivity(), "First name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mAge.getText())){
            Toast.makeText(getActivity(), "Age is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mGender.getText())){
            Toast.makeText(getActivity(), "Gender is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mCountry.getText())){
            Toast.makeText(getActivity(), "Country is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mHomeTown.getText())){
            Toast.makeText(getActivity(), "Home town is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mState.getText())){
            Toast.makeText(getActivity(), "State is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void selectImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            imageUri = data.getData();
            mUserImage.setImageURI(imageUri);
        }
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
        mAddButton = view.findViewById(R.id.add_user);
    }
}