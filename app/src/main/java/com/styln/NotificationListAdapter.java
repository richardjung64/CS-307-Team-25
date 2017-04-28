package com.styln;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanu on 4/27/17.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {
    private Context mContext;
    private PostTableDO post;
    private List<String> users_who_liked;
    private List<UsersDO> users_list;
    private String curr_userId;
    private UsersDO curr_user;
    private double new_followers_count;
    private List<String> followers;
    private static final String LOG_TAG = NotificationListAdapter.class.getSimpleName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.notify_list);
        }
    }


    public NotificationListAdapter(Context mContext, PostTableDO post, UsersDO user) {
        this.mContext = mContext;
        users_who_liked = new ArrayList<>();
        if (post != null) {
            this.post = post;
            users_who_liked = post.getLikedUser();
        }
        users_list = new ArrayList<>();
        if (user != null) {
            this.curr_user = user;
            Log.d(LOG_TAG, "MY USERNAME " + curr_user.getUserName());
            this.new_followers_count = user.getNew_followers();
            Log.d(LOG_TAG, "MY NEW FOLLOWERS " + new_followers_count);
            followers = user.getUserFollower();
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notify_activity_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final NotificationListAdapter.MyViewHolder holder, int position) {

        Log.d(LOG_TAG, "List size: " + users_who_liked.size());
        for (int i = 0 ; i < users_who_liked.size(); i++) {
            curr_userId = users_who_liked.get(i);
            Log.d(LOG_TAG, "USER: " + curr_userId);
            GetUsers users = new GetUsers();
            UsersDO curr_user = null;
            try {
                curr_user = users.execute().get();
                Log.d(LOG_TAG, "USER NAME: " + curr_user.getUserName());
            }
            catch (Exception e) {
                Log.e(LOG_TAG, "Failed to find user");
            }
            if (curr_user != null)
                users_list.add(curr_user);
        }
        if (users_list.size() > 0) {
            UsersDO user = users_list.get(position);
            String setText = user.getUserName() + " liked your post";
            Log.d(LOG_TAG, "TO DISPLAY: " + setText);
            holder.name.setText(setText);
        }
        Log.d(LOG_TAG, "new followers: " + new_followers_count);
        if (new_followers_count > 0) {
            users_list = new ArrayList<>();
            List <String> new_followers = new ArrayList<>();
            for (int i = 0 ; i < new_followers_count; i++)
                new_followers.add(followers.get(followers.size() - i - 1));

            for (int i = 0 ; i < new_followers.size(); i++) {
                curr_userId = new_followers.get(i);
                GetUsers users = new GetUsers();
                UsersDO curr_user = null;
                try {
                    curr_user = users.execute().get();
                    Log.d(LOG_TAG, "USER NAME FOLLOW: " + curr_user.getUserName());
                }
                catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to find user");
                }
                if (curr_user != null)
                    users_list.add(curr_user);
            }
            UsersDO _user = users_list.get(position);
            String _setText = _user.getUserName() + " is following you";
            Log.d(LOG_TAG, "TO DISPLAY: " + _setText);
            holder.name.setText(_setText);
        }
        //holder.name.setText();
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

    private class GetUsers extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;

        public String getId () {
            id = curr_userId;
            return id;
        }
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO loadUser = mapper.load(UsersDO.class, getId());
            if (loadUser == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return loadUser;
        }
    }

    public int getItemCount() {
        if (users_who_liked.size() > 0)
            return users_who_liked.size();
        return (int)(new_followers_count);
    }
}