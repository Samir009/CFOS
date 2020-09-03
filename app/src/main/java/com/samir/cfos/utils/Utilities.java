package com.samir.cfos.utils;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samir.cfos.constants.AppConstants;

import static com.samir.cfos.helpers.MyApplication.getSharedPreference;

/**
 * created by SAMIR SHRESTHA on 1/29/2020  at 9:05 AM
 */

public class Utilities {

    public static FirebaseAuth firebaseAuth(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }
//
    public static FirebaseFirestore firebaseFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db;
    }

   public static void saveDeviceToken(String token){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(AppConstants.DEVICE_TOKEN, token);
        editor.apply();
        editor.commit();
   }

   public static void saveDocId(String docId){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(AppConstants.DOCUMENT_ID, docId);
        editor.apply();
        editor.commit();
   }

   public static String getSavedDocId(){
        return getSharedPreference().getString(AppConstants.DOCUMENT_ID, null);
   }
//
   public static String getDeviceToken(){
        return getSharedPreference().getString(AppConstants.DEVICE_TOKEN, null);
   }
//
    public static void saveUserDetails(String name, String gender, String address, String contact,
                                       String email, String token, String role){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(AppConstants.USERNAME, name);
        editor.putString(AppConstants.USERNAME, gender);
        editor.putString(AppConstants.ADDRESS, address);
        editor.putString(AppConstants.CONTACT, contact);
        editor.putString(AppConstants.EMAIL, email);
        editor.putString(AppConstants.PASSWORD, token);
        editor.putString(AppConstants.ROLE, role);
        editor.apply();
        editor.commit();
    }


    public static void setLoggedIn(boolean b) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(AppConstants.IS_USER_LOGGED_IN, b);
        editor.apply();
    }

    public static boolean isUserLoggedIn(){
        return getSharedPreference().getBoolean(AppConstants.IS_USER_LOGGED_IN, false);
    }
//
//    public static String getUserName(){
//        String username = getSharedPreference().getString(AppConstants.USERNAME, null);
//        return username;
//    }
//
//    public static String getGender(){
//        String gender = getSharedPreference().getString(AppConstants.GENDER, null);
//        return gender;
//    }
//
//    public static String getSemester(){
//        String sem = getSharedPreference().getString(AppConstants.SEMESTER, null);
//        return sem;
//    }
//
//    public static String getAddress(){
//        String address = getSharedPreference().getString(AppConstants.ADDRESS, null);
//        return address;
//    }
//
//    public static String getContact(){
//        String contact = getSharedPreference().getString(AppConstants.CONTACT, null);
//        return contact;
//    }
//
//    public static String getEmail(){
//        String email = getSharedPreference().getString(AppConstants.EMAIL, null);
//        return email;
//    }
//
//    public static String getPassword(){
//        String password = getSharedPreference().getString(AppConstants.PASSWORD, null);
//        return password;
//    }
//
//    public static void saveLoginResponse(UserRegModel regModel){
//        SharedPreferences.Editor editor = getSharedPreference().edit();
//        editor.putString(AppConstants.LOGIN_RESPONSE, String.valueOf(regModel));
//        editor.apply();
//        setLoggedIn(true);
//    }
//

//
//    public static UserRegModel getSavedLoginResponse(){
//        String savedRegisteredResponse = getSharedPreference().getString(AppConstants.LOGIN_RESPONSE, null);
//        return new GsonBuilder().create().fromJson(savedRegisteredResponse, UserRegModel.class);
//    }
//

//
//    public static boolean isIntroOpened(){
//        return getSharedPreference().getBoolean(AppConstants.IS_FIRST_INTRO_OPENED, false);
//    }
//
//    public static void setIntroCompleted(boolean b){
//        SharedPreferences.Editor editor = getSharedPreference().edit();
//        editor.putBoolean(AppConstants.IS_FIRST_INTRO_OPENED, b);
//        editor.apply();
//    }

}
