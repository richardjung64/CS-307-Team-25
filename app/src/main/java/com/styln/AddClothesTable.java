package com.styln;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.unmarshallers.IntegerSetUnmarshaller;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.Arrays;
import java.util.Random;

/**
 * Created on 4/7/17.
 */

public class AddClothesTable {
    private static final int ITEMS = 6;
    private DynamoDBMapper mapper;
    public AmazonClientException lastException;
    final String LOG_TAG = AddClothesTable.class.getSimpleName();

    public AddClothesTable() {
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }

    public void addClothes() {
        Random random = new Random();
        final ClothingDO[] items_list = new ClothingDO[ITEMS - 1];
        //AmazonS3 s3 = new AmazonS3Client(credentialsProvider);;
        for (int count = 0; count < ITEMS - 1; count++) {
            final ClothingDO item = new ClothingDO();
            item.setUserId("cloth_usr_" + Integer.toString(count));
            item.setClothingBrand("brand " + Integer.toString(1 + random.nextInt(4)));
            item.setClothingColor("color" + Integer.toString(1 + random.nextInt(4)));
            item.setClothingPhotoLink("");
            item.setClothingLikes((double) 0);
            String price = Integer.toString(30 + random.nextInt(60));
            item.setClothingPrice(price);
            item.setOld_price(price);
            item.setClothingStore("Store" + Integer.toString(1 + random.nextInt(4)));

            items_list[count] = item;
        }
        try {
            mapper.batchSave(Arrays.asList(items_list));
            Log.i(LOG_TAG, "Added clothes...");
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item batch : " + ex.getMessage(), ex);
            lastException = ex;
        }

        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            throw lastException;
        }
    }
}