package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Post_Table")

public class PostTableDO {
    private String _userId;
    private String _postDescription;
    private Double _postLikes;
    private String _postUser;
    private List<String> _postClothing;
    private List<String> _likedUser;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "Post_Description")
    public String getPostDescription() {
        return _postDescription;
    }

    public void setPostDescription(final String _postDescription) {
        this._postDescription = _postDescription;
    }
    @DynamoDBAttribute(attributeName = "Post_Likes")
    public Double getPostLikes() {
        return _postLikes;
    }

    public void setPostLikes(final Double _postLikes) {
        this._postLikes = _postLikes;
    }
    @DynamoDBAttribute(attributeName = "Post_User")
    public String getPostPoster() {
        return _postUser;
    }

    public void setPostPoster(final String _postPoster) {
        this._postUser = _postPoster;
    }
    @DynamoDBAttribute(attributeName = "Liked_User")
    public List<String> getLikedUser() {
        return _likedUser;
    }

    public void setLikedUser(final List<String> _likedUser) {
        this._likedUser = _likedUser;
    }
    @DynamoDBAttribute(attributeName = "Post_Clothing")
    public List<String> getPostClothing() {
        return _postClothing;
    }

    public void setPostClothing(final List<String> _postClothing) {
        this._postClothing = _postClothing;
    }
}
