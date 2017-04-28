package com.styln;

import android.util.Log;

import com.amazonaws.models.nosql.UsersDO;

import java.util.Comparator;

/**
 * Created by Kevin Jiang on 4/28/2017.
 */

public class FollowersComparator implements Comparator<UsersDO> {
        @Override
        public int compare(UsersDO c1, UsersDO c2){
            //Log.d("ASD", Integer.toString(c1.getUserFollower().size()));
            return (c1.getUserFollower().size() - c2.getUserFollower().size());

        }
    }