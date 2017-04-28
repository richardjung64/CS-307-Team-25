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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BrowseActivity extends AppCompatActivity {

    private static final String LOG_TAG = BrowseActivity.class.getSimpleName();
    private EditText to_search;
    private RadioGroup category;
    private RadioButton cat_button;
    private RadioGroup choice_group;
    private String query_string;
    private String search_choice;
    private RecyclerView recyclerView;
    private SearchListItemsAdapter iAdapter;
    private TrendUsersAdapter uAdapter;

    private final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        to_search = (EditText) findViewById(R.id.search_textbox);
        choice_group = (RadioGroup) findViewById(R.id.searchGroup);

        recyclerView = (RecyclerView) findViewById(R.id._recycler_view);
    }

    private class GetUsers extends AsyncTask<String, Void, PaginatedScanList<UsersDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;
        @Override
        protected PaginatedScanList<UsersDO> doInBackground(String... strings) {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<UsersDO> result = mapper.scan(UsersDO.class, scanExpression);
            if (result == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return result;
        }
    }

    private class GetClothes extends AsyncTask<String, Void, PaginatedScanList<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        String id;
        @Override
        protected PaginatedScanList<ClothingDO> doInBackground(String... strings) {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<ClothingDO> result = mapper.scan(ClothingDO.class, scanExpression);
            if (result == null)
                Log.e(LOG_TAG, "Search failed..");
            else
                Log.e(LOG_TAG, "Search successful..");
            return result;
        }
    }

    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(BrowseActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(BrowseActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","ITEM"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
        Log.d(LOG_TAG, "Launching Post Activity...");
        startActivity(new Intent(BrowseActivity.this, PostActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openBrowse(View view) {
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(BrowseActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    private void search_user(final String query)  {

    }

    public void search_item(View view) {
        query_string = to_search.getText().toString().toLowerCase();
        if (query_string.trim().equals("")) {
            Toast.makeText(BrowseActivity.this, "Nothing to search", Toast.LENGTH_SHORT).show();
            return;
        }
        int select = choice_group.getCheckedRadioButtonId();
        cat_button = (RadioButton) findViewById(select);
        search_choice = cat_button.getText().toString();
        if (search_choice.equals("User")) {
            GetUsers task = new GetUsers();
            PaginatedScanList<UsersDO> user_result = null;
            try {
                user_result = task.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            List<UsersDO> selected_users = new ArrayList<>();
            if (user_result != null) {
                for (int i = 0; i < user_result.size(); i++) {
                    //Log.d(LOG_TAG, "Search is successful");
                    UsersDO user = user_result.get(i);
                    if (user.getUserName().toLowerCase().contains(query_string))
                        selected_users.add(user);
                }
                uAdapter = new TrendUsersAdapter(this, selected_users);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(uAdapter);
            }
        } else {
            GetClothes _task = new GetClothes();
            PaginatedScanList<ClothingDO> clothes_result = null;
            try {
                clothes_result = _task.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            List<ClothingDO> selected_clothes = new ArrayList<>();
            if (clothes_result != null) {
                for (int i = 0; i < clothes_result.size(); i++) {
                    //Log.d(LOG_TAG, "Search is successful");
                    ClothingDO cloth_item = clothes_result.get(i);
                    try {
                        if (cloth_item.getUserId().toLowerCase().contains(query_string) || cloth_item.getClothingBrand().toLowerCase().contains(query_string)
                                || cloth_item.getClothingDescription().toLowerCase().contains(query_string) || cloth_item.getClothingType().toLowerCase().contains(query_string)
                                || cloth_item.getClothingColor().toLowerCase().contains(query_string))
                            selected_clothes.add(cloth_item);
                    }
                    catch (Exception e) {
                        continue;
                    }
                }
                iAdapter = new SearchListItemsAdapter(this, selected_clothes);RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(iAdapter);

            }
        }


    }
}