package com.webzino.milkdelightuser.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webzino.milkdelightuser.Activity.Home;
import com.webzino.milkdelightuser.R;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

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

            Log.e("remoteMestitle",remoteMessage.getNotification().getTitle());

            if (remoteMessage.getNotification() != null) {
                Log.e("NOTIFICATION", "Message Notification Body: " + remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            }else if (remoteMessage.getData().size() > 0) {
                Log.e("NOTIFICATION", "Message DATA PayLoad: " + remoteMessage.getData());
                sendNotification(getString(R.string.app_name),remoteMessage.getData().get("message"));
            }

        }
    }

    private void sendNotification(String title, String messageBody) {

//
        /*SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
        editor.putBoolean(SharePreferenceManager.NOTIFICATION_BADGE, true);
        editor.apply();*/

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
}
