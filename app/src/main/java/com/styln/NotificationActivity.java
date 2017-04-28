package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

/**
 * Created by shanu on 4/27/17.
 */

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationListAdapter iAdapter;
    private final DynamoDBMapper g_mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    private UsersDO g_currUser;

    private static final String LOG_TAG = NotificationActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (HomePostsAdapter.post_id == null) {
//            setContentView(R.layout.no_notf_layout);
//            return;
//        }
        PostTableDO thisPost = null;
        if (HomePostsAdapter.post_id != null) {
            GetPost post = new GetPost();
            try {
                thisPost = post.execute().get();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Post error");
            }
        }
//        if (thisPost == null) {
//            setContentView(R.layout.no_notf_layout);
//            return;
//        }
//        if (!(thisPost.getPostPoster().equals(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()))) {
//            setContentView(R.layout.no_notf_layout);
//            return;
//        }
        UsersDO curr_user = null;
        GetUsers users = new GetUsers();
        try {
            curr_user = users.execute().get();
            g_currUser = curr_user;
            Log.d(LOG_TAG, "USER NAME: " + curr_user.getUserName());
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed to find user");
        }

        setContentView(R.layout.notification_layout);

        iAdapter = new NotificationListAdapter(this,thisPost, curr_user);

        recyclerView = (RecyclerView) findViewById(R.id._recycler_view_notify);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

    }

    private class GetPost extends AsyncTask<String, Void, PostTableDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;
        @Override
        protected PostTableDO doInBackground(String... strings) {
            PostTableDO post = mapper.load(PostTableDO.class, HomePostsAdapter.post_id);
            if (post == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return post;
        }
    }

    private class GetUsers extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO loadUser = mapper.load(UsersDO.class, AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
            if (loadUser == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return loadUser;
        }
    }

//    public void onResume() {
//        super.onResume();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    g_currUser.setNew_followers(0);
//                    g_mapper.save(g_currUser);
//                } catch (final AmazonClientException ex) {
//                    Log.e(LOG_TAG, "failed to add");
//                    return;
//                }
//                ThreadUtils.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                });
//            }
//        }).start();
//    }

    public void back(View view) {
        Log.d(LOG_TAG, "Launching Settings Activity...");
        startActivity(new Intent(NotificationActivity.this, SettingsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }
}