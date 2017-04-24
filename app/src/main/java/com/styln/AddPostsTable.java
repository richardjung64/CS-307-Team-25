package com.styln;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.PostTableDO;

/**
 * Created on 4/7/17.
 */

public class AddPostsTable {
    private DynamoDBMapper mapper;
    public AmazonClientException lastException;
    final String LOG_TAG = AddToUsersTable.class.getSimpleName();

    public AddPostsTable() {
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }

    public void addPost() {
        Log.i (LOG_TAG, "Adding post...");
        final PostTableDO post = new PostTableDO();
        post.setUserId("post_101");
        post.setPostDescription("My first post");
        post.setPostLikes((double)0);
        post.setPostPoster("kevin_usr101");
        try {
            Log.i (LOG_TAG, "Adding for real now...");
            mapper.save(post);
        }
        catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "add not successful");
            lastException = ex;
        }
        if (lastException != null) {
            throw lastException;
        }
        Log.i (LOG_TAG, "Post successful...");
    }

    public void onCancel(final IdentityProvider provider) {

    }
    public void onError(final IdentityProvider provider, final Exception ex) {

    }

}
