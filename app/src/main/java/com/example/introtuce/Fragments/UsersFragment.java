package com.example.introtuce.Fragments;

import android.icu.lang.UCharacter;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.introtuce.Adapters.UserAdapter;
import com.example.introtuce.Classes.User;
import com.example.introtuce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// this fragment shows the list of all the enrolled users

public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> users;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progress_circular);
        users = new ArrayList<>();

        userAdapter = new UserAdapter(users, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(userAdapter);
        initList();

        userAdapter.setOnItemClickListener(new UserAdapter.onItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeUser(users.get(position).getUid());
                users.remove(position);
                userAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    //this method removes the user from the database
    private void removeUser(String uid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Item removed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //this method initialize the users list
    private void initList() {
        progressBar.setVisibility(View.VISIBLE);

        //this firebase method called whenever the value is changed in the database
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                users.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                    }
                    //reverse the users list to display the last added user on the top of the recycler view
                    Collections.reverse(users);
                    userAdapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}