package com.webzino.milkdelightuser.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.webzino.milkdelightuser.Activity.Login;
import com.webzino.milkdelightuser.Activity.MainActivity;

import java.util.HashMap;


public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences("Daily", PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences("Daily", PRIVATE_MODE);
        editor2 = prefs2.edit();

    }

    public void createLoginSession(String id, String email, String name, String image, String phone) {
        editor.putBoolean(BaseURL.IS_LOGIN, true);
        editor.putString(BaseURL.KEY_ID, id);
        editor.putString(BaseURL.KEY_EMAIL, email);
        editor.putString(BaseURL.KEY_NAME, name);
        editor.putString(BaseURL.KEY_IMAGE,image);
        editor.putString(BaseURL.KEY_MOBILE,phone);
       // editor.putString(BaseURL.KEY_PINCODE,pincode);

        editor.commit();
        editor.apply();
    }

   /* public void createLoginSession(String email, String pass){
        editor.putBoolean(BaseURL.IS_LOGIN, true);
        editor.putString(BaseURL.KEY_ID, pass);
        editor.putString(BaseURL.KEY_EMAIL, email);

        editor.commit();
        editor.apply();
    }*/

//
    public boolean  checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, MainActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(BaseURL.KEY_ID, prefs.getString(BaseURL.KEY_ID, null));
        // user email id
        user.put(BaseURL.KEY_EMAIL, prefs.getString(BaseURL.KEY_EMAIL, null));
        // user name
        user.put(BaseURL.KEY_NAME, prefs.getString(BaseURL.KEY_NAME, null));
        user.put(BaseURL.KEY_MOBILE, prefs.getString(BaseURL.KEY_MOBILE, null));
        user.put(BaseURL.KEY_IMAGE, prefs.getString(BaseURL.KEY_IMAGE, null));
//        user.put(KEY_TYPE, prefs.getString(KEY_TYPE,null));

        // return user
        return user;
    }
//
//    public void updateData(String name, String mobile, String pincode
//            , String socity_id, String image, String wallet, String rewards, String house) {
//
//        editor.putString(KEY_NAME, name);
//        editor.putString(KEY_MOBILE, mobile);
//        editor.putString(KEY_IMAGE, image);
//        editor.apply();
//    }
//
//    public void updateSocity(String socity_name, String socity_id) {
//        editor.putString(KEY_SOCITY_NAME, socity_name);
//        editor.putString(KEY_SOCITY_ID, socity_id);
//
//        editor.apply();
//    }

    public void logoutSession() {
        editor.clear();
        editor.commit();
        cleardatetime();

        Intent logout = new Intent(context, Login.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, MainActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

//    public void creatdatetime(String date, String time) {
//        editor2.putString(KEY_DATE, date);
//        editor2.putString(KEY_TIME, time);
//
//        editor2.commit();
//    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

//
//    public HashMap<String, String> getdatetime() {
//        HashMap<String, String> user = new HashMap<String, String>();
//
//        user.put(KEY_DATE, prefs2.getString(KEY_DATE, null));
//        user.put(KEY_TIME, prefs2.getString(KEY_TIME, null));
//
//        return user;
//    }

//     Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(BaseURL.IS_LOGIN, false);
    }

}
