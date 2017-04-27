package com.styln;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.models.nosql.ClothingDO;
import com.bumptech.glide.Glide;

import java.util.Collection;
import java.util.List;

public class CollectionItemsAdapter extends RecyclerView.Adapter<CollectionItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ClothingDO> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,brand;
        public ImageView image,action;
        public int sku;
        public ImageView space;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemName);
            brand = (TextView) view.findViewById(R.id.itemBrand);
            image = (ImageView) view.findViewById(R.id.itemImage);
            action = (ImageView) view.findViewById(R.id.itemAction);
            space = (ImageView) view.findViewById(R.id.itemSpace);
        }
    }


    public CollectionItemsAdapter(Context mContext, List<ClothingDO> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);
        return new CollectionItemsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ClothingDO item  = itemList.get( position);
        holder.name.setText(item.getUserId());
        holder.brand.setText(item.getClothingBrand());
        Glide.with(mContext).load(item.getClothingPhotoLink()).into(holder.image);
        holder.space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HHAHA","HAHHA opening");
                Intent intent= new Intent(mContext, ItemActivity.class);
                intent.putExtra("SKU",""+holder.name.getText());
                mContext.startActivity(intent);
            }
        });
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove
                Intent intentCurrent = ((Activity) mContext).getIntent();

                DataAction da = new DataAction();
                if(intentCurrent.getStringExtra("KEY").equals("wardrobe")){
                    da.ownClothing(""+holder.name.getText());
                } else {
                    da.wishClothing(""+holder.name.getText());
                }

                Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, CollectionActivity.class);
                intent.putExtra("ID",intentCurrent.getStringExtra("ID"));
                intent.putExtra("KEY",intentCurrent.getStringExtra("KEY"));
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
