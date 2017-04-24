package com.styln;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.MyViewHolder> {

    private Context mContext;
    private List<PostTableDO> postList;

    private List<ClothingDO> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfilePostsItemsAdapter iAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,numLikes,description,date;
        public String Uid,Pid;
        public ImageView space;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.post_name);
            description = (TextView)view.findViewById(R.id.postDescription);
            numLikes = (TextView)view.findViewById(R.id.numLikes);
            Uid = "";
            Pid = "";
            recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
            space = (ImageView)view.findViewById(R.id.post_page);
            date = (TextView)view.findViewById(R.id.postDate);
        }
    }


    public ProfilePostsAdapter(Context mContext, List<PostTableDO> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_profile, parent, false);

        return new ProfilePostsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PostTableDO post = postList.get(position);

        holder.description.setText(post.getPostDescription());
        holder.numLikes.setText(post.getPostLikes().intValue()+" likes");
        holder.date.setText(post.getPostDate());
        holder.Uid = post.getPostPoster();
        holder.Pid = post.getUserId();

        grabUser task = new grabUser();
        UsersDO loadUser = new UsersDO();
        try {
            task.id = holder.Uid;
            loadUser = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        holder.space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.Pid;
                Intent intent = new Intent(mContext, PostPageActivity.class);
                intent.putExtra("ID", id);
                mContext.startActivity(intent);
            }
        });

       getList task2 = new getList();
        task2.id = holder.Pid;

        try {
            itemList = task2.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        iAdapter = new ProfilePostsItemsAdapter(mContext,itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

        iAdapter.notifyDataSetChanged();

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

            if(post.getPostClothing() == null){
                post.setPostClothing(new ArrayList<String>());
            }
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
