package com.styln;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungr on 4/4/17.
 */

public class ProfileInformationActivity extends AppCompatActivity{
    CheckBox ch1;
    Button b1;
    EditText editTextName;
    EditText editTextName3;
    String userGender;
    Spinner gender;
    Spinner selectedGender;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay;

    private static String LOG_TAG = ProfileInformationActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //user age
        setContentView(R.layout.profileinformation);
        b1 = (Button) findViewById(R.id.button23);
        //EditText editTextName = (EditText) findViewById(R.id.editText);

        //super.onCreate(savedInstanceState);

        //gender drop down list
        radioSexGroup = (RadioGroup) findViewById(R.id.genderGroup);


        //private button
        setContentView(R.layout.profileinformation);
        ch1=(CheckBox)findViewById(R.id.checkBox1);


        //Description
        //EditText editTextName3 = (EditText) findViewById(R.id.editText3);
        b1.setClickable(true);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Button was clicked");
                editTextName=(EditText)findViewById(R.id.editText);
                editTextName3=(EditText)findViewById(R.id.editText3);
                Log.i(LOG_TAG, "INFO" + editTextName.getText().toString());
                //etpassword=(EditText)findViewById(R.id.password);
                //tv.setText("Your Input: \n"+etname.getText().toString()+"\n"+etemail.getText().toString()+"\n"+etpassword.getText().toString()+"\nEnd.");
            }
        });

    }


    public void BBBBBBB(View view) {
        //Log.d(LOG_TAG, "Button was clicked");
        editTextName=(EditText)findViewById(R.id.editText);
        editTextName3=(EditText)findViewById(R.id.editText3);
        userGender = String.valueOf(gender.getSelectedItem());
        int selectedID = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedID);
        Toast.makeText(ProfileInformationActivity.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent();

    }

    public void onRadioButtonClicked(View view) {
        //radioSexButton = (RadioButton) find
    }
}
