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
    }

    public void back(View view) {
        Log.d(LOG_TAG, "Launching Main Activity...");
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        // finish should always be called on the main thread.
        finish();
    }

    String change = "";
    public void changeName(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Username");

// Specify input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

//Confirm button
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                change = input.getText().toString();
                userName.setText(change);
            }
        });
//Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void signOut(View view) {
        identityManager.signOut();
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
        finish();
        return;
    }

    public void setPrivacy(View view) {
        CompoundButton cb = (CheckBox) findViewById(R.id.AccPrivBox);
        if(view.getId() == R.id.AccPrivBox){
            checked = cb.isChecked();
            if(checked){
                //Set Account Private
            } else {
                //Set Account Public

            }
        }
    }

}
