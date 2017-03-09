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
    private String _cLothingPhotoLink;
    private String _clothingBrand;
    private String _clothingColor;
    private String _clothingWebLink;
    private Double _clothingDislikes;
    private Double _clothingLikes;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "CLothing_PhotoLink")
    public String getCLothingPhotoLink() {
        return _cLothingPhotoLink;
    }

    public void setCLothingPhotoLink(final String _cLothingPhotoLink) {
        this._cLothingPhotoLink = _cLothingPhotoLink;
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
    @DynamoDBAttribute(attributeName = "Clothing_WebLink")
    public String getClothingWebLink() {
        return _clothingWebLink;
    }

    public void setClothingWebLink(final String _clothingWebLink) {
        this._clothingWebLink = _clothingWebLink;
    }
    @DynamoDBAttribute(attributeName = "Clothing_dislikes")
    public Double getClothingDislikes() {
        return _clothingDislikes;
    }

    public void setClothingDislikes(final Double _clothingDislikes) {
        this._clothingDislikes = _clothingDislikes;
    }
    @DynamoDBAttribute(attributeName = "Clothing_likes")
    public Double getClothingLikes() {
        return _clothingLikes;
    }

    public void setClothingLikes(final Double _clothingLikes) {
        this._clothingLikes = _clothingLikes;
    }

}
