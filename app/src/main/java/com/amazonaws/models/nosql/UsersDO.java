package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Users")

public class UsersDO {
    private String _userId;
    private String _userAge;
    private String _userDescription;
    private String _userGender;
    private String _userName;
    private String _userPhoto;
    private List<String> _userPosts;
    private boolean _userPrivacy;
    public List<String> _userFollower;
    public List<String> _userFollowing;
    private List<String> _userWardrobe;
    private List<String> _userWishList;
    private boolean isFirstTime;
    private boolean hasCustomDp;
    private boolean login_opt;

    @DynamoDBAttribute(attributeName = "login_opt")
    public boolean isLogin_opt() {
        return login_opt;
    }

    public void setLogin_opt(boolean login_opt) {
        this.login_opt = login_opt;
    }

    @DynamoDBAttribute(attributeName = "hasCustomDp")
    public boolean isHasCustomDp() {
        return hasCustomDp;
    }

    public void setHasCustomDp(boolean hasCustomDp) {

        this.hasCustomDp = hasCustomDp;
    }

    @DynamoDBAttribute(attributeName = "isFirstTime")
    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
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
    public String getUserAge() {
        return _userAge;
    }

    public void setUserAge(final String _userAge) {
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
    public boolean getUserPrivacy() {
        return _userPrivacy;
    }

    public void setUserPrivacy(final boolean _userPrivacy) {
        this._userPrivacy = _userPrivacy;
    }
    @DynamoDBAttribute(attributeName = "User_Follower")
    public List<String> getUserFollower() {
        return _userFollower;
    }

    public void setUserFollower(final List<String> _userFollower) {
        this._userFollower = _userFollower;
    }
    @DynamoDBAttribute(attributeName = "User_Following")
    public List<String> getUserFollowing() {
        return _userFollowing;
    }

    public void setUserFollowing(final List<String> _userFollowing) {
        this._userFollowing = _userFollowing;
    }
    @DynamoDBAttribute(attributeName = "User_Wardrobe")
    public List<String> getUserWardrobe() {
        return _userWardrobe;
    }

    public void setUserWardrobe(final List<String> _userWardrobe) {
        this._userWardrobe = _userWardrobe;
    }
    @DynamoDBAttribute(attributeName = "User_Wishlist")
    public List<String> getUserWishList() {
        return _userWishList;
    }

    public void setUserWishList(final List<String> _userWishList) {
        this._userWishList = _userWishList;
    }

}
