package com.styln;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName;
    static boolean checked = false;
    private ImageView profilePic;

    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();
        identityManager = awsMobileClient.getIdentityManager();
        userName = (TextView)findViewById(R.id.userName);
        profilePic = (ImageView)findViewById(R.id.profilePicture);
        if (SignInActivity.signin_opt == 'f') {
            String address = FacebookSignInProvider.userImageUrl;
            new LoadURLImage(address, profilePic).execute();
            userName.setText(FacebookSignInProvider.userName);
        }
        else {
            String address = GoogleSignInProvider.userImageUrl;
            new LoadURLImage(address, profilePic).execute();
            userName.setText(GoogleSignInProvider.userName);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        iAdapter = new ItemsAdapter(this,itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

        prepareCollectionData();

    }

    private void prepareCollectionData() {
        Item item = new Item("Adidas Shirt",R.drawable.item_1);
        itemList.add(item);

        item = new Item("Adidas Shirt",R.drawable.item_1);
        itemList.add(item);
        item = new Item("Adidas Shirt",R.drawable.item_1);
        itemList.add(item);
        item = new Item("Adidas Shirt",R.drawable.item_1);
        itemList.add(item);

        item = new Item("Adidas Shoes",R.drawable.item_2);
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
