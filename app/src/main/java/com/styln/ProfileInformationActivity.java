package com.styln;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jungr on 4/4/17.
 */

public class ProfileInformationActivity extends AppCompatActivity{
    CheckBox ch1;
    Button b1;

    private static String LOG_TAG = ProfileInformationActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.profileinformation);
       EditText editTextName = (EditText) findViewById(R.id.editText);
       editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
               boolean handled = false;
               if (i == EditorInfo.IME_ACTION_NEXT) {
                   String inputText = textView.getText().toString();
                   Toast.makeText(ProfileInformationActivity.this, "You inputted: " + inputText, Toast.LENGTH_SHORT);
               }
               return handled;
           }
       });



            //super.onCreate(savedInstanceState);
            EditText editTextName2 = (EditText) findViewById(R.id.editText2);
            editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    boolean handled = false;
                    if (i == EditorInfo.IME_ACTION_NEXT) {
                        String inputText = textView.getText().toString();
                        Toast.makeText(ProfileInformationActivity.this, "You inputted: " + inputText, Toast.LENGTH_SHORT);
                    }
                    return handled;
                }
            });

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.profileinformation);
        ch1=(CheckBox)findViewById(R.id.checkBox1);
        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                result.append("Thanks : ").append(ch1.isChecked());
                Toast.makeText(ProfileInformationActivity.this, result.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });


        //super.onCreate(savedInstanceState);
        EditText editTextName3 = (EditText) findViewById(R.id.editText3);
        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String inputText = textView.getText().toString();
                    Toast.makeText(ProfileInformationActivity.this, "You inputted: " + inputText, Toast.LENGTH_SHORT);


                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    handled = true;
                }
                return handled;
            }
        });



//        Log.d(LOG_TAG,"Launching Home Activity");
//        startActivity(new Intent(ProfileInformationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        finish();
   }




}


