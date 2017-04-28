package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobile.user.signin.GoogleSignInProvider;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.styln.demo.nosql.DemoNoSQLOperationListItem;
import com.styln.demo.nosql.DemoNoSQLTableBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class HomeActivityBACKUP extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivityBACKUP.class.getSimpleName();
    private ImageView imageLike;
    private TextView textLikes;
    private ImageView profilePic;
    private Button follow,followMe;
    private String userName;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private ListView mListView;

    private AddToUsersTable addToUsersTable;
    private AddClothesTable addClothesTable;
    private DummyPostUser dummy;
    private AddPostsTable addPost;
    DemoNoSQLTableBase table;

    ListView operationsListView;
    private ArrayAdapter<DemoNoSQLOperationListItem> operationsListAdapter;
    private boolean a = false;

    private List<ClothingDO> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfilePostsItemsAdapter iAdapter;

    static boolean liked = false;
    static int numLikes = 0;
    static boolean followed = false;
    static boolean followedMe = false;

    String usr_name;
    String str_age;
    String userDescr;
    String str_privacy;
    String gender;
    boolean isPrivate;
    String filePath;
    String s3_link;

    public final static String USER_NAME = "User name to activity";
    public final static String USER_AGE = "User age to activity";
    public final static String USER_DESCRIPTION = "User descr to activity";
    public final static String USER_PRIVACY = "User privacy to activity";
    public final static String USER_GENDER = "User gender to activity";


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
                    }
                });
            }
        }).start();
    }

    private void addDummyItemTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dummy.addDummyItem();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    private void addPostTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    addPost.addPost();
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

    private void addClothesTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    addClothesTable.addClothes();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UsersDO thisUser = null;
                grabUser task = new grabUser();

                try {
                    thisUser = task.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (thisUser != null && !thisUser.isLogin_opt()) {
                    if (thisUser.isHasCustomDp()) {
                        profilePic = (ImageView) findViewById(R.id.profilePicture);
                        String address = thisUser.getUserPhoto();
                        Glide.with(getBaseContext()).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                                thumbnail(0.1f).into(profilePic);
                    } else {
                        profilePic = (ImageView) findViewById(R.id.profilePicture);
                        String address = FacebookSignInProvider.userImageUrl;
                        Glide.with(getBaseContext()).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                                thumbnail(0.1f).into(profilePic);
                    }
                    //Log.i(LOG_TAG,FacebookSignInProvider.userName);
                } else {
                    if (thisUser != null && thisUser.isHasCustomDp()) {
                        profilePic = (ImageView) findViewById(R.id.profilePicture);
                        String address = thisUser.getUserPhoto();
                        Log.d(LOG_TAG, "Profile DP " + address);
                        Glide.with(getBaseContext()).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                                thumbnail(0.1f).into(profilePic);
                    } else {
                        profilePic = (ImageView) findViewById(R.id.profilePicture);
                        String address = GoogleSignInProvider.userImageUrl;
                        Glide.with(getBaseContext()).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                                thumbnail(0.1f).into(profilePic);
                    }
                }
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textLikes = (TextView)findViewById(R.id.numLikes);
        follow = (Button)findViewById(R.id.home_follow);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mListView = (ListView) findViewById(R.id.list_view);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        refreshContent();
                    }
                }
        );

        Intent i = getIntent();
        usr_name = i.getStringExtra(InformationActivity.USER_NAME);
        str_age = i.getStringExtra(InformationActivity.USER_AGE);
        userDescr = i.getStringExtra(InformationActivity.USER_DESCRIPTION);
        isPrivate = i.getBooleanExtra(InformationActivity.USER_PRIVACY, false);
        gender = i.getStringExtra(InformationActivity.USER_GENDER);
        filePath = i.getStringExtra(InformationActivity.USER_DP);
        s3_link = i.getStringExtra(InformationActivity.S3_LINK);

        //selectedImage = Uri.parse(i.getStringExtra(InformationActivity.USER_DP));
//        Log.d(LOG_TAG, "RESTORED NAME " + usr_name);
//        Log.d(LOG_TAG, "RESTORED AGE " + str_age);
//        Log.d(LOG_TAG, "RESTORED DESCRIPTION " + userDescr);
//        Log.d(LOG_TAG, "RESTORED IDENTITY " + gender);
//        Log.d(LOG_TAG, "RESTORED PRIVACY " + isPrivate);
        //isPrivate = Boolean.getBoolean(str_privacy);


    //TODO get our servers username
        //Log.d(LOG_TAG, "Login Option " + Application.getSign_opt());
        UsersDO thisUser = null;
        grabUser task = new grabUser();

        try {
            thisUser = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (thisUser != null && !thisUser.isLogin_opt()) {
            if (thisUser.isHasCustomDp()) {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = thisUser.getUserPhoto();
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
            else {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = FacebookSignInProvider.userImageUrl;
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
            //Log.i(LOG_TAG,FacebookSignInProvider.userName);
        }
        else {
            if (thisUser != null && thisUser.isHasCustomDp()) {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = thisUser.getUserPhoto();
                Log.d(LOG_TAG, "Profile DP " + address);
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
            else {
                profilePic = (ImageView) findViewById(R.id.profilePicture);
                String address = GoogleSignInProvider.userImageUrl;
                Glide.with(this).load(address).bitmapTransform(new CropCircleTransformation(getBaseContext())).
                        thumbnail(0.1f).into(profilePic);
            }
        }

        if ((thisUser == null) || (thisUser.isFirstTime())) {
            Log.d(LOG_TAG, "Adding to database");
            addToUsersTable = new AddToUsersTable(usr_name, str_age, userDescr, gender, isPrivate,
                    FacebookSignInProvider.userImageUrl, GoogleSignInProvider.userImageUrl);
            addItemTable();
            addPost = new AddPostsTable();
            addPostTable();
            addClothesTable = new AddClothesTable();
            addClothesTable();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        iAdapter = new ProfilePostsItemsAdapter(this,itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

        prepareCollectionData();

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(USER_NAME, usr_name);
        savedInstanceState.putString(USER_AGE, str_age);
        savedInstanceState.putBoolean(USER_PRIVACY, isPrivate);
        savedInstanceState.putString(USER_DESCRIPTION, userDescr);
        savedInstanceState.putString(USER_GENDER, gender);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void openUser(View view) {
        String userid = "us-east-1:6266ddac-b3e7-403c-a2e0-bb5b7c861b60";
        Log.d(LOG_TAG, "Launching Other User Activity...");
        startActivity(new Intent(HomeActivityBACKUP.this, OthersActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("ID",userid));
        // finish should always be called on the main thread.
        finish();
    }


//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        usr_name = savedInstanceState.getString(InformationActivity.USER_NAME);
//        str_age = savedInstanceState.getString(InformationActivity.USER_AGE);
//        isPrivate = savedInstanceState.getBoolean(InformationActivity.USER_PRIVACY);
//        userDescr = savedInstanceState.getString(InformationActivity.USER_DESCRIPTION);
//        gender = savedInstanceState.getString(InformationActivity.USER_GENDER);
//    }

    private class grabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            loadresult = currentUser;
            return loadresult;
        }
    }


    private void prepareCollectionData() {
        //TODO item load
        ClothingDO item = new ClothingDO();
        item.setUserId("tshirt");
        item.setClothingBrand("Hollister");
        itemList.add(item);
        iAdapter.notifyDataSetChanged();
    }

    public void checkLogin()
    {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO user = mapper.load(UsersDO.class, AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        if(user == null)
            a =  true;
    }
    public void checkLoginHelper()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkLogin();
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "failed to add");
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    public void openHome(View view) {
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(HomeActivityBACKUP.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(HomeActivityBACKUP.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(HomeActivityBACKUP.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openSettings(View view) {
            Log.d(LOG_TAG, "Launching Settings Activity...");
            startActivity(new Intent(HomeActivityBACKUP.this, SettingsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            // finish should always be called on the main thread.
            finish();
    }



    public void addTo(View view) {
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.item_action_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new HomeActivityBACKUP.MyMenuItemClickListener());
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
            DataAction fl = new DataAction();
            fl.followSomeone("us-east-1:6266ddac-b3e7-403c-a2e0-bb5b7c861b60");
            Log.d("d","Follow");

        } else {
            followed = true;
            follow.setText("UNFOLLOW");
            follow.setTextSize(10);
            DataAction fl = new DataAction();
            fl.unfollowSomeone("us-east-1:6266ddac-b3e7-403c-a2e0-bb5b7c861b60");
            Log.d("d","UNFollow");
        }
    }

    protected void onResume(Bundle savedInstanceState) {
        super.onResume();
        if (savedInstanceState != null) {
            usr_name = savedInstanceState.getString(USER_NAME);
            str_age = savedInstanceState.getString(USER_AGE);
            isPrivate = savedInstanceState.getBoolean(USER_PRIVACY);
            userDescr = savedInstanceState.getString(USER_DESCRIPTION);
            gender = savedInstanceState.getString(USER_GENDER);
        }
    }

    public void openSuggest(View view) {
        Log.d(LOG_TAG, "Launching Suggested Followers Activity...");
        startActivity(new Intent(HomeActivityBACKUP.this, SettingsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }


}
