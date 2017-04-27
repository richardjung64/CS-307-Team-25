package com.styln;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrendActivity extends AppCompatActivity {

    private static final String LOG_TAG = TrendActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<ClothingDO> itemRank = new ArrayList<>();
    private List<UsersDO> userRank = new ArrayList<>();

    private TrendItemsAdapter iAdapter;
    private TrendUsersAdapter uAdapter;

    private String pageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);


        pageKey = getIntent().getStringExtra("KEY");
        Log.d(LOG_TAG, "Opened from " + pageKey);

        if(pageKey.equals("USER")){

        } else {
           /* getItemList task = new getItemList();
            try {
                itemRank = task.execute(pageKey).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            iAdapter = new TrendItemsAdapter(this, itemRank);*/
        }


        //iAdapter = new TrendItemsAdapter(this, rank);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

    }

    private void prepareTrendData() {
        //TODO Load the RANK here

    }

    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(TrendActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {

    }

    public void openPost(View view) {
        Log.d(LOG_TAG, "Launching Post Activity...");
        startActivity(new Intent(TrendActivity.this, PostActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(TrendActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(TrendActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTopItem(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(TrendActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","ITEM"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTopUser(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(TrendActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","USER"));
        // finish should always be called on the main thread.
        finish();
    }


    private class getItemList extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        String id;
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = id;
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

    private class getUserList extends AsyncTask<String, Void, List<UsersDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<UsersDO> loadresult = new ArrayList<UsersDO>();
        String id;
        @Override
        protected List<UsersDO> doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();
            String userID = id;
            currentUser = mapper.load(UsersDO.class, userID);
            List<String> tempSet;

            if(currentUser.getUserFollower() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUserFollower(new ArrayList<String>());
            }
            tempSet = currentUser.getUserFollower();

            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                UsersDO iterator = mapper.load(UsersDO.class, str);
                loadresult.add(iterator);
            }
            return loadresult;
        }
    }
}
