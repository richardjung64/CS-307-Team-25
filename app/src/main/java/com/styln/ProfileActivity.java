package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobile.user.signin.GoogleSignInProvider;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName;
    static boolean checked = false;
    private ImageView profilePic;

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
    }

    public void openFollowers(View view) {
        Log.d(LOG_TAG, "Launching Followers Activity...");
        startActivity(new Intent(ProfileActivity.this, FollowActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openFollowing(View view) {
        Log.d(LOG_TAG, "Launching Following Activity...");
        startActivity(new Intent(ProfileActivity.this, FollowingActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

}
