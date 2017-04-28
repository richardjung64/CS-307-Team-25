package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Clothing")

public class ClothingDO {
    private String _userId;
    private String _clothingBrand;
    private String _clothingColor;
    private String _clothingDescription;
    private Map<String, String> _clothingComments;
    private Double _clothingOwned;
    private Double _clothingLikes;
    private String _clothingPhotoLink;
    private String _clothingPrice;
    private String _clothingStore;
    private boolean _clothingAvailability;
    private Map<String, String> _clothingTags;
    private String _clothingType;
    private String _clothingYear;
    private List<String> _likedUser;
    private List<String> _ownedUser;
    private String old_price;

    @DynamoDBAttribute(attributeName = "old_price")
    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Brand")
    public String getClothingBrand() {
        return _clothingBrand;
    }

    public void setClothingBrand(final String _clothingBrand) {
        this._clothingBrand = _clothingBrand;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Color")
    public String getClothingColor() {
        return _clothingColor;
    }

    public void setClothingColor(final String _clothingColor) {
        this._clothingColor = _clothingColor;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Comments")
    public Map<String, String> getClothingComments() {
        return _clothingComments;
    }

    public void setClothingComments(final Map<String, String> _clothingComments) {
        this._clothingComments = _clothingComments;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Owned")
    public Double getClothingOwned() {
        return _clothingOwned;
    }

    public void setClothingOwned(final Double _clothingOwned) {
        this._clothingOwned = _clothingOwned;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Likes")
    public Double getClothingLikes() {
        return _clothingLikes;
    }

    public void setClothingLikes(final Double _clothingLikes) {
        this._clothingLikes = _clothingLikes;
    }
    @DynamoDBAttribute(attributeName = "Clothing_PhotoLink")
    public String getClothingPhotoLink() {
        return _clothingPhotoLink;
    }

    public void setClothingPhotoLink(final String _clothingPhotoLink) {
        this._clothingPhotoLink = _clothingPhotoLink;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Price")
    public String getClothingPrice() {
        return _clothingPrice;
    }

    public void setClothingPrice(final String _clothingPrice) {
        this._clothingPrice = _clothingPrice;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Store")
    public String getClothingStore() {
        return _clothingStore;
    }

    public void setClothingStore(final String _clothingStore) {
        this._clothingStore = _clothingStore;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Tags")
    public Map<String, String> getClothingTags() {
        return _clothingTags;
    }

    public void setClothingTags(final Map<String, String> _clothingTags) {
        this._clothingTags = _clothingTags;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Type")
    public String getClothingType() {
        return _clothingType;
    }

    public void setClothingType(final String _clothingType) {
        this._clothingType = _clothingType;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Year")
    public String getClothingYear() {
        return _clothingYear;
    }

    public void setClothingYear(final String _clothingYear) {
        this._clothingYear = _clothingYear;
    }
    @DynamoDBAttribute(attributeName = "Liked_User")
    public List<String> getLikedUser() {
        return _likedUser;
    }

    public void setLikedUser(final List<String> _likedUser) {
        this._likedUser = _likedUser;
    }
    @DynamoDBAttribute(attributeName = "Owned_User")
    public List<String> getOwnedUser() {
        return _ownedUser;
    }

    public void setOwnedUser(final List<String> _ownedUser) {
        this._ownedUser = _ownedUser;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Availability")
    public boolean getClothingAvailability() {
        return _clothingAvailability;
    }

    public void setClothingAvailability(boolean _clothingAvailability) {
        this._clothingAvailability = _clothingAvailability;
    }
    @DynamoDBAttribute(attributeName = "Clothing_Description")
    public String getClothingDescription() {
        return _clothingDescription;
    }

    public void setClothingDescription(String _clothingDescription) {
        this._clothingDescription = _clothingDescription;
    }
}
