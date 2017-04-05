package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class FollowActivity extends AppCompatActivity {

    private static final String LOG_TAG = FollowActivity.class.getSimpleName();
    private String pageKey;


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
