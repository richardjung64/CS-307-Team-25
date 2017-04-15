package com.styln;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
            follow = (Button) view.findViewById(R.id.list_follow);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UsersDO user = userList.get(position);
        holder.name.setText(user.getUserName());
        holder.following = false;

        if(holder.following == false) {
            holder.following = true;
            holder.follow.setText("Follow");
        } else {
            holder.following = false;
            holder.follow.setText("Unfollow");
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.following){
                    unfollow(holder.follow);
                    Intent intentCurrent = ((Activity) mContext).getIntent();
                    Intent intent= new Intent(mContext, FollowActivity.class);
                    intent.putExtra("KEY",intentCurrent.getStringExtra("KEY"));
                    mContext.startActivity(intent);
                } else {
                    follow(holder.follow);
                    Intent intentCurrent = ((Activity) mContext).getIntent();
                    Intent intent= new Intent(mContext, FollowActivity.class);
                    intent.putExtra("KEY",intentCurrent.getStringExtra("KEY"));
                    mContext.startActivity(intent);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    private void follow(View view){
        Log.d("DD","DD");
        Button followButton = (Button)view.findViewById(R.id.list_follow);
        followButton.setText("Unfollow");
    }
    private void unfollow(View view){
        Log.d("DD","DD");
        Button followButton = (Button)view.findViewById(R.id.list_follow);
        followButton.setText("Follow");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
