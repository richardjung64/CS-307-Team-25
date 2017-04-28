package com.styln;

import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shalingyi on 4/7/17.
 */

public class DataAction {
    /*
@Param: someone is the userid of the person current user want to follow;
 */

    public static String postID;

    public DataAction() {
    }

    public void followSomeone(final String someone) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                if (curr.getUserFollowing() == null) {
                    curr.setUserFollowing(new ArrayList<String>());
                }
                curr.getUserFollowing().add(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                if (sb.getUserFollower() == null) {
                    sb.setUserFollower(new ArrayList<String>());
                }
                sb.getUserFollower().add(currUserID);
                double new_followers = sb.getNew_followers();
                new_followers++;
                sb.setNew_followers(new_followers);
                mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

    public void unfollowSomeone(final String someone) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                if (curr.getUserFollowing() == null) {
                    curr.setUserFollowing(new ArrayList<String>());
                }
                curr.getUserFollowing().remove(someone);
                mapper.save(curr);
                UsersDO sb = mapper.load(UsersDO.class, someone);
                if (sb.getUserFollower() == null) {
                    sb.setUserFollower(new ArrayList<String>());
                }
                sb.getUserFollower().remove(currUserID);
                mapper.save(sb);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

    public void user_dp(final String s3_link) {
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

    public void UserChanges(final String username, final String age, final boolean isPrivate, final String description, final String gender) {
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


    public void likeClothing(final String someItem) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                ClothingDO sth = mapper.load(ClothingDO.class, someItem);

                if (sth.getLikedUser() == null) {
                    sth.setLikedUser(new ArrayList<String>());
                }
                if (sth.getLikedUser().contains(currUserID)) {
                    sth.setClothingLikes(sth.getClothingLikes() - 1);
                    sth.getLikedUser().remove(currUserID);
                } else {
                    sth.setClothingLikes(sth.getClothingLikes() + 1);
                    sth.getLikedUser().add(currUserID);
                }
                mapper.save(sth);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

    public void ownClothing(final String someItem) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                ClothingDO sth = mapper.load(ClothingDO.class, someItem);

                if (sth.getOwnedUser() == null) {
                    sth.setOwnedUser(new ArrayList<String>());
                }

                if (curr.getUserWardrobe() == null) {
                    curr.setUserWardrobe(new ArrayList<String>());
                }

                if (sth.getOwnedUser().contains(currUserID)) {
                    sth.setClothingOwned(sth.getClothingOwned() - 1);
                    sth.getOwnedUser().remove(currUserID);
                    curr.getUserWardrobe().remove(sth.getUserId());
                } else {
                    sth.setClothingOwned(sth.getClothingOwned() + 1);
                    sth.getOwnedUser().add(currUserID);
                    curr.getUserWardrobe().add(sth.getUserId());

                }
                mapper.save(curr);
                mapper.save(sth);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

    public void wishClothing(final String someItem) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                ClothingDO sth = mapper.load(ClothingDO.class, someItem);


                if (curr.getUserWishList() == null) {
                    curr.setUserWishList(new ArrayList<String>());
                }

                if (!curr.getUserWishList().contains(sth.getUserId())) {
                    curr.getUserWishList().add(sth.getUserId());
                } else {
                    curr.getUserWishList().remove(sth.getUserId());
                }

                mapper.save(curr);
                mapper.save(sth);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();

    }

    public void likePost(final String somePost) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                PostTableDO spost = mapper.load(PostTableDO.class, somePost);
                UsersDO post_poster = mapper.load(UsersDO.class, spost.getPostPoster());
                try {
                    if (spost.getLikedUser() == null) {
                        spost.setLikedUser(new ArrayList<String>());
                    }
                }
                catch (Exception e) {
                    spost.setLikedUser(new ArrayList<String>());
                }
                if (spost.getLikedUser().contains(currUserID)) {
                    //Already liked, unlike
                    spost.setPostLikes(spost.getPostLikes() - 1);
                    spost.getLikedUser().remove(currUserID);
                    List<String> liskedPost = post_poster.getPosts_liked();
                    if (liskedPost.contains(spost.getUserId()))
                        liskedPost.remove(spost.getUserId());

                } else {
                    //Like
                    spost.setPostLikes(spost.getPostLikes() + 1);
                    spost.getLikedUser().add(currUserID);
                    List<String> liskedPost = curr.getPosts_liked();
                    if (liskedPost == null)
                        liskedPost = new ArrayList<>();
                    liskedPost.add(spost.getUserId());
                    post_poster.setPosts_liked(liskedPost);
                }
                mapper.save(post_poster);
                mapper.save(spost);
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
        postID = somePost;
    }

    public void post(final String description, final List<String> list) {
        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                DateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                Date dateobj = new Date();
                String postTime = df.format(dateobj);

                PostTableDO post = mapper.load(PostTableDO.class, postTime);
                UsersDO curr = mapper.load(UsersDO.class, currUserID);
                if (post == null) {
                    Log.d("", "POST");
                    post = new PostTableDO();
                    post.setUserId(df.format(dateobj));
                    post.setPostPoster(curr.getUserId());
                    post.setPostDate(df.format(dateobj));
                    post.setPostDescription(description);
                    list.toString();
                    post.setPostClothing(list);
                    post.setPostLikes(0.0);
                    post.setLikedUser(new ArrayList<String>());
                    mapper.save(post);

                    try {
                        curr.getUserPosts().add(post.getUserId());
                    } catch (Exception e) {
                        List<String> posts = new ArrayList<>();
                        posts.add(post.getUserId());
                        curr.setUserPosts(posts);
                    }

                    mapper.save(curr);
                }

            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

}
