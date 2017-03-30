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

import java.util.HashMap;


public class AddToUsersTable {
    public Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    //CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
    public UsersDO users_table;
    public AmazonClientException lastException;
    final String LOG_TAG = AddToUsersTable.class.getSimpleName();
    public AddToUsersTable(CognitoCachingCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        credentialsProvider = new CognitoCachingCredentialsProvider(context, "us-east-1:43cde55a-51f7-4d7a-a2ab-f77c948eed21", Regions.US_EAST_1);
        ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        mapper = new DynamoDBMapper(ddbClient);
        users_table = new UsersDO();
    }
     // adding to user table
    public void addItem() {
        users_table.setUserId();
        users_table.setUserName("John");
        users_table.setUserAge((double)23);
        users_table.setUserDescription("First user yo");
        users_table.setUserGender("Trans");
        HashMap<String, AttributeValue> primaryKey = new HashMap<>();
        AttributeValue userId = new AttributeValue().withS("user101");
        primaryKey.put("userId", userId);
        PutItemRequest request = new PutItemRequest().withTableName("stylin-mobilehub-1048106400-Users");
        try {
            ddbClient.putItem(request);
        }
        catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "add not succesful");
            lastException = ex;
        }
        if (lastException != null) {
            throw lastException;
        }
    }

    public void onCancel(final IdentityProvider provider) {

    }
    public void onError(final IdentityProvider provider, final Exception ex) {

    }

}
