package com.samir.cfos.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.preference.PreferenceManager;

import com.samir.cfos.utils.Utilities;

/**
 * created by SAMIR SHRESTHA on 1/29/2020  at 11:32 AM
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MyApplication myApplication;
    private static SharedPreferences sharedPreferences;

    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        myApplication = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
//        Log.e("D-Token: ", Utilities.getDeviceToken());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getAppContext() {
        return myApplication.getApplicationContext();
    }

    public static SharedPreferences getSharedPreference() {
        return sharedPreferences;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
