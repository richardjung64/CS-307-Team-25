package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class FollowerActivity extends AppCompatActivity {

    private static final String LOG_TAG = FollowerActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
    }


    public void back(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(FollowerActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }
}
