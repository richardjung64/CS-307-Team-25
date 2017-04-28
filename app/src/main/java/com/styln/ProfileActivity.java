package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobile.user.signin.GoogleSignInProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName,description,numFollowers,numFollowing;
    static boolean checked = false;
    private ImageView profilePic;
    String currUserID;

    private List<PostTableDO> postList = new ArrayList<>();
    private List<ClothingDO> itemList = new ArrayList<>();


    private RecyclerView recyclerView,recyclerView2;
    private ProfileItemsAdapter iAdapter;
    private ProfilePostsAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();
        identityManager = awsMobileClient.getIdentityManager();
        userName = (TextView)findViewById(R.id.userName);
        description = (TextView)findViewById(R.id.description);
        profilePic = (ImageView)findViewById(R.id.profilePicture);
        numFollowers = (TextView)findViewById(R.id.numFollowers);
        numFollowing = (TextView)findViewById(R.id.numFollowing);

        UsersDO thisUser = null;
        grabUser _task = new grabUser();

        try {
            thisUser = _task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (thisUser != null && !thisUser.isLogin_opt()) {
            if (thisUser.isHasCustomDp()) {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = thisUser.getUserPhoto();
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
            else {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = FacebookSignInProvider.userImageUrl;
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
            //Log.i(LOG_TAG,FacebookSignInProvider.userName);
        }
        else {
            if (thisUser != null && thisUser.isHasCustomDp()) {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = thisUser.getUserPhoto();
                Log.d(LOG_TAG, "Profile DP " + address);
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
            else {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = GoogleSignInProvider.userImageUrl;
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
        }

        grabUser task = new grabUser();
        UsersDO currentUser = new UsersDO();
        try {
            currentUser = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        userName.setText(currentUser.getUserName());
        description.setText(currentUser.getUserDescription());
        if(currentUser.getUserFollower() == null){
            numFollowers.setText("0");
        } else {
            numFollowers.setText(""+currentUser.getUserFollower().size());
        }
        if(currentUser.getUserFollowing() == null){
            numFollowing.setText("0");
        } else {
            numFollowing.setText(""+currentUser.getUserFollowing().size());
        }

        getPostList task2 = new getPostList();
        try {
            postList = task2.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Collections.reverse(postList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pAdapter = new ProfilePostsAdapter(this,postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

        pAdapter.notifyDataSetChanged();


        getWardrobe task3 = new getWardrobe();
        try {
            itemList = task3.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Collections.reverse(itemList);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        iAdapter = new ProfileItemsAdapter(this,itemList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(iAdapter);

        iAdapter.notifyDataSetChanged();
    }

    public void editProfile(View view) {
        Intent intent= new Intent(ProfileActivity.this, InformationActivity.class);
        intent.putExtra("ID",AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        Log.d(LOG_TAG,AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        startActivity(intent);
        return;
    }

    private class grabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            loadresult = currentUser;
            return loadresult;
        }
    }

    private class getWardrobe extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            List<String> tempSet;

            if(currentUser.getUserWardrobe() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUserWardrobe(new ArrayList<String>());
            }

            tempSet = currentUser.getUserWardrobe();
            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                ClothingDO iterator = mapper.load(ClothingDO.class, str);
                loadresult.add(iterator);

            }
            return loadresult;
        }
    }

    private class getPostList extends AsyncTask<String, Void, List<PostTableDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<PostTableDO> loadresult = new ArrayList<PostTableDO>();
        @Override
        protected List<PostTableDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            List<String> tempSet;

            if(currentUser.getUserPosts() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUserPosts(new ArrayList<String>());
            }

            tempSet = currentUser.getUserPosts();
            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                PostTableDO iterator = mapper.load(PostTableDO.class, str);
                loadresult.add(iterator);

            }
            return loadresult;
        }
    }


    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(ProfileActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","ITEM"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
        Log.d(LOG_TAG, "Launching Post Activity...");
        startActivity(new Intent(ProfileActivity.this, PostActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(ProfileActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
    }

    public void openFollowers(View view) {
        Log.d(LOG_TAG, "Launching Followers Activity...");
        startActivity(new Intent(ProfileActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","followers").putExtra("ID",currUserID));
        // finish should always be called on the main thread.
        finish();
    }

    public void openFollowing(View view) {
        Log.d(LOG_TAG, "Launching Following Activity...");
        startActivity(new Intent(ProfileActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","following").putExtra("ID",currUserID));
        // finish should always be called on the main thread.
        finish();
    }

    public void openWardrobe(View view) {
        Log.d(LOG_TAG, "Launching Wardrobe Activity...");
        startActivity(new Intent(ProfileActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wardrobe").putExtra("ID",currUserID));
        // finish should always be called on the main thread.
        finish();
    }

    public void openWishlist(View view) {
        Log.d(LOG_TAG, "Launching Wishlist Activity...");
        startActivity(new Intent(ProfileActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wishlist").putExtra("ID",currUserID));
        // finish should always be called on the main thread.
        finish();
    }

}
