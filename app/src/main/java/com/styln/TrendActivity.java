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

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.Collections;
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
            prepareUserTrendData();
            while(userRank.isEmpty()) {
                Log.d(LOG_TAG, "POKEMON");
            }
        } else {
            prepareTrendData();
            Log.d(LOG_TAG, ":ASDA");
            while(itemRank.isEmpty()) {
                ;//Log.d(LOG_TAG, "ASFSAFXZCVZXC");
            }
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

        if(pageKey.equals("USER")){
            uAdapter = new TrendUsersAdapter(this, userRank);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(uAdapter);
        }
        else{
            iAdapter = new TrendItemsAdapter(this, itemRank);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);}

    }

    private void prepareTrendData() {
        new prepareTrend().execute();
    }
    private void prepareUserTrendData() {
        new prepareUserTrend().execute();
    }

    private class prepareTrend extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...Params) {
            Log.d(LOG_TAG, "scasafsafsafnList == null");
            try{
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                DynamoDBScanExpression scanR = new DynamoDBScanExpression();
                List<ClothingDO> scanList = mapper.scan(ClothingDO.class, scanR);
                if(scanList.isEmpty()) {
                    Log.d("AsyncTrend", "scanList == null");
                }
                scanList = new ArrayList<ClothingDO>(scanList);
                Collections.sort(scanList, new likesComparator());
                Collections.reverse(scanList);
                List<ClothingDO> rank = new ArrayList();
                int count = 0;
                for(ClothingDO cloth : scanList){
                    count++;
                    if(count <= 5){
                        itemRank.add(cloth);
                    }
                }
                if(itemRank.isEmpty()){
                    Log.d(LOG_TAG, "userList is empty");
                }
            if(scanList.isEmpty()){
                Log.d(LOG_TAG, "scanList is empty");
            }
                /*for(ClothingDO c : rank) {
                    itemRank.add(c);
                }*/
            } catch(Exception ex) {
                Log.d("AsyncScanClothing", "catch an exception");
            };

            return null;
        }
    }
    private class prepareUserTrend extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...Params) {
            Log.d(LOG_TAG, "USERERADSF == null");
            //try{
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                DynamoDBScanExpression scanR = new DynamoDBScanExpression();
                List<UsersDO> scanList = mapper.scan(UsersDO.class, scanR);
                if(scanList.isEmpty()) {
                    Log.d(LOG_TAG, "scanList == null");
                }
                scanList = new ArrayList<UsersDO>(scanList);
                if(!scanList.isEmpty()) {
                    Log.d(LOG_TAG, "scanList =dfdsafdafdas= null");
                }
                Collections.sort(scanList, new FollowersComparator());
                Collections.reverse(scanList);
                //List<ClothingDO> rank = new ArrayList();
                int count = 0;
                for(UsersDO cloth : scanList){
                    count++;
                    if(count <= 5){
                        userRank.add(cloth);
                    }
                }
                if(userRank.isEmpty()){
                    Log.d(LOG_TAG, "rankList is empty");
                }
                if(scanList.isEmpty()){
                    Log.d(LOG_TAG, "scanList is empty");
                }
                /*for(ClothingDO c : rank) {
                    itemRank.add(c);
                }*/
            /*} catch(Exception ex) {
                Log.d("AsyncScanClothing", "catch an exception");
            };*/

            return null;
        }
    }

    public void openHome(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Launching Home Activity...");
                    startActivity(new Intent(TrendActivity.this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // finish should always be called on the main thread.
                    //finish();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
    }

    public void openTrend(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Launching Trend Activity...");
                    startActivity(new Intent(TrendActivity.this, TrendActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","ITEM"));
                    // finish should always be called on the main thread.
                    //finish();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
    }

    public void openPost(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Launching Post Activity...");
                    startActivity(new Intent(TrendActivity.this, PostActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // finish should always be called on the main thread.
                    //finish();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(TrendActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Launching Profile Activity...");
                    startActivity(new Intent(TrendActivity.this, ProfileActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // finish should always be called on the main thread.
                    //finish();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
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
