package com.styln;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private Context mContext;
    private List<PostTableDO> postList;

    private List<ClothingDO> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostItemsAdapter iAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,numLikes,description;
        public boolean following,liked;
        public ImageView userPic,like,action;
        public Button follow;
        public String id;
        public ImageView userSpace;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.post_name);
            description = (TextView)view.findViewById(R.id.postDescription);
            numLikes = (TextView)view.findViewById(R.id.numLikes);
            like = (ImageView) view.findViewById(R.id.post_like);
            action = (ImageView) view.findViewById(R.id.post_action);
            follow = (Button)view.findViewById(R.id.post_follow);
            id = "";
            userPic = (ImageView)view.findViewById(R.id.userPicture);
            recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
            userSpace = (ImageView)view.findViewById(R.id.post_user_space);
        }
    }


    public PostsAdapter(Context mContext, List<PostTableDO> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_feed, parent, false);

       /* iAdapter = new PostItemsAdapter(parent.getContext(),itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);*/
        return new PostsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PostTableDO post = postList.get(position);

        holder.description.setText(post.getPostDescription());
        holder.numLikes.setText(post.getPostLikes().intValue()+" likes");
        holder.id = post.getPostPoster();

        grabUser task = new grabUser();
        UsersDO loadUser = new UsersDO();
        try {
            task.id = holder.id;
            loadUser = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Glide.with(mContext).load(loadUser.getUserPhoto()).bitmapTransform(new CropCircleTransformation(mContext)).
                thumbnail(0.1f).into(holder.userPic);

        holder.userSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DDD","DASHIUASHISAU");
                String userid = holder.id;
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra("ID", userid);
                mContext.startActivity(intent);
            }
        });

        holder.name.setText(loadUser.getUserName());

        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        holder.following = loadUser.getUserFollower().contains(currUserID);

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
                    holder.follow.setText("Follow");
                } else {
                    da.followSomeone(""+holder.id);
                    holder.follow.setText("Unfollow");
                }
            }
        });


        holder.liked = post.getLikedUser().contains(currUserID);

        if(!holder.liked) {
            holder.like.setImageResource(R.drawable.main_like_0);
        } else {
            holder.like.setImageResource(R.drawable.main_like_1);
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataAction da = new DataAction();
                if(holder.liked){
                    //like post
                    da.likePost(holder.id);
                    holder.like.setImageResource(R.drawable.main_like_0);
                } else {
                    da.likePost(holder.id);
                    holder.like.setImageResource(R.drawable.main_like_1);
                }
            }
        });




     /*   getList task2 = new getList();
        task2.id = holder.id;

        try {
            itemList = task2.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        iAdapter = new PostItemsAdapter(mContext,itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

        iAdapter.notifyDataSetChanged();*/

       /* Glide.with(mContext).load(item.getImage()).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, ItemActivity.class);
                intent.putExtra("SKU",""+holder.sku);
                mContext.startActivity(intent);

            }
        });*/
    }

    private class grabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();
        String id = "";
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO loadUser = new UsersDO();
            String userID = ""+id;
            loadUser = mapper.load(UsersDO.class, userID);
            loadresult = loadUser;
            return loadresult;
        }
    }

    private class getList extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        String id = "";
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            PostTableDO post;
            String postID = ""+id;
            post = mapper.load(PostTableDO.class, postID);
            List<String> tempSet;

            tempSet = post.getPostClothing();
            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                ClothingDO iterator = mapper.load(ClothingDO.class, str);
                loadresult.add(iterator);

            }
            return loadresult;
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
