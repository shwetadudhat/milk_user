package com.webzino.milkdelightuser.fcm;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webzino.milkdelightuser.R;

import org.json.JSONObject;

import androidx.annotation.NonNull;


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

            if (remoteMessage.getNotification() != null) {
                Log.e("NOTIFICATION", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
            if (remoteMessage.getData().size() > 0) {
                Log.e("NOTIFICATION", "Message DATA PayLoad: " + remoteMessage.getData());
            }

            JSONObject notificationObject = new JSONObject(remoteMessage.getData());
            try {
                JSONObject objNotificationData = new JSONObject(notificationObject.optString("notification_data"));
                String message = notificationObject.optString("message");

                sendNotification(getString(R.string.app_name), message, objNotificationData);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", "" + e.toString());
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendNotification(String title, String messageBody, JSONObject responseObject) {
//
//        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
//        editor.putBoolean(SharePreferenceManager.NOTIFICATION_BADGE, true);
//        editor.apply();
//
//        String notificationType = responseObject.optString("notification_type");
//        Intent intent = new Intent(this, ActivityDashboard.class);
//        intent.putExtra("From", "");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = "Acuidarnos";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.app_icon)
//                        .setContentTitle(title)
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
