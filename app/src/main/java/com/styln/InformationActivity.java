package com.styln;

import android.content.Intent;
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

/**
 * Created by jungr on 4/4/17.
 */

public class InformationActivity extends AppCompatActivity{
    CheckBox ch1;
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
        setContentView(R.layout.activity_Information);
        confirm = (Button) findViewById(R.id.change_confirm);
        cancel = (Button) findViewById(R.id.change_cancel);


        //gender drop down list
        radioSexGroup = (RadioGroup) findViewById(R.id.genderGroup);


        //private button
        setContentView(R.layout.activity_Information);
        ch1 = (CheckBox) findViewById(R.id.change_privacy);
        if (ch1.isChecked()) {
            privateCounter++;
        }

    }


    public void Confirm(View view) {
        picture = (ImageView)findViewById(R.id.change_picture);
        changePic = (Button)findViewById(R.id.change_picture_button);
        nameText = (EditText)findViewById(R.id.change_name);
        name = nameText.getText().toString();
        ageText = (EditText)findViewById(R.id.change_age);
        age = ageText.getText().toString();
        descriptionText = (EditText)findViewById(R.id.change_description);
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