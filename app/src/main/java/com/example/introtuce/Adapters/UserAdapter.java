package com.example.introtuce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.introtuce.Classes.User;
import com.example.introtuce.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    List<User> mUsersList;
    Context mContext;

    public UserAdapter(List<User> usersList, Context context) {
        this.mUsersList = usersList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        User user = mUsersList.get(position);

        holder.name.setText(user.getFirstname());
        holder.details.setText(user.getGender() + " | " + user.getAge() + " | " + user.getState());
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView userImage, delete;
        private TextView name, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            delete = itemView.findViewById(R.id.delete);
            name = itemView.findViewById(R.id.name);
            details = itemView.findViewById(R.id.details);

        }
    }

}
