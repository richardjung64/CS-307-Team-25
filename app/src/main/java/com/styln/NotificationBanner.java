package com.styln;

import android.content.Context;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.PostTableDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by shanu on 4/24/17.
 */

public class NotificationBanner {
    private ArrayList<Notifications> notifyList;
    private static NotificationBanner sBanner; // Android convention to denote static variable
    private Context appContext;

    public void getPost() {
        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                if (DataAction.postID == null) {
                    notifyList = null;
                    return;
                }
                PostTableDO spost = mapper.load(PostTableDO.class, DataAction.postID);
                if (spost!= null && spost.getPostPoster().equals(AWSMobileClient.defaultMobileClient().
                        getIdentityManager().getCachedUserID())) {

                    if (spost.getLikedUser() != null) {
                        String userIds[] = new String[spost.getLikedUser().size()];
                        String userNames[] = new String[spost.getLikedUser().size()];
                        for (int i = 0; i < userIds.length; i++) {
                            userIds[i] = spost.getLikedUser().get(i);
                            UsersDO thisUser = mapper.load(UsersDO.class, userIds[i]);
                            userNames[i] = thisUser.getUserName();
                            Notifications likes = new LikePost();
                            likes.notify_user(userNames[i]);
                            notifyList.add(likes);
                        }
                    }
                }
                else {
                    notifyList = null;
                    return;
                }

            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }

    private NotificationBanner(Context appContext) {
        this.appContext = appContext;
        notifyList = new ArrayList<Notifications>();
        getPost();
    }

    public static NotificationBanner get(Context c) {
        if(sBanner == null)
            sBanner = new NotificationBanner(c.getApplicationContext());
        return sBanner;
    }

    public ArrayList<Notifications> getNotificationList() {
        return notifyList;
    }

    public Notifications getNotifications(UUID id) {
        for(Notifications c : notifyList)
            if(c.getUuid().equals(id))
                return c;
        return null;
    }
}
