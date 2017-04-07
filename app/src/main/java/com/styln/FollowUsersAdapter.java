package com.styln;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.models.nosql.UsersDO;

import java.util.List;

public class FollowUsersAdapter extends RecyclerView.Adapter<FollowUsersAdapter.MyViewHolder> {

    private Context mContext;
    private List<UsersDO> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public boolean following;
        public Button follow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            follow = (Button) view.findViewById(R.id.follow);
            following = false;
        }
    }


    public FollowUsersAdapter(Context context, List<UsersDO> userList) {
        mContext = context;
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UsersDO user = userList.get(position);
        holder.name.setText(user.getUserName());
        if(holder.following == false) {
            holder.follow.setText("Follow");
        } else {
            holder.follow.setText("Unfollow");

        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
