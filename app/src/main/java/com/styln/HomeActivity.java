package com.styln;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobile.user.signin.GoogleSignInProvider;
import com.amazonaws.mobile.util.ThreadUtils;
import com.styln.demo.nosql.DemoNoSQLOperationListAdapter;
import com.styln.demo.nosql.DemoNoSQLOperationListItem;
import com.styln.demo.nosql.DemoNoSQLTableBase;
import com.styln.demo.nosql.DemoNoSQLTableFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private ImageView imageLike;
    private TextView textLikes;
    private ImageView profilePic;
    private Button follow;
    private String userName;
    private AddToUsersTable addToUsersTable;
    DemoNoSQLTableBase table;
    ListView operationsListView;
    private ArrayAdapter<DemoNoSQLOperationListItem> operationsListAdapter;


    static boolean liked = false;
    static int numLikes = 0;
    static boolean followed = false;

    private void addItemTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    addToUsersTable.addItem();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
//                        dialogBuilder.setTitle(R.string.nosql_dialog_title_added_sample_data_text);
//                        dialogBuilder.setMessage("Add successful");
//                        dialogBuilder.setNegativeButton(R.string.nosql_dialog_ok_text, null);
//                        dialogBuilder.show();
                    }
                });
            }
        }).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLike = (ImageView)findViewById(R.id.like);
        textLikes = (TextView)findViewById(R.id.numLikes);
        follow = (Button)findViewById(R.id.follow);

        profilePic = (ImageView)findViewById(R.id.profilePicture);
        String address = FacebookSignInProvider.userImageUrl;
        new LoadURLImage(address, profilePic).execute();


        if(liked){
            imageLike.setImageResource(R.drawable.main_like_1);
            textLikes.setText(numLikes+" likes");
        } else {
            imageLike.setImageResource(R.drawable.main_like_0);
            textLikes.setText(numLikes+" likes");
        }
        if(followed){
            follow.setText("UNFOLLOW");
            follow.setTextSize(10);
    } else {
            follow.setText("FOLLOW");
            follow.setTextSize(12);
    }
        if (SignInActivity.signin_opt == 'f') {
            //String address = FacebookSignInProvider.userImageUrl;
            //new LoadURLImage(address, profilePic).execute();
            //userName.setText(FacebookSignInProvider.userName);
            userName = FacebookSignInProvider.userName;
            //Log.i(LOG_TAG,FacebookSignInProvider.userName);
        }
        else {
            //String address = GoogleSignInProvider.userImageUrl;
            //new LoadURLImage(address, profilePic).execute();
            //userName.setText(GoogleSignInProvider.userName);
            userName = GoogleSignInProvider.userName;
            //Log.i(LOG_TAG,GoogleSignInProvider.userName);

        }
        addToUsersTable = new AddToUsersTable(userName);
    addItemTable();

    }

    public void openHome(View view) {
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
            Log.d(LOG_TAG, "Launching Settings Activity...");
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            // finish should always be called on the main thread.
            finish();
    }

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

    public void addItem(View view) {
        int id = view.getId();
        Button a = (Button) findViewById(R.id.Bitem1);
        Button b = (Button) findViewById(R.id.Bitem2);
        a.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
    }
    public void addTo(View view) {
        Button a = (Button) findViewById(R.id.Bitem1);
        Button b = (Button) findViewById(R.id.Bitem2);
       a.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
    }

    public void follow(View view) {
        if(followed){
            followed = false;
            follow.setText("FOLLOW");
            follow.setTextSize(12);
        } else {
            followed = true;
            follow.setText("UNFOLLOW");
            follow.setTextSize(10);
        }
    }


}
