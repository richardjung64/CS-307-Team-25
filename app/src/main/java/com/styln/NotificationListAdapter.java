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
    private String curr_clothId;
    private UsersDO curr_user;
    private String curr_postId;
    private double new_followers_count;
    private List<String> followers;
    private PaginatedScanList<ClothingDO> clothes;
    private List<String> wishList_clothes;
    private List<String> postsLiked;
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
            wishList_clothes = user.getUserWishList();
            postsLiked = user.getPosts_liked();
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
        if (postsLiked != null) {
            for (int j = 0; j < postsLiked.size(); j++) {
                GetPost getPost = new GetPost();
                PostTableDO curr_post = new PostTableDO();
                curr_postId = postsLiked.get(j);
                try {
                    curr_post = getPost.execute().get();
                    //Log.d(LOG_TAG, "USER NAME FOLLOW: " + curr_user.getUserName());
                } catch (Exception e) {
                    //Log.e(LOG_TAG, "Failed to find user");
                }
                users_who_liked = curr_post.getLikedUser();
                for (int i = 0; i < users_who_liked.size(); i++) {
                    curr_userId = users_who_liked.get(i);
                    //Log.d(LOG_TAG, "USER: " + curr_userId);
                    GetUsers users = new GetUsers();
                    UsersDO curr_user = null;
                    try {
                        curr_user = users.execute().get();
                        Log.d(LOG_TAG, "USER NAME: " + curr_user.getUserName());
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Failed to find user");
                    }
                    if (curr_user != null)
                        users_list.add(curr_user);
                }
            }
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
            List<String> new_followers = new ArrayList<>();
            for (int i = 0; i < new_followers_count; i++)
                new_followers.add(followers.get(followers.size() - i - 1));

            for (int i = 0; i < new_followers.size(); i++) {
                curr_userId = new_followers.get(i);
                GetUsers users = new GetUsers();
                UsersDO curr_user = null;
                try {
                    curr_user = users.execute().get();
                    Log.d(LOG_TAG, "USER NAME FOLLOW: " + curr_user.getUserName());
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to find user");
                }
                if (curr_user != null)
                    users_list.add(curr_user);
            }
            if (users_list.size() > 0) {
                try {
                    UsersDO _user = users_list.get(position);
                    String _setText = _user.getUserName() + " is following you";
                    Log.d(LOG_TAG, "TO DISPLAY: " + _setText);
                    holder.name.setText(_setText);
                }
                catch (Exception e) {
                    Log.e(LOG_TAG, "Damn son");
                }
            }
        }
        List<ClothingDO> sale_items = new ArrayList<>();
        for (int i = 0; i < wishList_clothes.size(); i++) {
            curr_clothId = wishList_clothes.get(i);
            GetClothes clothes = new GetClothes();
            ClothingDO curr_cloth = null;
            try {
                curr_cloth = clothes.execute().get();
                Log.d(LOG_TAG, "USER NAME FOLLOW: " + curr_user.getUserName());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to find user");
            }

            //Log.e(LOG_TAG, "Cloth price: " + curr_cloth.getClothingPrice());
            if (curr_cloth != null && Integer.parseInt(curr_cloth.getClothingPrice()) < Integer.parseInt(curr_cloth.getOld_price()))
                sale_items.add(curr_cloth);
        }
        if (sale_items.size() > 0) {
            ClothingDO _cloth = sale_items.get(position);
            String _setText = _cloth.getUserId() + " is on sale!!";
            Log.d(LOG_TAG, "TO DISPLAY: " + _setText);
            holder.name.setText(_setText);
        }
    }

    private class GetUsers extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;

        public String getId() {
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

    private class GetPost extends AsyncTask<String, Void, PostTableDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;

        public String getId() {
            id = curr_postId;
            return id;
        }

        @Override
        protected PostTableDO doInBackground(String... strings) {
            PostTableDO loadUser = mapper.load(PostTableDO.class, getId());
            if (loadUser == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return loadUser;
        }
    }

    private class GetClothes extends AsyncTask<String, Void, ClothingDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;

        public String getId() {
            id = curr_clothId;
            return id;
        }

        @Override
        protected ClothingDO doInBackground(String... strings) {
            ClothingDO loadClothes = mapper.load(ClothingDO.class, getId());
            if (loadClothes == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return loadClothes;
        }
    }

    public int getItemCount() {
        int sum = 0;
        if (users_who_liked.size() > 0)
            sum += users_who_liked.size();
        if (new_followers_count > 0)
            sum += (int) (new_followers_count);
        if (wishList_clothes.size() > 0)
            sum += wishList_clothes.size();
        if (sum == 0)
            return 2;
        return sum;
    }
}