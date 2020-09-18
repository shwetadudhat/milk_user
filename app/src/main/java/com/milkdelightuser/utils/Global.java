package com.milkdelightuser.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milkdelightuser.Activity.drawer;
import com.milkdelightuser.Activity.subscription2;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

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
                Log.e("product_price",String.valueOf(product_price));
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

    public  static int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }


   /* public static void showDiaog(Context context,String txStatus){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_success, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
        TextView tvStts=dialogView.findViewById(R.id.tvStts);
        TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
        TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
        ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);

        tvTransId.setVisibility(View.GONE);

        tvStts.setText("Payment Success");
        tvStts.setTextColor(context.getResources().getColor(R.color.green));
        ivIcon.setImageResource(R.drawable.ic_noun_check_1);
        // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
        ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        tvTransDesc.setText("Payment done through your Wallet amount");

        //  db.removeItemFromCart(product_id);



        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, drawer.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                alertDialog.dismiss();
            }
        });


        alertDialog.show();


    }*/

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgoLikeTwitter(long time, Context context, boolean allowLongFormat) {
        if (allowLongFormat) {
            return getTimeAgoLongFormatLikeTwitter(time, context);
        }

        if (time < 1000000000000L) {
            //IF TIMESTAMP GIVEN IN SECONDS, CONVERT TO MILLIS
            time *= 1000;
        }

        long now = getCurrentTime();
        if (time > now || time <= 0) {
            return "+1d";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return diff / SECOND_MILLIS + "s";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return 1 + "m";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return 1 + "h";
        } else if (diff < 24 * HOUR_MILLIS) {
            if (diff / HOUR_MILLIS == 1)
                return 1 + "h";
            else
                return diff / HOUR_MILLIS + "h";
        } else if (diff < 48 * HOUR_MILLIS) {
            return 1 + "d";
        } else {
            return diff / DAY_MILLIS + "d";
        }
    }

    private static String getTimeAgoLongFormatLikeTwitter(long time, Context context) {
        if (time < 1000000000000L) {
            //if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = getCurrentTime();
        if (time > now || time <= 0) {
            return "+1d";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.seconds_ago, diff / SECOND_MILLIS);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.min_ago, 1);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return context.getString(R.string.mins_ago, diff / MINUTE_MILLIS);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return context.getString(R.string.hour_ago, 1);
        } else if (diff < 24 * HOUR_MILLIS) {
            if (diff / HOUR_MILLIS == 1)
                return context.getString(R.string.hour_ago, 1);
            else
                return context.getString(R.string.new_hours_ago, diff / HOUR_MILLIS);
        } else if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.day_ago, 1);
        } else {
            return context.getString(R.string.new_days_ago, diff / DAY_MILLIS);
        }
    }

    private static long getCurrentTime() {
        return new Date().getTime();
    }


    public static String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }


}
