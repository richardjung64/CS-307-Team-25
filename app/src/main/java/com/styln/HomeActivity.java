package com.styln;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobile.user.signin.GoogleSignInProvider;
import com.amazonaws.mobile.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.styln.demo.nosql.DemoNoSQLOperationListItem;
import com.styln.demo.nosql.DemoNoSQLTableBase;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private ImageView imageLike;
    private TextView textLikes;
    private ImageView profilePic;
    private Button follow,followMe;
    private String userName;
    private AddToUsersTable addToUsersTable;
    DemoNoSQLTableBase table;
    ListView operationsListView;
    private ArrayAdapter<DemoNoSQLOperationListItem> operationsListAdapter;


    static boolean liked = false;
    static int numLikes = 0;
    static boolean followed = false;
    static boolean followedMe = false;

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
        follow = (Button)findViewById(R.id.home_follow);
        followMe = (Button)findViewById(R.id.home_followMe);

        profilePic = (ImageView)findViewById(R.id.profilePicture);
        String address = FacebookSignInProvider.userImageUrl;
        Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                thumbnail(0.1f).into(profilePic);

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
            follow.setTextSize(10);
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
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(HomeActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
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


    public void addTo(View view) {
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.item_action_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new HomeActivity.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_like:
                    Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_add_to_wardrobe:
                    Toast.makeText(getApplicationContext(), "Added to Wardrobe", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_add_to_wishlist:
                    Toast.makeText(getApplicationContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    public void follow(View view) {
        if(followed){
            followed = false;
            follow.setText("FOLLOW");
            follow.setTextSize(10);
        } else {
            followed = true;
            follow.setText("UNFOLLOW");
            follow.setTextSize(10);
        }
    }


    public void followMe(View view) {
        if(followedMe){
            followedMe = false;
            followMe.setText("FOLLOW ME");
            followMe.setTextSize(10);
        } else {
            followedMe = true;
            followMe.setText("UNFOLLOW ME");
            followMe.setTextSize(8);
        }
    }
}
