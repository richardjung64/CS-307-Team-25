package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity {

    private static final String LOG_TAG = FollowActivity.class.getSimpleName();
    private String pageKey;

    private List<UsersDO> followerList = new ArrayList<>();
    private List<UsersDO> followingList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FollowUsersAdapter uAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        pageKey = getIntent().getStringExtra("KEY");
        Log.d(LOG_TAG, "Opened from " + pageKey);

        if(pageKey.equals("followers")){
            uAdapter = new FollowUsersAdapter(this, followerList);
            prepareFollowerData();
        } else {
            uAdapter = new FollowUsersAdapter(this, followingList);
            prepareFollowingData();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(uAdapter);

    }


    public void back(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(FollowActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void prepareFollowerData() {
        UsersDO user = new UsersDO("Tiger");
        followerList.add(user);

        user = new UsersDO("Tiger 2");
        followerList.add(user);

        user = new UsersDO("Tiger 3");
        followerList.add(user);

        user = new UsersDO("Tiger 4");
        followerList.add(user);

        uAdapter.notifyDataSetChanged();
    }


    private void prepareFollowingData() {
        UsersDO user = new UsersDO("Tiger");
        followingList.add(user);

        user = new UsersDO("Tiger 2");
        followingList.add(user);

        uAdapter.notifyDataSetChanged();
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
