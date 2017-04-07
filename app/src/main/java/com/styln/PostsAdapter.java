package com.styln;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> postList;

    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostItemsAdapter iAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView like,action;
        public Button follow;
        public int id;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.post_name);
            like = (ImageView) view.findViewById(R.id.post_like);
            action = (ImageView) view.findViewById(R.id.post_action);
            follow = (Button)view.findViewById(R.id.post_follow);
            id = -1;
            recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        }
    }


    public PostsAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_feed, parent, false);

        iAdapter = new PostItemsAdapter(parent.getContext(),itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);
        return new PostsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.name.setText("");
        
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



    @Override
    public int getItemCount() {
        return postList.size();
    }
}
