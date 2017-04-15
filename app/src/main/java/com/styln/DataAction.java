package com.styln;

import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;

/**
 * Created by shalingyi on 4/7/17.
 */

public class DataAction {
    /*
@Param: someone is the userid of the person current user want to follow;
 */

    public DataAction() {
    }

    public void followSomeone(final String someone){
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                if(curr.getUserFollowing() == null){
                    curr.setUserFollowing(new ArrayList<String>());
                }
                curr.getUserFollowing().add(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                if(sb.getUserFollower() == null){
                    sb.setUserFollower(new ArrayList<String>());
                }
                sb.getUserFollower().add(currUserID);
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
                if(curr.getUserFollowing() == null){
                    curr.setUserFollowing(new ArrayList<String>());
                }
                curr.getUserFollowing().remove(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                if(sb.getUserFollower() == null){
                    sb.setUserFollower(new ArrayList<String>());
                }
                sb.getUserFollower().remove(currUserID);
                mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

    public void user_dp (final String s3_link) {
        if (s3_link == null)
            Log.e(DataAction.class.getSimpleName(), "No link");
        Log.d(DataAction.class.getSimpleName(), "user dp method...");
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                Log.d(DataAction.class.getSimpleName(), "Here");
                final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                Log.d(DataAction.class.getSimpleName(), "Now Here-----");
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                Log.d(DataAction.class.getSimpleName(), "Now Here");
//                if(curr.getUsersFollowing() == null){
//                    curr.setUsersFollowing(new ArrayList<String>());
//                }
                //curr.getUsersFollowing().remove(someone);
                if (curr == null) {
                    Log.d(DataAction.class.getSimpleName(), "New user");
                    curr = new UsersDO();
                    curr.setUserId(currUserID);
                    curr.setUserAge("0");
                    curr.setUserDescription("");
                    curr.setUserPrivacy(false);
                    curr.setUserName("");
                    curr.setUserGender("");
                    curr.setHasCustomDp(true);
                    curr.setUserPhoto(s3_link);
                    if (Application.getSign_opt() == 'g')
                        curr.setLogin_opt(true); // True is google
                    else
                        curr.setLogin_opt(false);
                    mapper.save(curr);
                    Log.d(DataAction.class.getSimpleName(), "Add to table");
                }
                if (curr != null) {
                    curr.setUserPhoto(s3_link);
                    curr.setHasCustomDp(true);
                    mapper.save(curr);
                }
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

    public void UserChanges(final String username, final String age, final boolean isPrivate, final String description, final String gender){
        Log.d(DataAction.class.getSimpleName(), "UserChanges method...");
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


    public void likeClothing(final String someItem){
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                ClothingDO sth = mapper.load(ClothingDO.class, someItem);
                sth.setClothingLikes(sth.getClothingLikes()+1);
                mapper.save(sth);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

    public void ownClothing(final String someItem){
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                ClothingDO sth = mapper.load(ClothingDO.class, someItem);
                sth.setClothingOwned(sth.getClothingOwned()+1);
                mapper.save(sth);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

}