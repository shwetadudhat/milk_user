package com.milkdelightuser.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.milkdelightuser.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class Global {

    public static final String FOLDER_NAME = "ProfileImage";
    public static final String MY_FILTER_PREFS_NAME = "filterPref";
    public static final String MY_FREQ_PREFS_NAME = "freqPref";
    public static final String FREQ_ID = "freq_id";
    public static final String MY_NOTIFICATION_PREFS_NAME = "notificationPref";
    public static final String MY_GST_PREFS_NAME = "GSTPref";
    public static final String NOTIFICATION_DATA = "jsonarray";
    public static final String GST_DATA = "jsonObject";
    public static final String MY_SUBSCRIPTION_PREFS_NAME = "mySubscriptionlistPref";
    public static final String SUB_DATA = "subList";

    public static boolean isNumeric(String nmbr){
        if (TextUtils.isEmpty(nmbr)) {
            return false;
        } else {
            boolean r = Pattern.compile("^[0-9]*$").matcher(nmbr).matches();
            return r;
        }
    }

    public static boolean isValidPinCode(String pinCode)
    {

        // Regex to check valid pin code of India.
        String regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // If the pin code is empty
        // return false
        if (pinCode == null) {
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given pin code
        // and regular expression.
        Matcher m = p.matcher(pinCode);

        // Return if the pin code
        // matched the ReGex
        return m.matches();
    }

    public static boolean isValidEmail(String email) {
         if (TextUtils.isEmpty(email)) {
             return  false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isInternetConnected(Context mContext) {
        boolean outcome = false;

        try {
            if (mContext != null) {
                ConnectivityManager cm = (ConnectivityManager) mContext
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

                for (NetworkInfo tempNetworkInfo : networkInfos) {
                    if (tempNetworkInfo.isConnected()) {
                        outcome = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outcome;
    }


    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            sendExceptionReport(e);
        }

        return 480;
    }

    public static int getDeviceHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            sendExceptionReport(e);
        }

        return 800;
    }

    public static void sendExceptionReport(Exception e) {
        e.printStackTrace();

        try {
            // Writer result = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(result);
            // e.printStackTrace(printWriter);
            // String stacktrace = result.toString();
            // new CustomExceptionHandler(c, URLs.URL_STACKTRACE)
            // .sendToServer(stacktrace);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static ArrayList<String> asList(String str) {
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(str.split("\\s*,\\s*")));
        return items;
    }

    public static void showKeyBoard(Context c, View v) {
        v.requestFocus();
        v.setFocusableInTouchMode(true);

        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public static String convertDate(String date){

        return date;
    }

    //region FOR SAVE BITMAP
    public static void saveBitmap(Bitmap bitmap, File file) {
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion


    public static File getOutputMediaFile() {
        try {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
             File mediaStorageDir;
            if (isSDcardMounted()) {
                mediaStorageDir =new File(FOLDER_RIDEINN_PATH);
                Log.e("iffff","ifffff");
            } else {
                mediaStorageDir =new File(
                        Environment.getRootDirectory(),
                        FOLDER_NAME
                );
                Log.e("elseee","elseee");
            }

            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    mediaStorageDir.mkdirs();
                   // return null;
                }
            }
            // Create a media file name
            File mediaFile =new File(mediaStorageDir.getAbsolutePath() + File.separator +new Date().getTime() + ".jpg");
            Log.e ("mediaFile",mediaFile.toString());
            mediaFile.createNewFile();
            Log.e ("mediaFile123",mediaFile.toString());
            return mediaFile;
        } catch ( Exception e) {
            return null;
        }
    }

    public static Boolean isSDcardMounted() {
        String state = Environment.getExternalStorageState();
        Log.e("state",state);
        return state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    public static String FOLDER_RIDEINN_PATH = (Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "ProfileImage");


    public static void getDateData(int targetDate, String tardetDate, String enddate){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(tardetDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, targetDate);
        enddate = dateFormat.format(calendar.getTime());
        Log.e("enddate",enddate);
    }


    public static String getDateConvert(String start_date, String pattern1/*"yyyy-MM-dd"*/, String pattern2/*"EEE dd, MMM yyyy"*/) throws ParseException {
/*"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"*/
        SimpleDateFormat format1 = new SimpleDateFormat(pattern1);
        SimpleDateFormat format2 = new SimpleDateFormat(pattern2);
        Date date = format1.parse(start_date);
        System.out.println(format2.format(date));
        Log.e("dateeee",format2.format(date));
        String formattedDate = format2.format(date);

        return  formattedDate;

    }

    //region FOR SHOW INTERNET CONNECTION DIALOG
    public static void showInternetConnectionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.no_internet_connection_with_emoji))
                .setMessage(context.getResources().getString(R.string.connection_not_available))
                .setPositiveButton(context.getResources().getString(R.string.btn_enable), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        try {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                })
                .setNegativeButton(context.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public  static Double getTax(Context context, Double product_price){
       SharedPreferences GstPref1 = context.getSharedPreferences(MY_GST_PREFS_NAME, MODE_PRIVATE);
        String gstData = GstPref1.getString(GST_DATA, null);
        Double gst,sgst,tax = null;
        if (gstData != null) {
            Log.e("gstData",gstData);
            try {
                JSONObject jsonObject=new JSONObject(gstData);
                gst= Double.parseDouble(jsonObject.getString("gst"))/100;
                sgst= Double.parseDouble(jsonObject.getString("sgst"))/100;

                Log.e("gsttt", String.valueOf(gst));
               // tax=gst+sgst;
                tax=product_price*(gst+sgst);
                Log.e("taxxx",tax.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return tax;
    }

    public  static Double getGSTTax(Context context, Double product_price){
        SharedPreferences GstPref1 = context.getSharedPreferences(MY_GST_PREFS_NAME, MODE_PRIVATE);
        String gstData = GstPref1.getString(GST_DATA, null);
        Double gst= null;
        if (gstData != null) {
            Log.e("gstData",gstData);
            try {
                JSONObject jsonObject=new JSONObject(gstData);
                gst= Double.parseDouble(jsonObject.getString("gst"))/100;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return gst;
    }

    public  static Double getSGSTTax(Context context, Double product_price){
        SharedPreferences GstPref1 = context.getSharedPreferences(MY_GST_PREFS_NAME, MODE_PRIVATE);
        String gstData = GstPref1.getString(GST_DATA, null);
        Double sgst=null;
        if (gstData != null) {
            Log.e("gstData",gstData);
            try {
                JSONObject jsonObject=new JSONObject(gstData);
                sgst= Double.parseDouble(jsonObject.getString("sgst"))/100;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return sgst;
    }

}
