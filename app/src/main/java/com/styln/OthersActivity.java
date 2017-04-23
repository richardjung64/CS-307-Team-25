package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobile.user.signin.GoogleSignInProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class OthersActivity extends AppCompatActivity {

    private static final String LOG_TAG = OthersActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName,description,numFollowers,numFollowing,isPrivate,textWard;
    static boolean checked = false;
    private ImageView profilePic,isPrivatePicture,wardBG;
    private String pageID;
    private Button follow;

    private List<ClothingDO> itemList = new ArrayList<>();


    private RecyclerView recyclerView;
    private ProfileItemsAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();
        identityManager = awsMobileClient.getIdentityManager();

        pageID = getIntent().getStringExtra("ID");

        grabUser task = new grabUser();
        UsersDO loadUser = new UsersDO();
        try {
            task.id = pageID;
            loadUser = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        isPrivate = (TextView)findViewById(R.id.isPrivate);
        isPrivatePicture = (ImageView)findViewById(R.id.isPrivatePicture);
        textWard = (TextView)findViewById(R.id.textWardrobe);
        wardBG = (ImageView)findViewById(R.id.collectionbg);

        userName = (TextView)findViewById(R.id.userName);
        description = (TextView)findViewById(R.id.description);
        profilePic = (ImageView)findViewById(R.id.profilePicture);
        numFollowers = (TextView)findViewById(R.id.numFollowers);
        numFollowing = (TextView)findViewById(R.id.numFollowing);
        follow = (Button)findViewById(R.id.others_follow);
        boolean following = false;

        if (loadUser != null && !loadUser.isLogin_opt()) {
            if (loadUser.isHasCustomDp()) {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = loadUser.getUserPhoto();
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
            if (loadUser != null && loadUser.isHasCustomDp()) {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = loadUser.getUserPhoto();
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

        String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        if(loadUser.getUserFollower() != null) {
            following = loadUser.getUserFollower().contains(currUserID);
        }

        userName.setText(loadUser.getUserName());
        description.setText(loadUser.getUserDescription());
        if(loadUser.getUserFollower() == null){
            numFollowers.setText("0");
        } else {
            numFollowers.setText(""+loadUser.getUserFollower().size());
        }
        if(loadUser.getUserFollowing() == null){
            numFollowing.setText("0");
        } else {
            numFollowing.setText(""+loadUser.getUserFollowing().size());
        }

        if(following){
            follow.setText("UNFOLLOW");
        } else {
            follow.setText("FOLLOW");
        }

        //DONT LOAD Posts and Wardrobe if private
        if(loadUser.getUserPrivacy() && (!loadUser.getUserFollower().contains(currUserID))){
            isPrivate.setVisibility(View.VISIBLE);
            isPrivatePicture.setVisibility(View.VISIBLE);
            wardBG.setVisibility(View.GONE);
            textWard.setVisibility(View.GONE);

        } else {
            getWardrobe task2 = new getWardrobe();
            task2.id = pageID;

            try {
                itemList = task2.execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            iAdapter = new ProfileItemsAdapter(this,itemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(iAdapter);

            iAdapter.notifyDataSetChanged();
        }
    }

    public void followOthers(View view) {
        DataAction da = new DataAction();
        Boolean following;
        String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();

        grabUser task = new grabUser();
        UsersDO loadUser = new UsersDO();
        try {
            task.id = pageID;
            loadUser = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        following = loadUser.getUserFollower().contains(currUserID);
        if(following){
            da.unfollowSomeone(getIntent().getStringExtra("ID"));
        } else {
            da.followSomeone(getIntent().getStringExtra("ID"));
        }

        startActivity(new Intent(OthersActivity.this, OthersActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ID",getIntent().getStringExtra("ID")));
        // finish should always be called on the main thread.
        finish();
    }

    private class grabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();
        String id = "";
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO loadUser = new UsersDO();
            String userID = ""+id;
            loadUser = mapper.load(UsersDO.class, userID);
            loadresult = loadUser;
            return loadresult;
        }
    }

    private class getWardrobe extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        String id = "";
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = ""+id;
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


    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(OthersActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(OthersActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(OthersActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(OthersActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openFollowers(View view) {
        Log.d(LOG_TAG, "Launching Followers Activity...");
        startActivity(new Intent(OthersActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","followers").putExtra("ID",getIntent().getStringExtra("ID")));
        // finish should always be called on the main thread.
        finish();
    }

    public void openFollowing(View view) {
        Log.d(LOG_TAG, "Launching Following Activity...");
        startActivity(new Intent(OthersActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","following").putExtra("ID",getIntent().getStringExtra("ID")));
        // finish should always be called on the main thread.
        finish();
    }

    public void openWardrobe(View view) {
        Log.d(LOG_TAG, "Launching Wardrobe Activity...");
        startActivity(new Intent(OthersActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wardrobe").putExtra("ID",getIntent().getStringExtra("ID")));
        // finish should always be called on the main thread.
        finish();
    }


}
