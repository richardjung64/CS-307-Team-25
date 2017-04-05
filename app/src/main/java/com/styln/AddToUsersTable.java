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

import java.util.HashMap;
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
        //this.context = context;
        //credentialsProvider = new CognitoCachingCredentialsProvider(context, "us-east-1:43cde55a-51f7-4d7a-a2ab-f77c948eed21", Regions.US_EAST_1);
        //ddbClient = new AmazonDynamoDBClient(credentialsProvider);
       // mapper = new DynamoDBMapper(ddbClient);
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }
     // adding to user table
    public void addItem() {
        Log.i (LOG_TAG, "Adding item...");
        final UsersDO users_table = new UsersDO();
        users_table.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        users_table.setUserName(userName);
        users_table.setUserPhoto("NO PHOTO");
        users_table.setUserPrivacy("public".getBytes());
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
