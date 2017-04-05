package com.styln;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

import java.net.URL;

/**
 * Created by shalingyi on 4/5/17.
 */

public class Item {
    private String name;
    private int image;
    private int sku;
    private String description;
    private double price;
    private boolean availability;

    private String[] colors;

    private int numLikes, numOwned;
    private URL storeLink;

    private String[] comments;


    public Item() {
    }

    public Item(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
