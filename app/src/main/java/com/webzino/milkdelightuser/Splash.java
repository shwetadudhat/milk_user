package com.webzino.milkdelightuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.material.snackbar.Snackbar;
import com.webzino.milkdelightuser.Activity.ChooseLoginSignup;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.Home;
import com.webzino.milkdelightuser.Activity.referFriend;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.Session_management;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Splash extends BaseActivity {

    Session_management sessionManagement;

    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    InstallReferrerClient referrerClient;

    String referrerUrl;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        sessionManagement = new Session_management(this);
        initTracking();



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();

            } else {
                // write your logic here
                Thread background = new Thread() {
                    public void run() {

                        try {
                            // Thread will sleep` for 5 seconds
                            sleep(4 * 1000);
                            // After 5 seconds redirect to another intent
                            go_next();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                // start thread
                background.start();
            }

    }


    private void initTracking() {
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.

                        try {
                            obtainReferrerDetails();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }


    private void obtainReferrerDetails() throws RemoteException {
        ReferrerDetails response = referrerClient.getInstallReferrer();
        referrerUrl = response.getInstallReferrer();
        long referrerClickTime = response.getReferrerClickTimestampSeconds();
        long appInstallTime = response.getInstallBeginTimestampSeconds();
        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();


        Log.e("response",response.toString());
        Log.e("referrerUrl",referrerUrl);

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)/*+ ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)*/
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{Manifest.permission
                                                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                }
                            }
                        }).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else {
            // write your logic code if permission already granted

            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep` for 5 seconds
                        sleep(4 * 1000);

                        // After 5 seconds redirect to another intent
                        go_next();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            // start thread
            background.start();
        }
    }

    public void go_next() {

       /* Intent intent=new Intent(Splash.this,CashFreeActivity.class);
        startActivity(intent);*/
        if(sessionManagement.isLoggedIn()) {
            Intent startmain = new Intent(Splash.this, Home.class);
            startActivity(startmain);
        }
        else{

            Intent intent=new Intent(Splash.this, ChooseLoginSignup.class/*referFriend.class*/);
            intent.putExtra("referCode",referrerUrl);
            startActivity(intent);

        }
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternalFile)
                    {
                       /* go_next();*/
                        Thread background = new Thread() {
                            public void run() {

                                try {
                                    // Thread will sleep` for 5 seconds
                                    sleep(4 * 1000);

                                    // After 5 seconds redirect to another intent
                                    go_next();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        // start thread
                        background.start();
                        // write your logic here
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                    new String[]{Manifest.permission
                                                            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    PERMISSIONS_MULTIPLE_REQUEST);
                                        }
                                    }
                                }).show();
                    }
                }
                break;
        }
    }




}