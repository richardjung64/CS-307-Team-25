package com.styln;

import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;

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
                if(curr.getUsersFollowing() == null){
                    curr.setUsersFollowing(new ArrayList<String>());
                }
                curr.getUsersFollowing().add(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                if(sb.getUsersFollowers() == null){
                    sb.setUsersFollowers(new ArrayList<String>());
                }
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
                if(curr.getUsersFollowing() == null){
                    curr.setUsersFollowing(new ArrayList<String>());
                }
                curr.getUsersFollowing().remove(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                if(sb.getUsersFollowers() == null){
                    sb.setUsersFollowers(new ArrayList<String>());
                }
                sb.getUsersFollowers().remove(currUserID);
                mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

    public void UserChanges(final String username, final String age, final boolean isPrivate, final String description, final String gender){
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
//                if(curr.getUsersFollowing() == null){
//                    curr.setUsersFollowing(new ArrayList<String>());
//                }
                //curr.getUsersFollowing().remove(someone);
                if (curr != null) {
                    curr.getUserAge();
                    curr.setUserAge(age);
                    mapper.save(curr);
                    curr.getUserDescription();
                    curr.setUserDescription(description);
                    curr.getUserPrivacy();
                    curr.setUserPrivacy(isPrivate);
                    mapper.save(curr);
                    curr.getUserName();
                    curr.setUserName(username);
                    curr.getUserGender();
                    curr.setUserGender(gender);
                    mapper.save(curr);
                }
                //UsersDO sb = mapper.load(UsersDO.class, someone);
//                if(sb.getUsersFollowers() == null){
//                    sb.setUsersFollowers(new ArrayList<String>());
//                }
                //sb.getUsersFollowers().remove(currUserID);
                //mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }
}
