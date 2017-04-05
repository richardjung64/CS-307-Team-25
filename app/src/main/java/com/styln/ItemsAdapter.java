package com.styln;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameItem);
            image = (ImageView) view.findViewById(R.id.imageItem);
        }
    }


    public ItemsAdapter(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);
        return new ItemsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Item item  = itemList.get(position);
        holder.name.setText(item.getName());
        Glide.with(mContext).load(item.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
