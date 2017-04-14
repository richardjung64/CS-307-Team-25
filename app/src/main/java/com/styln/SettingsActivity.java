package com.styln;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName;
    static boolean checked = false;
    private ImageView profilePic;

    private class GrabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            loadresult = currentUser;
            mapper.delete(loadresult);
            return loadresult;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();
        identityManager = awsMobileClient.getIdentityManager();
    }

    public void back(View view) {
        Log.d(LOG_TAG, "Launching Home Activity...");
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }


    public void signOut(View view) {
        identityManager.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
        finish();
        return;
    }


    public void changeProfile(View view) {
        Intent intent= new Intent(SettingsActivity.this, InformationActivity.class);
        intent.putExtra("ID",AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        Log.d(LOG_TAG,AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        startActivity(intent);
        return;
    }

    public void deleteAccount(View view) {
        GrabUser grabUser = new GrabUser();
        UsersDO thisUser;
        try {
             thisUser = grabUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
    }
}
