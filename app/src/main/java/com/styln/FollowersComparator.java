package com.styln;

import com.amazonaws.models.nosql.UsersDO;

import java.util.Comparator;

/**
 * Created by Kevin Jiang on 4/28/2017.
 */

public class FollowersComparator implements Comparator<UsersDO> {
        @Override
        public int compare(UsersDO c1, UsersDO c2){
            if(c1.getUserFollower().size() > (c2.getUserFollower().size()))
                return 1;
            if(c1.getUserFollower().size() < (c2.getUserFollower().size()))
                return -1;
            return 0;
        }
    }