package com.styln;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataAccessNotAuthorizedException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ClothingDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostActivity extends AppCompatActivity {

    private static final String LOG_TAG = PostActivity.class.getSimpleName();

    private Context mContext;
    private List<ClothingDO> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostActivityItemsAdapter iAdapter;
    private EditText pDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mContext = getApplicationContext();
        pDescription = (EditText)findViewById(R.id.post_description);

        getWardrobe task = new getWardrobe();
        String currUserID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        task.id = currUserID;
        try {
            itemList = task.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        iAdapter = new PostActivityItemsAdapter(this, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

    }

    public void Post(View view) {
        Log.d("", "Confirm Post");
        Log.d("Array:", iAdapter.postList.toString());
        String description = pDescription.getText().toString();
        List<String> postList = iAdapter.postList;
        DataAction da = new DataAction();
        da.post(description,postList);

        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(PostActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void Cancel(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(PostActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    private class getWardrobe extends AsyncTask<String, Void, List<ClothingDO>> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        List<ClothingDO> loadresult = new ArrayList<ClothingDO>();
        String id;
        @Override
        protected List<ClothingDO> doInBackground(String... strings) {
            UsersDO currentUser;
            String userID = id;
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


}
