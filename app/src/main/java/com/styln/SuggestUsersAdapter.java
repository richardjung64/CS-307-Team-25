package com.styln;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SuggestUsersAdapter extends RecyclerView.Adapter<SuggestUsersAdapter.MyViewHolder> {

    private Context mContext;
    private List<UsersDO> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public boolean following;
        public Button follow;
        public ImageView picture,space;
        private String id;



        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            follow = (Button) view.findViewById(R.id.list_follow);
            following = false;
            picture = (ImageView)view.findViewById(R.id.list_picture);
            space = (ImageView)view.findViewById(R.id.userSpace);
            id = "";
        }
    }


    public SuggestUsersAdapter(Context context, List<UsersDO> userList) {
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
        holder.id = user.getUserId();

        Glide.with(mContext).load(user.getUserPhoto()).bitmapTransform(new CropCircleTransformation(mContext)).
                thumbnail(0.1f).into(holder.picture);

        String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        holder.following = user.getUserFollower().contains(currUserID);

        if(!holder.following) {
            holder.follow.setText("Follow");
        } else {
            holder.follow.setText("Unfollow");
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataAction da = new DataAction();
                if(holder.following){
                    da.unfollowSomeone(""+holder.id);
                    Intent intentCurrent = ((Activity) mContext).getIntent();
                    Intent intent= new Intent(mContext, FollowActivity.class);
                    intent.putExtra("KEY",intentCurrent.getStringExtra("KEY"));
                    mContext.startActivity(intent);
                } else {
                    da.followSomeone(""+holder.id);
                    Intent intentCurrent = ((Activity) mContext).getIntent();
                    Intent intent= new Intent(mContext, FollowActivity.class);
                    intent.putExtra("KEY",intentCurrent.getStringExtra("KEY"));
                    mContext.startActivity(intent);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //TODO open other people's profile
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
