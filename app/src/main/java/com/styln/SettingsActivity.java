package com.styln;

import android.content.DialogInterface;
import android.content.Intent;
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

public class SettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    private IdentityManager identityManager;
    private TextView userName;
    static boolean checked = false;
    private ImageView profilePic;


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
}
