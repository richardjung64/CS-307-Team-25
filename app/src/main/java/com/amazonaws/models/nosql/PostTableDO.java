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

@DynamoDBTable(tableName = "stylin-mobilehub-1048106400-Post_Table")

public class PostTableDO {
    private String _userId;
    private String _postDescription;
    private Double _postLikes;
    private String _postPhotoLink;
    private String _postPoster;
    private Map<String, String> _postTags;

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
    @DynamoDBAttribute(attributeName = "Post_PhotoLink")
    public String getPostPhotoLink() {
        return _postPhotoLink;
    }

    public void setPostPhotoLink(final String _postPhotoLink) {
        this._postPhotoLink = _postPhotoLink;
    }
    @DynamoDBAttribute(attributeName = "Post_Poster")
    public String getPostPoster() {
        return _postPoster;
    }

    public void setPostPoster(final String _postPoster) {
        this._postPoster = _postPoster;
    }
    @DynamoDBAttribute(attributeName = "Post_Tags")
    public Map<String, String> getPostTags() {
        return _postTags;
    }

    public void setPostTags(final Map<String, String> _postTags) {
        this._postTags = _postTags;
    }

}
