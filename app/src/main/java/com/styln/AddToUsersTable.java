package com.styln;

import android.app.Activity;
import android.content.*;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.IdentityProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddToUsersTable {
    //private CognitoCachingCredentialsProvider credentialsProvider;
    //CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
    //public UsersDO users_table;
    public AmazonClientException lastException;
    final String LOG_TAG = AddToUsersTable.class.getSimpleName();
    private String userName;

    public AddToUsersTable(String userName) {
        this.userName = userName;
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }
     // adding to user table
    public void addItem() {
        Log.i (LOG_TAG, "Adding item...");
        final UsersDO users_table = new UsersDO();
        List<String> a = new ArrayList<String>();
        List<String> b = new ArrayList<String>();
        List<String> c = new ArrayList<String>();
        List<String> d = new ArrayList<String>();
        List<String> e = new ArrayList<String>();
        users_table.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        users_table.setUserName(userName);
        users_table.setUserPhoto("NO PHOTO");
        users_table.setUserPrivacy("public".getBytes());
        users_table.setUserDescription("No Description");
        users_table.setUserPosts(a);
        users_table.setUsersFollowers(b);
        users_table.setUsersFollowing(c);
        users_table.set_UserWardrobe(d);
        users_table.set_UserWishList(e);

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
