package com.styln;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.models.nosql.ClothingDO;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomePostsItemsAdapter extends RecyclerView.Adapter<HomePostsItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ClothingDO> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,brand;
        public ImageView image;
        public String sku;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemName);
            image = (ImageView) view.findViewById(R.id.itemImage);
            sku = "";
        }
    }


    public HomePostsItemsAdapter(Context mContext, List<ClothingDO> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }


    @Override
    public HomePostsItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_row, parent, false);
        return new HomePostsItemsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final HomePostsItemsAdapter.MyViewHolder holder, int position) {
        ClothingDO item  = itemList.get(position);
        holder.name.setText(item.getUserId());
        holder.sku = item.getUserId();
        Glide.with(mContext).load(item.getClothingPhotoLink()).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, ItemActivity.class);
                intent.putExtra("SKU",holder.sku);
                mContext.startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
