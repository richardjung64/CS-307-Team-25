package com.styln;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.PostTableDO;

import java.util.List;

/**
 * Created by shanu on 4/24/17.
 */

public class LikePost extends Notifications {
    private String title;
    public void notify_user(String userName) {
        title = userName + " liked your post";
    }

    public String getTitle() {
        return title;
    }
//    private List<String> follow_notf;
//    public void notify_like() {
//        final String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                //DynamoDB calls go here
//                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
//                PostTableDO spost = mapper.load(PostTableDO.class, HomePostsAdapter.post_id);
//                follow_notf = spost.getLikedUser();
//                if (follow_notf != null && !follow_notf.isEmpty()) {
//
//                }
////                    if(spost.getLikedUser() == null){
////                        spost.setLikedUser(new ArrayList<String>());
////                    }
////                    if(spost.getLikedUser().contains(currUserID)){
////                        //Already liked, unlike
////                        spost.setPostLikes(spost.getPostLikes()-1);
////                        spost.getLikedUser().remove(currUserID);
////                    } else {
////                        //Like
////                        spost.setPostLikes(spost.getPostLikes()+1);
////                        spost.getLikedUser().add(currUserID);
////                    }
////                    mapper.save(spost);
//            }
//        };
//        Thread thr = new Thread(runnable);
//        thr.start();
//    }
}
