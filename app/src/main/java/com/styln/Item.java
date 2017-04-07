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
    private String brand;
    private String description;
    private double price;
    private boolean availability;

    private String Tag;

    private int numLikes, numOwned, rank;
    private URL storeLink;

    private String[] comments;


    public Item() {
    }

    public Item(String name, String brand,int sku, int image) {
        this.name = name;
        this.brand = brand;
        this.sku = sku;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSKU() {
        return sku;
    }

    public void setSKU(int sku) {
        this.sku = sku;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }



}
