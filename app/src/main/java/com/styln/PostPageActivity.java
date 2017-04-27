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
import com.amazonaws.models.nosql.PostTableDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostPageActivity extends AppCompatActivity {

    private static final String LOG_TAG = PostPageActivity.class.getSimpleName();
    private String Pid;
    private List<PostTableDO> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomePostsAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);

        Pid = getIntent().getStringExtra("ID");

        grabPost task = new grabPost();
        PostTableDO post = new PostTableDO();
        try {
            task.id = Pid;
            post = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        postList.add(post);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pAdapter = new HomePostsAdapter(this,postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

    }

    private class grabPost extends AsyncTask<String, Void, PostTableDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        PostTableDO loadresult = new PostTableDO();
        String id = "";
        @Override
        protected PostTableDO doInBackground(String... strings) {
            PostTableDO post = new PostTableDO();
            String PID = id;
            post = mapper.load(PostTableDO.class, PID);
            loadresult = post;
            return loadresult;
        }
    }


    public void openHome(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(PostPageActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openTrend(View view) {
        Log.d(LOG_TAG, "Launching Trend Activity...");
        startActivity(new Intent(PostPageActivity.this, TrendActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("KEY","ITEM"));
        // finish should always be called on the main thread.
        finish();
    }

    public void openPost(View view) {
        Log.d(LOG_TAG, "Launching Post Activity...");
        startActivity(new Intent(PostPageActivity.this, PostActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openBrowse(View view) {
        Log.d(LOG_TAG, "Launching Browse Activity...");
        startActivity(new Intent(PostPageActivity.this, BrowseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    public void openProfile(View view) {
        Log.d(LOG_TAG, "Launching Profile Activity...");
        startActivity(new Intent(PostPageActivity.this, ProfileActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }
}
