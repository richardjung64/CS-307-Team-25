package com.styln;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

/**
 * Created by shalingyi on 4/7/17.
 */

public class FollowAction {
    /*
@Param: someone is the userid of the person current user want to follow;
 */

    public FollowAction() {
    }

    public void followSomeone(final String someone){
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                curr.getUsersFollowing().add(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                sb.getUsersFollowers().add(currUserID);
                mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

    public void unfollowSomeone(final String someone){
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                curr.getUsersFollowing().remove(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                sb.getUsersFollowers().remove(currUserID);
                mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

}
