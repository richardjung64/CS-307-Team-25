package com.styln;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by shanu on 4/27/17.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {
    private Context mContext;
    private List<UsersDO> users;
    private List<ClothingDO> clothes;
    private boolean isUser = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView space;
        public String id;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.search_result);
            space = (ImageView) view.findViewById(R.id._itemSpace);
            id = "";
        }
    }


    public SearchListAdapter(Context mContext, List<ClothingDO> clothes, List<UsersDO> users) {
        this.mContext = mContext;
        if (clothes != null) {
            this.clothes = clothes;
            isUser = false;
        }
        if (users != null) {
            this.users = users;
            isUser = true;
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search, parent, false);
        return new SearchListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SearchListAdapter.MyViewHolder holder, int position) {
        if (isUser) {
            UsersDO user = users.get(position);
            holder.name.setText(user.getUserName());
            holder.id = user.getUserId();
            holder.space.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("HHAHA","HAHHA opening");
                    Intent intent= new Intent(mContext, OthersActivity.class);
                    intent.putExtra("ID",""+holder.id);
                    mContext.startActivity(intent);
                }
            });
        }
        else {
            ClothingDO item = clothes.get(position);
            holder.name.setText(item.getUserId());
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
//        holder.brand.setText(item.getClothingBrand());
//        Glide.with(mContext).load(item.getClothingPhotoLink()).into(holder.image);
//        holder.space.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("HHAHA","HAHHA opening");
//                Intent intent= new Intent(mContext, ItemActivity.class);
//                intent.putExtra("SKU",""+holder.name.getText());
//                mContext.startActivity(intent);
//            }
//        });
//        holder.action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //remove
//                Intent intentCurrent = ((Activity) mContext).getIntent();
//
//                DataAction da = new DataAction();
//                if(intentCurrent.getStringExtra("KEY").equals("wardrobe")){
//                    da.ownClothing(""+holder.name.getText());
//                } else {
//                    da.wishClothing(""+holder.name.getText());
//                }
//
//                Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(mContext, CollectionActivity.class);
//                intent.putExtra("ID",intentCurrent.getStringExtra("ID"));
//                intent.putExtra("KEY",intentCurrent.getStringExtra("KEY"));
//                mContext.startActivity(intent);
//            }
//        });
    }

    public int getItemCount() {
        if (isUser)
            return users.size();
        return clothes.size();
    }
}
