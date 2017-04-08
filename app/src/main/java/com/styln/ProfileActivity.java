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
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName,description,numFollowers,numFollowing;
    static boolean checked = false;
    private ImageView profilePic;

    private List<Item> itemList = new ArrayList<>();
    private List<Item> followerList = new ArrayList<>();
    private List<Item> followingList = new ArrayList<>();


    private RecyclerView recyclerView;
    private ProfileItemsAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        description.setText("CardView is another major element introduced in Material Design. Using CardView you can represent the information in a card manner with a drop shadow (elevation) and corner radius which looks consistent across the platform. ");

        if (Application.getSign_opt() == 'f') {
            String address = FacebookSignInProvider.userImageUrl;
            Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                    thumbnail(0.1f).into(profilePic);
        }
        else {
            String address = GoogleSignInProvider.userImageUrl;
            Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                    thumbnail(0.1f).into(profilePic);
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
        //currentUser.setUserDescription("ASHIUASHI");
        userName.setText(currentUser.getUserName());
        description.setText(currentUser.getUserDescription());
        /*if(currentUser.getUsersFollowers() == null){
            numFollowers.setText("0");
        }
        numFollowers.setText(currentUser.getUsersFollowers().size());
        if(currentUser.getUsersFollowing() == null){
            numFollowing.setText("0");
        }
        numFollowing.setText(currentUser.getUsersFollowing().size());*/



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        iAdapter = new ProfileItemsAdapter(this,itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

        prepareWardrobeData();

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

    private void prepareWardrobeData() {
        //TODO load wardrobe
        Item item = new Item("Tshirt 1", "Hollister",1,R.drawable.shirt1);
        itemList.add(item);

        item = new Item("Tshirt 2", "Hollister",1,R.drawable.shirt1);
        itemList.add(item);
        item = new Item("Tshirt 3", "Hollister",1,R.drawable.shirt1);
        itemList.add(item);
        item = new Item("Tshirt 4", "Hollister",1,R.drawable.shirt1);
        itemList.add(item);


        iAdapter.notifyDataSetChanged();
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
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
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
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","followers"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openFollowing(View view) {
        Log.d(LOG_TAG, "Launching Following Activity...");
        startActivity(new Intent(ProfileActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","following"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openWardrobe(View view) {
        Log.d(LOG_TAG, "Launching Wardrobe Activity...");
        startActivity(new Intent(ProfileActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wardrobe"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openWishlist(View view) {
        Log.d(LOG_TAG, "Launching Wishlist Activity...");
        startActivity(new Intent(ProfileActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wishlist"));
        // finish should always be called on the main thread.
        finish();
    }

}
