package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CollectionActivity extends AppCompatActivity {


    private static final String LOG_TAG = FollowActivity.class.getSimpleName();
    private String pageKey;

    private List<ClothingDO> Wardrobe = new ArrayList<>();
    private List<ClothingDO> Wishlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private CollectionItemsAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        pageKey = getIntent().getStringExtra("KEY");
        Log.d(LOG_TAG, "Opened from " + pageKey);



        if(pageKey.equals("wardrobe")){
            getWardrobe task = new getWardrobe();
            try {
                Wardrobe = task.execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            iAdapter = new CollectionItemsAdapter(this, Wardrobe);
        } else {
            getWishlist task = new getWishlist();
            try {
                Wishlist = task.execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            iAdapter = new CollectionItemsAdapter(this,Wishlist);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

    }


    public void back(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(CollectionActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        // finish should always be called on the main thread.
        finish();
    }


    private class getWardrobe extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            List<String> tempSet;

            if(currentUser.getUserWardrobe() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUserWardrobe(new ArrayList<String>());
            }

            tempSet = currentUser.getUserWardrobe();
            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                ClothingDO iterator = mapper.load(ClothingDO.class, str);
                loadresult.add(iterator);

            }
            return loadresult;
        }
    }

    private class getWishlist extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            List<String> tempSet;

            if(currentUser.getUserWishList() == null){
                Log.d(LOG_TAG,"NULLLLL");
                currentUser.setUserWishList(new ArrayList<String>());
            }

            tempSet = currentUser.getUserWishList();
            List<String> result = new ArrayList<String>(tempSet);

            for (String str : result) {
                ClothingDO iterator = mapper.load(ClothingDO.class, str);
                loadresult.add(iterator);

            }
            return loadresult;
        }
    }


    public void refreshWardrobe(View view) {
        startActivity(new Intent(CollectionActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wardrobe"));
        finish();
    }
    public void refreshWishlist(View view) {
        startActivity(new Intent(CollectionActivity.this, CollectionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","wishlist"));
        finish();
    }
}
