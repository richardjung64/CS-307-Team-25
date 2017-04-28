package com.styln;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by shanu on 4/27/17.
 */

public class SearchListItemsAdapter extends RecyclerView.Adapter<SearchListItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ClothingDO> clothes;
    private boolean isUser = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView space,image;
        public String id;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemName);
            space = (ImageView) view.findViewById(R.id.itemSpace);
            image = (ImageView) view.findViewById(R.id.itemImage);
            id = "";
        }
    }


    public SearchListItemsAdapter(Context mContext, List<ClothingDO> clothes) {
        this.mContext = mContext;
        if (clothes != null) {
            this.clothes = clothes;
            isUser = false;
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_search_row, parent, false);
        return new SearchListItemsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SearchListItemsAdapter.MyViewHolder holder, int position) {
            ClothingDO item = clothes.get(position);
            holder.name.setText(item.getUserId());

                Log.d("mContext",mContext.toString());
        Log.d("link",item.getClothingPhotoLink());
        Log.d("holder",holder.image.toString());


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
    }

    public int getItemCount() {
        return clothes.size();
    }
}