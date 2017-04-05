package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity {

    private static final String LOG_TAG = FollowActivity.class.getSimpleName();
    private String pageKey;

    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UsersAdapter uAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        pageKey = getIntent().getStringExtra("KEY");
        Log.d(LOG_TAG, "Opened from " + pageKey);
        if(pageKey.equals("followers")){
            refreshFollowers(this.findViewById(android.R.id.content));
        } else {
            refreshFollowing(this.findViewById(android.R.id.content));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        uAdapter = new UsersAdapter(userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(uAdapter);

        prepareUserData();

    }

    private void prepareUserData() {
        User user = new User("Tiger");
        userList.add(user);

        user = new User("Tiger 2");
        userList.add(user);

        user = new User("Tiger 3");
        userList.add(user);

        user = new User("Tiger 4");
        userList.add(user);

        uAdapter.notifyDataSetChanged();
    }


    public void back(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(FollowActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        // finish should always be called on the main thread.
        finish();
    }


    public void refreshFollowers(View view) {
        Log.d(LOG_TAG, "Refresh Followers");

    }

    public void refreshFollowing(View view) {
        Log.d(LOG_TAG, "Refresh Following");
    }
}
