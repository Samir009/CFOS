package com.samir.cfos.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * created by SAMIR SHRESTHA on 12/27/2019  at 8:41 PM
 */

public class UtilitiesFunctions {

    public static boolean isNetworkAvailable(Context context){
        if(context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static String getCurrentDateTime(Context context){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }

//    public static UserLogin getUserLogin() {
//
//        String savedUserResponse = UpasargaApplication.getSharedPreference().getString(AppConstants.LOGIN_RESPONSE, null);
//        return new GsonBuilder().create().fromJson(savedUserResponse, UserRegModel.class);
//    }
//

    // sharedpref isAdmin = false;

//    public static void saveUserLogin(UserLogin userLogin) {
//        String json = new GsonBuilder().create().toJson(userLogin);
//        SharedPreferences.Editor editor = UpasargaApplication.getSharedPreference().edit();
//        editor.putString(AppConstants.LOGIN_RESPONSE, json);
//        editor.apply();
//    }
//
//    public static Str isAdmin() {
//        boolean isLogin = UpasargaApplication.getSharedPreference().getBoolean(AppConstants.IS, false);
//        if (isLogin) {
//            return true;
//        }
//        return false;
//    }
//
//
//    public static boolean saveFoodDrinksList() {
//        String json = new GsonBuilder().create().toJson(userLogin);
//        SharedPreferences.Editor editor = UpasargaApplication.getSharedPreference().edit();
//        editor.putString(AppConstants.LOGIN_RESPONSE+ username, json);
//        editor.apply();
//    }



}
