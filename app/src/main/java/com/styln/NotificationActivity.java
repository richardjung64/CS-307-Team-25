package com.styln;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

/**
 * Created by shanu on 4/27/17.
 */

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationListAdapter iAdapter;

    private static final String LOG_TAG = NotificationActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HomePostsAdapter.post_id == null) {
            setContentView(R.layout.no_notf_layout);
            return;
        }
        GetPost post = new GetPost();
        PostTableDO thisPost = null;
        try {
            thisPost = post.execute().get();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Post error");
        }
        if (thisPost == null) {
            setContentView(R.layout.no_notf_layout);
            return;
        }
        if (!(thisPost.getPostPoster().equals(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()))) {
            setContentView(R.layout.no_notf_layout);
            return;
        }

        setContentView(R.layout.notification_layout);

        iAdapter = new NotificationListAdapter(this,thisPost);

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
}
