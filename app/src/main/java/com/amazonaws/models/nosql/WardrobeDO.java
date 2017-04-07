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

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Wardrobe")

public class WardrobeDO {
    private String _wardrobeID;
    private Set<String> _clothesID;
    private String _userStoreID;

    @DynamoDBHashKey(attributeName = "WardrobeID")
    @DynamoDBAttribute(attributeName = "WardrobeID")
    public String getWardrobeID() {
        return _wardrobeID;
    }

    public void setWardrobeID(final String _wardrobeID) {
        this._wardrobeID = _wardrobeID;
    }
    @DynamoDBAttribute(attributeName = "ClothesID")
    public Set<String> getClothesID() {
        return _clothesID;
    }

    public void setClothesID(final Set<String> _clothesID) {
        this._clothesID = _clothesID;
    }
    @DynamoDBAttribute(attributeName = "User_Store_ID")
    public String getUserStoreID() {
        return _userStoreID;
    }

    public void setUserStoreID(final String _userStoreID) {
        this._userStoreID = _userStoreID;
    }

}
