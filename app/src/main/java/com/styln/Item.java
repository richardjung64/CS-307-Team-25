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
    private ImageView image;
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

    public Item(String name) {
        this.name = name;
        this.image.setImageResource(R.drawable.item_1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

}
