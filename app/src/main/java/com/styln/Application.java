//
//
//
package com.styln;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;

/**
 * Application class responsible for initializing singletons and other common components.
 */
public class Application extends MultiDexApplication {
    private static final String LOG_TAG = Application.class.getSimpleName();
    private static char sign_opt = 'f';

    public static char getSign_opt() {
        return sign_opt;
    }

    public static void setSign_opt(char sign_opt) {
        Application.sign_opt = sign_opt;
    }
    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        initializeApplication();
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");
    }

    private void initializeApplication() {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());

        // logic here...
    }

}
