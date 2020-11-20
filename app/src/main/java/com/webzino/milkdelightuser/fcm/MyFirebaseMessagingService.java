package com.webzino.milkdelightuser.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.webzino.milkdelightuser.Activity.Home;
import com.webzino.milkdelightuser.Model.About_Model;
import com.webzino.milkdelightuser.Model.Notification_Model;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Splash;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static com.webzino.milkdelightuser.utils.Global.MY_NOTIFICATION_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.NOTIFICATION_DATA;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    ArrayList<Notification_Model>  notificationList=new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    Notification_Model notification_model;
    private NotificationManager notifManager;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String token = s;
        Log.e("Token", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {

            Log.e("remoteMessage", new Gson().toJson(remoteMessage));



            Log.e("remoteMestitle",remoteMessage.getNotification().getTitle());

            if (remoteMessage.getNotification() != null) {
                Log.e("NOTIFICATION", "Message Notification Body: " + remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            }else if (remoteMessage.getData().size() > 0) {
                Log.e("NOTIFICATION", "Message DATA PayLoad: " + remoteMessage.getData());
                sendNotification(getString(R.string.app_name),remoteMessage.getData().get("message"));
            }
        }else
        {
            Log.e("msgelse","msgelse");
        }
    }


//    public void createNotificationJAVA(String aMessage, Context context) {
//        final int NOTIFY_ID = 0; // ID of notification
//        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
//        String title = context.getString(R.string.app_name); // Default Channel
//        Intent intent;
//        PendingIntent pendingIntent;
//        NotificationCompat.Builder builder;
//        if (notifManager == null) {
//            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
//            if (mChannel == null) {
//                mChannel = new NotificationChannel(id, title, importance);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                notifManager.createNotificationChannel(mChannel);
//            }
//            builder = new NotificationCompat.Builder(context, id);
//            intent = new Intent(context, Splash.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            builder.setContentTitle(aMessage)                            // required
//                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                    .setContentText(context.getString(R.string.app_name)) // required
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setTicker(aMessage)
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        } else {
//            builder = new NotificationCompat.Builder(context, id);
//            intent = new Intent(context, Splash.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            builder.setContentTitle(aMessage)                             //REQUIRED
//                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   //REQUIRED
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                    .setContentText(context.getString(R.string.app_name)) // required
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setTicker(aMessage)
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
//                    .setPriority(Notification.PRIORITY_HIGH);
//        }
//        Notification notification = builder.build();
//        notifManager.notify(NOTIFY_ID, notification);
//    }

    private void sendNotification(String title, String messageBody) {

        notification_model=new Notification_Model(title,messageBody);
        Log.e("notificationList",String.valueOf(notification_model));

        setDataFromSharedPreferences(notification_model);

//        String notificationType = responseObject.optString("notification_type");
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("From", "");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Acuidarnos";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_logo_main)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void setDataFromSharedPreferences(Notification_Model curProduct){

        Gson gson = new Gson();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(MY_NOTIFICATION_PREFS_NAME, Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString(NOTIFICATION_DATA, "");
        String jsonNewproductToAdd = gson.toJson(curProduct);

        JSONArray jsonArrayProduct= new JSONArray();

        try {
            if(jsonSaved.length()!=0){
                jsonArrayProduct = new JSONArray(jsonSaved);
            }
            jsonArrayProduct.put(new JSONObject(jsonNewproductToAdd));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("jsonArrayProduct",String.valueOf(jsonArrayProduct));

        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NOTIFICATION_DATA, String.valueOf(jsonArrayProduct));
        editor.commit();
    }
}
