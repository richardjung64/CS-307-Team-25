package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class FollowActivity extends AppCompatActivity {

    private static final String LOG_TAG = FollowActivity.class.getSimpleName();
    private String pageKey;

    private RecyclerView recyclerView;
    private FollowUsersAdapter uAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        pageKey = getIntent().getStringExtra("KEY");
        Log.d(LOG_TAG, "Opened from " + pageKey);


        List<UsersDO> us = null;

        if(pageKey.equals("followers")){
            getFollower task;
            task = new getFollower();
            try {
                us = task.execute(pageKey).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            getFollowing task;
            task = new getFollowing();
            try {
                us = task.execute(pageKey).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        Log.d("THIS IS IT",us.toString());

        uAdapter = new FollowUsersAdapter(this, us);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(uAdapter);

        uAdapter.notifyDataSetChanged();

    }


    public void back(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(FollowActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private class getFollower extends AsyncTask<String, Void, List<UsersDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<UsersDO> loadresult = new ArrayList<UsersDO>();
        @Override
        protected List<UsersDO> doInBackground(String... strings) {
                    UsersDO currentUser = new UsersDO();
                    String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
                    currentUser = mapper.load(UsersDO.class, userID);
                    List<String> tempSet;

            if(currentUser.getUsersFollowers() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUsersFollowers(new ArrayList<String>());

            }
            tempSet = currentUser.getUsersFollowers();

                    List<String> result = new ArrayList<String>(tempSet);

                    for (String str : result) {
                        UsersDO iterator = mapper.load(UsersDO.class, str);
                        loadresult.add(iterator);

                    }
            return loadresult;
        }
    }

    private class getFollowing extends AsyncTask<String, Void, List<UsersDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<UsersDO> loadresult = new ArrayList<UsersDO>();
        @Override
        protected List<UsersDO> doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();

            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);

            List<String> tempSet;
            if(currentUser.getUsersFollowing() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUsersFollowing(new ArrayList<String>());

            }
            tempSet = currentUser.getUsersFollowing();

            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                UsersDO iterator = mapper.load(UsersDO.class, str);
                loadresult.add(iterator);

            }
            return loadresult;
        }
    }


    public void refreshFollowers(View view) {
        Log.d(LOG_TAG, "Refresh Followers");
        startActivity(new Intent(FollowActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","followers"));
        finish();
    }

    public void refreshFollowing(View view) {
        Log.d(LOG_TAG, "Refresh Following");
        startActivity(new Intent(FollowActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","following"));
        finish();
    }
}
