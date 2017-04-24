package com.styln;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shanu on 4/24/17.
 */

public class NotificationListActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_activity_list);
        NotificationListFragment themesFragment = new NotificationListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id._FragmentContainer, themesFragment);
        ft.commit();
    }
}
