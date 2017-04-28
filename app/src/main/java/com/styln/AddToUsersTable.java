package com.styln;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;


public class AddToUsersTable {
    //private CognitoCachingCredentialsProvider credentialsProvider;
    //CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
    //public UsersDO users_table;
    public AmazonClientException lastException;
    final String LOG_TAG = AddToUsersTable.class.getSimpleName();

    private String usr_name;

    private String userDescr;

    private String gender;
    private boolean isPrivate;
    private String age;
    private String filePath;

    public AddToUsersTable(String userName, String age, String userDescr, String gender, boolean isPrivate, String filePath) {
        this.usr_name = userName;
        this.age = age;
        this.userDescr = userDescr;
        this.gender = gender;
        this.isPrivate = isPrivate;
        this.filePath = filePath;
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
        users_table.setUserName(usr_name);
        users_table.setUserPhoto("");
        users_table.setUserPrivacy(isPrivate);
        users_table.setUserAge(age);
        users_table.setUserDescription(userDescr);
        users_table.setUserGender(gender);
        users_table.setFirstTime(false);

        users_table.setUserFollower(new ArrayList<String>());
        users_table.setUserFollowing(new ArrayList<String>());
        users_table.setUserPosts(new ArrayList<String>());
        users_table.setUserWardrobe(new ArrayList<String>());
        users_table.setUserWishList(new ArrayList<String>());

//        if (Application.getSign_opt() == 'g')
//            users_table.setLogin_opt(true); // True is google
//        else
//            users_table.setLogin_opt(false);
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
