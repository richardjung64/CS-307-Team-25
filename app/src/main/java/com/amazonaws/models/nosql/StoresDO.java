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

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Stores")

public class StoresDO {
    private String _storeID;
    private String _storeDescription;
    private String _storeUsername;
    private String _wardrobeID;

    @DynamoDBHashKey(attributeName = "StoreID")
    @DynamoDBAttribute(attributeName = "StoreID")
    public String getStoreID() {
        return _storeID;
    }

    public void setStoreID(final String _storeID) {
        this._storeID = _storeID;
    }
    @DynamoDBAttribute(attributeName = "Store_Description")
    public String getStoreDescription() {
        return _storeDescription;
    }

    public void setStoreDescription(final String _storeDescription) {
        this._storeDescription = _storeDescription;
    }
    @DynamoDBAttribute(attributeName = "Store_Username")
    public String getStoreUsername() {
        return _storeUsername;
    }

    public void setStoreUsername(final String _storeUsername) {
        this._storeUsername = _storeUsername;
    }
    @DynamoDBAttribute(attributeName = "WardrobeID")
    public String getWardrobeID() {
        return _wardrobeID;
    }

    public void setWardrobeID(final String _wardrobeID) {
        this._wardrobeID = _wardrobeID;
    }

}
