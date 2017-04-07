package com.styln;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by shanu on 4/7/17.
 */

public class DummyPostUser {
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
    //public UsersDO users_table;
    public AmazonClientException lastException;
    final String LOG_TAG = DummyPostUser.class.getSimpleName();
    private String userName;

    public DummyPostUser() {
        this.userName = "Kevin Jiang";
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }
    // adding to user table
    public void addDummyItem() {
        Log.i (LOG_TAG, "Adding item...");
        final UsersDO users_table = new UsersDO();
        users_table.setUserId("kevin_usr101");
        users_table.setUserName(userName);
        users_table.setUserPhoto("NO PHOTO");
        users_table.setUserPrivacy(false);
        try {
            Log.i (LOG_TAG, "Adding for real now...");
            mapper.save(users_table);
        }
        catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "add not succesful");
            lastException = ex;
        }
        if (lastException != null) {
            throw lastException;
        }
        Log.i (LOG_TAG, "Looks a success...");
    }

    public void onCancel(final IdentityProvider provider) {

    }
    public void onError(final IdentityProvider provider, final Exception ex) {

    }
}
