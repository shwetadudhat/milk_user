package com.vaapglkns;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.vaapglkns.utils.Constant;
import com.vaapglkns.utils.Debug;
import com.vaapglkns.utils.GpsUtils;
import com.vaapglkns.utils.Utils;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
    String TAG = "SplashActivity";
    Handler handler = new Handler();
    TextView splashMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (Utils.isInternetConnected(getActivity())) {
//            getUpdateVersion();
            if (Utils.isLocationProviderEnabled(getActivity())) {
                startApplication(1000);
            } else {
                checkGPSLocation();
            }
        } else {
            handler.post(mPostInternetConDialog);
        }

//        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        checkPermission(permissions);
    }

    //region FOR CHECK PERMISSION
    private void checkPermission(String[] permissions) {
        if (!hasPermissions(this, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int a = 0; a < permissions.length; a++) {
                    if (checkSelfPermission(permissions[a]) != PackageManager.PERMISSION_GRANTED) {

                    }
                }
                requestPermissions(permissions, Constant.REQUEST_PERMISSION);
            }
        } else {
//            showPictureChooser();
        }
    }
    //endregion


    //region HAS PERMISSION
    boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int a = 0; a < permissions.length; a++) {
                if (ActivityCompat.checkSelfPermission(context, permissions[a]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //region FOR CHECK GPS IS ON OR OFF
    private void checkGPSLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS network not enabled!");
            dialog.setPositiveButton("Turn GPS ON", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new GpsUtils(SplashActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {

                        }
                    });
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } else {
            startApplication(1000);
        }
    }
    //endregion

    private void startApplication(long sleepTime) {
        handler.postDelayed(startApp, sleepTime);
    }

    Runnable startApp = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(startApp);
            Debug.e(TAG, "startApp");
//            if (!Utils.isUserLoggedIn(getActivity())) {

//                Intent i = new Intent(getActivity(), LoginActivity.class);
//                startActivity(i);
//                finish();

//            } else {

            Intent i = new Intent(getActivity(),
                    HomeActivity.class);
            startActivity(i);
            finish();

//            }
        }
    };

    int count = 30;

    Runnable mPostInternetConDialog = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.connection_title)
                    .setMessage(R.string.connection_not_available)
                    .setPositiveButton(R.string.btn_enable, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    };

    Runnable postLocationDialog = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.location_title)
                    .setMessage(R.string.location_msg)
                    .setPositiveButton(R.string.btn_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    Runnable checkConnection = new Runnable() {
        @Override
        public void run() {
            Debug.e(TAG, "checkConnection");
            if (Utils.isInternetConnected(getActivity())) {

                splashMsg.setText(getString(R.string.connected));
                handler.removeCallbacks(checkConnection);

                if (Utils.isInternetConnected(getActivity())) {
                    if (Utils.isLocationProviderEnabled(getActivity())) {
                        startApplication(1000);
                    } else {
                        checkGPSLocation();
                    }
                } else {
                    handler.post(mPostInternetConDialog);
                }

            } else {
                if (count != 0) {
                    splashMsg.setText(String.format(
                            getString(R.string.retrying), "" + (count--)));
                    handler.postDelayed(checkConnection, 1000);
                } else {
                    splashMsg.setText("Finishing... ");
                    finish();
                }

            }
        }
    };

//    public void getUpdateVersion() {
//        try {
////            showDialog("");
//
//            RequestParams params = RequestParamsUtils.newRequestParams(getActivity());
////            params.put(RequestParamsUtils.MEMBERCODE, "" + Utils.getMemberCode(getActivity()));
//
//            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest(getActivity());
//            clientPhotos.post(URLs.GET_CHALLAN(), params, new GetVersionDataHandler(getActivity()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private class GetVersionDataHandler extends AsyncResponseHandler {
//
//        public GetVersionDataHandler(Activity context) {
//            super(context);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//        }
//
//        @Override
//        public void onFinish() {
//            super.onFinish();
//            try {
//                dismissDialog();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onSuccess(String response) {
//
//            try {
//                Debug.e("", "getUpdateVersion# " + response);
//                if (response != null && response.length() > 0) {
////                    VersionUpdate loginRider = new Gson().fromJson(
////                            response, new TypeToken<VersionUpdate>() {
////                            }.getType());
////
////                    if (loginRider.St == 1) {
////                        if (Integer.valueOf(loginRider.Data.get(0).anroidVersion) > Utils.getAppVersionCode(getActivity())) {
////                            if (Debug.DEBUG) {
////                                startApplication(1000);
////                            } else {
////                                versionUpgradeDialog();
////                            }
////                        } else {
////                            startApplication(1000);
////                        }
////                    } else {
////                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onFailure(Throwable e, String content) {
//            startApplication(1000);
//            dismissDialog();
//        }
//    }

//    public void versionUpgradeDialog() {
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
//                .title(R.string.upgrade)
//                .content(R.string.update_text)
//                .positiveText(R.string.btn_update)
//                .negativeText(R.string.btn_cancel)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                        try {
//                            final String appPackageName = getPackageName();
//                            try {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                            } catch (android.content.ActivityNotFoundException anfe) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
//                            }
//                            finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                        finish();
//                    }
//                });
//
//        MaterialDialog dialog = builder.build();
//        dialog.show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQ_CODE_SETTING:
                splashMsg.setVisibility(View.VISIBLE);
                handler.post(checkConnection);
                break;

            case Constant.REQUEST_GPS:
                if (resultCode == Activity.RESULT_OK) {
                    checkGPSLocation();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isPermissionGranted = true;
        if (requestCode == Constant.REQUEST_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                for (int a = 0; a < permissions.length; a++) {
                    if (checkSelfPermission(permissions[a]) != PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = false;
                    }
                }
                if (!isPermissionGranted) {
                    requestPermissions(permissions, Constant.REQUEST_PERMISSION);
                } else {
//                    showPictureChooser();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        try {
            handler.removeCallbacks(startApp);
            handler.removeCallbacks(checkConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
