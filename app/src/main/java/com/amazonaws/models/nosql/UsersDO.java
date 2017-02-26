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

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Users")

public class UsersDO {
    private String _userId;
    private Double _userAge;
    private String _userGender;
    private String _userPhoto;
    private String _userPost;
    private byte[] _userPrivacy;
    private List<String> _usersFollowers;
    private List<String> _usersFollowing;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "User_Age")
    public Double getUserAge() {
        return _userAge;
    }

    public void setUserAge(final Double _userAge) {
        this._userAge = _userAge;
    }
    @DynamoDBAttribute(attributeName = "User_Gender")
    public String getUserGender() {
        return _userGender;
    }

    public void setUserGender(final String _userGender) {
        this._userGender = _userGender;
    }
    @DynamoDBAttribute(attributeName = "User_Photo")
    public String getUserPhoto() {
        return _userPhoto;
    }

    public void setUserPhoto(final String _userPhoto) {
        this._userPhoto = _userPhoto;
    }
    @DynamoDBAttribute(attributeName = "User_Post")
    public String getUserPost() {
        return _userPost;
    }

    public void setUserPost(final String _userPost) {
        this._userPost = _userPost;
    }
    @DynamoDBAttribute(attributeName = "User_Privacy")
    public byte[] getUserPrivacy() {
        return _userPrivacy;
    }

    public void setUserPrivacy(final byte[] _userPrivacy) {
        this._userPrivacy = _userPrivacy;
    }
    @DynamoDBAttribute(attributeName = "Users_Followers")
    public List<String> getUsersFollowers() {
        return _usersFollowers;
    }

    public void setUsersFollowers(final List<String> _usersFollowers) {
        this._usersFollowers = _usersFollowers;
    }
    @DynamoDBAttribute(attributeName = "Users_Following")
    public List<String> getUsersFollowing() {
        return _usersFollowing;
    }

    public void setUsersFollowing(final List<String> _usersFollowing) {
        this._usersFollowing = _usersFollowing;
    }

}
