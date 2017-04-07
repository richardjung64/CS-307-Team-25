package com.amazonaws.models.nosql;

import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import org.w3c.dom.UserDataHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Users")

public class UsersDO {
    private String _userId;
    private Double _userAge;
    private String _userDescription;
    private String _userGender;
    private String _userName;
    private String _userPhoto;
    private List<String> _userPosts;
    private byte[] _userPrivacy;
    private Set<String> _usersFollowers;
    private Set<String> _usersFollowing;

    public UsersDO()
    {;
    }
    public UsersDO(String name)
    {
        _userName   = name;
    }
    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        this._userId = userId;
    }
    @DynamoDBAttribute(attributeName = "User_Age")
    public Double getUserAge() {
        return _userAge;
    }

    public void setUserAge(final Double _userAge) {
        this._userAge = _userAge;
    }
    @DynamoDBAttribute(attributeName = "User_Description")
    public String getUserDescription() {
        return _userDescription;
    }

    public void setUserDescription(final String _userDescription) {
        this._userDescription = _userDescription;
    }
    @DynamoDBAttribute(attributeName = "User_Gender")
    public String getUserGender() {
        return _userGender;
    }

    public void setUserGender(final String _userGender) {
        this._userGender = _userGender;
    }
    @DynamoDBAttribute(attributeName = "User_Name")
    public String getUserName() {
        return _userName;
    }

    public void setUserName(final String _userName) {
        this._userName = _userName;
    }
    @DynamoDBAttribute(attributeName = "User_Photo")
    public String getUserPhoto() {
        return _userPhoto;
    }

    public void setUserPhoto(final String _userPhoto) {
        this._userPhoto = _userPhoto;
    }
    @DynamoDBAttribute(attributeName = "User_Posts")
    public List<String> getUserPosts() {
        return _userPosts;
    }

    public void setUserPosts(final List<String> _userPosts) {
        this._userPosts = _userPosts;
    }
    @DynamoDBAttribute(attributeName = "User_Privacy")
    public byte[] getUserPrivacy() {
        return _userPrivacy;
    }

    public void setUserPrivacy(final byte[] _userPrivacy) {
        this._userPrivacy = _userPrivacy;
    }
    @DynamoDBAttribute(attributeName = "Users_Followers")
    public Set<String> getUsersFollowers() {
        return _usersFollowers;
    }

    public void setUsersFollowers(final Set<String> _usersFollowers) {
        this._usersFollowers = _usersFollowers;
    }
    @DynamoDBAttribute(attributeName = "Users_Following")
    public Set<String> getUsersFollowing() {
        return _usersFollowing;
    }

    public void setUsersFollowing(final Set<String> _usersFollowing) {
        this._usersFollowing = _usersFollowing;
    }

}
