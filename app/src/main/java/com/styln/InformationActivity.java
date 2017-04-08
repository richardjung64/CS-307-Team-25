package com.styln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.RadioGroup;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jungr on 4/4/17.
 */

public class InformationActivity extends AppCompatActivity{
    String userID = "NOT_VALID";
    CheckBox privacy;
    ImageView picture;
    Button confirm,cancel,changePic;
    EditText nameText,ageText,descriptionText;
    String userGender;
    Spinner gender;
    Spinner selectedGender;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    int privateCounter = 0;
    boolean isPrivate;



    String name,age;
    String genderIdentity;
    String userDescription;


    private static String LOG_TAG = InformationActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //user age
        setContentView(R.layout.activity_information);

        confirm = (Button) findViewById(R.id.change_confirm);
        cancel = (Button) findViewById(R.id.change_cancel);
        picture = (ImageView)findViewById(R.id.change_picture);
        changePic = (Button)findViewById(R.id.change_picture_button);
        nameText = (EditText)findViewById(R.id.change_name);
        ageText = (EditText)findViewById(R.id.change_age);
        descriptionText = (EditText)findViewById(R.id.change_description);

        //TODO loads userinfo
        userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        if(userID == null){

        } else {
            //LOADS user info;


            grabUser task = new grabUser();
            UsersDO currentUser = new UsersDO();
            try {
                currentUser = task.execute("String").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            nameText.setText(currentUser.getUserName());
            ageText.setText(""+currentUser.getUserAge());
            descriptionText.setText(currentUser.getUserDescription());
            privacy = (CheckBox) findViewById(R.id.change_privacy);
            if (currentUser.getUserPrivacy()) {
                privateCounter++;
            }
            if(currentUser.getUserGender() == "male"){
                radioSexButton = (RadioButton)findViewById(R.id.radio_male);
            } else if (currentUser.getUserGender() == "female"){
                radioSexButton = (RadioButton)findViewById(R.id.radio_female);
            } else {
                radioSexButton = (RadioButton)findViewById(R.id.radio_other);
            }
            radioSexButton.setChecked(true);
        }

        //gender drop down list
        radioSexGroup = (RadioGroup) findViewById(R.id.genderGroup);

    }

    private class grabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();
        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();

            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);

            return loadresult;
        }
    }


    public void Confirm(View view) {
        //TODO upload changes

        name = nameText.getText().toString();
        age = ageText.getText().toString();
        userDescription = descriptionText.getText().toString();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int selectedID = radioGroup.getCheckedRadioButtonId();
        RadioButton btn = (RadioButton) findViewById(selectedID);
        genderIdentity = btn.getText().toString();

        if (privateCounter % 2 == 0) {
            isPrivate = false;
        }
        else {
            isPrivate = true;
        }

        Log.d(LOG_TAG, "" + age);
        Log.d(LOG_TAG, "" + userDescription);
        Log.d(LOG_TAG, "" + genderIdentity);

        Log.d(LOG_TAG, "Saved, Launching Home Activity...");
        startActivity(new Intent(InformationActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();

    }

    public void Cancel(View view) {
        Log.d(LOG_TAG, "Canceled, Launching Home Activity...");
        startActivity(new Intent(InformationActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }
}