package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        uAdapter = new FollowUsersAdapter(this, getFollowList(pageKey));

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

    public List<UsersDO> getFollowList(final String str){

        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final List<UsersDO> loadresult = new ArrayList<UsersDO>();

        Runnable runnable = new Runnable() {
            public void run() {
                UsersDO currentUser = new UsersDO();
                String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
                currentUser = mapper.load(UsersDO.class, userID);
                List<String> tempSet;

                if (str.equals("followers")) {
                    tempSet = currentUser.getUsersFollowers();
                } else {
                    tempSet = currentUser.getUsersFollowing();
                }
                currentUser.getUsersFollowers().add(5,"dd");

                if(currentUser.getUsersFollowers() == null){
                    Log.d(LOG_TAG,"NULLLLL");
                }
                List<String> result = new ArrayList<String>(tempSet);

                for (String str : result) {
                    UsersDO iterator = mapper.load(UsersDO.class, str);
                    loadresult.add(iterator);
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        return loadresult;
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
