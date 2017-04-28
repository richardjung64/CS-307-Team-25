package com.styln;


import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.models.nosql.ClothingDO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostActivityItemsAdapter extends RecyclerView.Adapter<PostActivityItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ClothingDO> itemList;
    public List<String> postList = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,brand;
        public ImageView image;
        public String id;
        public CheckBox checkBox;
        public ImageView space;
        public boolean checked;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemName);
            brand = (TextView) view.findViewById(R.id.itemBrand);
            image = (ImageView) view.findViewById(R.id.itemImage);
            checkBox = (CheckBox)view.findViewById(R.id.checkBox);
            space = (ImageView)view.findViewById(R.id.itemSpace);
        }
    }


    public PostActivityItemsAdapter(Context mContext, List<ClothingDO> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_post, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ClothingDO item  = itemList.get(position);
        holder.name.setText(item.getUserId());
        holder.brand.setText(item.getClothingBrand());
        holder.id=item.getUserId();
        Glide.with(mContext).load(item.getClothingPhotoLink()).into(holder.image);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checked){
                    holder.checked = false;
                    holder.checkBox.setChecked(false);
                    postList.remove(holder.id);
                } else {
                    holder.checked = true;
                    holder.checkBox.setChecked(true);
                    postList.add(holder.id);
                }
            }
        });
        holder.space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dd","vdvdvvdvd");
                if(holder.checked){
                    holder.checked = false;
                    holder.checkBox.setChecked(false);
                    postList.remove(holder.id);
                } else {
                    holder.checked = true;
                    holder.checkBox.setChecked(true);
                    postList.add(holder.id);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
