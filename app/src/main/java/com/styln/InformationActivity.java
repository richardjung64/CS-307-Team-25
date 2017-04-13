package com.styln;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.attr.data;

/**
 * Created by jungr on 4/4/17.
 */

public class InformationActivity extends AppCompatActivity {
    String userID = "NOT_VALID";
    CheckBox privacy;
    ImageView picture;
    Button confirm, cancel, changePic;
    EditText nameText, ageText, descriptionText;
    String userGender;
    Spinner gender;
    Spinner selectedGender;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    int privateCounter = 0;
    boolean isPrivate;

    public final static String USER_NAME = "User name to activity";
    public final static String USER_AGE = "User age to activity";
    public final static String USER_DESCRIPTION = "User descr to activity";
    public final static String USER_PRIVACY = "User privacy to activity";
    public final static String USER_GENDER = "User gender to activity";

    public static final int GET_FROM_GALLERY = 3;


    String name, age;
    String genderIdentity;
    String userDescription;


    private static String LOG_TAG = InformationActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //user age
        setContentView(R.layout.activity_information);

        confirm = (Button) findViewById(R.id.change_confirm);
        cancel = (Button) findViewById(R.id.change_cancel);
        picture = (ImageView) findViewById(R.id.change_picture);
        changePic = (Button) findViewById(R.id.change_picture_button);
        nameText = (EditText) findViewById(R.id.change_name);
        ageText = (EditText) findViewById(R.id.change_age);
        descriptionText = (EditText) findViewById(R.id.change_description);
        

        //TODO loads userinfo
        userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
        //LOADS user info;

        GrabUser task = new GrabUser();
        UsersDO currentUser = new UsersDO();
        try {
            currentUser = task.execute("String").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (currentUser != null) {
            Log.d(LOG_TAG, "Activity restored");
            name = currentUser.getUserName();
            nameText.setText(currentUser.getUserName());
            //nameText.setHint("Your Name");
            age = currentUser.getUserAge();
            ageText.setText(currentUser.getUserAge());
            //ageText.setHint("Your Age");
            userDescription = currentUser.getUserDescription();
            descriptionText.setText(currentUser.getUserDescription());
            //descriptionText.setHint("Tell us something about you");
            privacy = (CheckBox) findViewById(R.id.change_privacy);
            if (currentUser.getUserPrivacy()) {
                privacy.setChecked(true);
                isPrivate = true;
                privateCounter = 1;
            }
            else {
                isPrivate = false;
                privateCounter = 0;
            }
            privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    privateCounter++;
                }
            });
            if(currentUser.getUserGender().equals("male")){
            radioSexButton = (RadioButton) findViewById(R.id.radio_male);
                genderIdentity = "male";
            } else if (currentUser.getUserGender().equals("female")){
            radioSexButton = (RadioButton) findViewById(R.id.radio_female);
                genderIdentity = "female";
            } else {
            radioSexButton = (RadioButton) findViewById(R.id.radio_other);
                genderIdentity = "other";
            }

//            Log.d(LOG_TAG, "RESTORED NAME" + name);
//            Log.d(LOG_TAG, "RESTORED AGE" + age);
//            Log.d(LOG_TAG, "RESTORED DESCRIPTION" + userDescription);
//            Log.d(LOG_TAG, "RESTORED IDENTITY" + genderIdentity);
//            Log.d(LOG_TAG, "RESTORED PRIVACY" + isPrivate);

            //gender drop down list
            radioSexGroup = (RadioGroup) findViewById(R.id.genderGroup);
        }
        else {
            //nameText.setHint(currentUser.getUserName());
            nameText.setHint("Your Name");
            //ageText.setHint(""+currentUser.getUserAge());
            ageText.setHint("Your Age");
            //descriptionText.setHint(currentUser.getUserDescription());
            descriptionText.setHint("Tell us something about you");
            privacy = (CheckBox) findViewById(R.id.change_privacy);
            privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    privateCounter++;
                }
            });
            //if(currentUser.getUserGender().equals("male")){
            radioSexButton = (RadioButton) findViewById(R.id.radio_male);
            //} else if (currentUser.getUserGender().equals("female")){
            radioSexButton = (RadioButton) findViewById(R.id.radio_female);
            //} else {
            radioSexButton = (RadioButton) findViewById(R.id.radio_other);
            //}
            radioSexButton.setChecked(true);
            //}

            //gender drop down list
            radioSexGroup = (RadioGroup) findViewById(R.id.genderGroup);
        }
    }

    private class GrabUser extends AsyncTask<String, Void, UsersDO> {
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        UsersDO loadresult = new UsersDO();

        @Override
        protected UsersDO doInBackground(String... strings) {
            UsersDO currentUser = new UsersDO();
            String userID = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            currentUser = mapper.load(UsersDO.class, userID);
            loadresult = currentUser;
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
        Log.d(LOG_TAG, "" + isPrivate);

        FollowAction fl = new FollowAction();
        if (SignInActivity.firstTime)
            fl.UserChanges(name, age, isPrivate, userDescription);

        if (SignInActivity.firstTime)
            SignInActivity.firstTime = false;
        Log.d(LOG_TAG, "Saved, Launching Home Activity...");
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        i.putExtra(USER_NAME, name);
        i.putExtra(USER_AGE, age);
        i.putExtra(USER_DESCRIPTION, userDescription);
        i.putExtra(USER_PRIVACY, isPrivate);
        i.putExtra(USER_GENDER, genderIdentity);
//        startActivity(new Intent(InformationActivity.this, HomeActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(i);
        // finish should always be called on the main thread.
        finish();

    }

    public void Cancel(View view) {
        if (SignInActivity.firstTime) {
            Log.d(LOG_TAG, "Canceled, Launching Sign in Activity...");
            startActivity(new Intent(InformationActivity.this, SignInActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            // finish should always be called on the main thread.
            finish();
        } else {
            return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
