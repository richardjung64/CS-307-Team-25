package com.styln;

import android.media.Image;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shalingyi on 4/5/17.
 */

public class User {

    private String name;
    private int age;
    private int gender;
    private String description;
    private URL email;
    private Image displayPicture;
    private boolean isAccountPrivate;

    private List<User> followerList = new ArrayList<>();
    private List<User> followingList = new ArrayList<>();
    private List<Item> Wardrobe = new ArrayList<>();
    private List<Item> Wishlist = new ArrayList<>();

    public User() {
    }

    public User(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
