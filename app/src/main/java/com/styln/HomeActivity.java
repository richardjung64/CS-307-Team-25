package com.styln;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.facebook.login.widget.ProfilePictureView;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private ImageView imageLike;
    private TextView textLikes;
    private ImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLike = (ImageView)findViewById(R.id.like);
        textLikes = (TextView)findViewById(R.id.numLikes);
        profilePic = (ImageView)findViewById(R.id.profilePicture);
        String add = FacebookSignInProvider.userImageUrl;
    }



    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Main Activity...");
        startActivity(new Intent(HomeActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(HomeActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
    }

    public void openBrowse(View view) {
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openSettings(View view) {
    }

    boolean liked = false;
    int numLikes = 0;

    public void likePost(View view) {
        if(liked == false){
            liked = true;
            numLikes++;
            imageLike.setImageResource(R.drawable.main_like_1);
            textLikes.setText(numLikes+" likes");
        } else {
            liked = false;
            numLikes--;
            imageLike.setImageResource(R.drawable.main_like_0);
            textLikes.setText(numLikes+" likes");
        }
    }

    public void addTo(View view) {
/*        Intent intent = getIntent();
        ImageView image =
        intent.putExtra(String.valueOf(R.id.myCollection),R.drawable.main_add);
        startActivity(intent);*/
    }


}
