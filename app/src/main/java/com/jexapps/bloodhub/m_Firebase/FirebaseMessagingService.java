package com.jexapps.bloodhub.m_Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.jexapps.bloodhub.MainActivity;
import com.jexapps.bloodhub.RequestDetail;
import com.jexapps.bloodhub.R;

/**
 * Created by hp on 8/11/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public String intentId, title, body, notificationKey;
    public Intent intent;
    public FirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        if (remoteMessage.getData().size() > 0) {
            intentId = remoteMessage.getData().get("requestId");
            notificationKey = remoteMessage.getData().get("notificationKey");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }
        sendNotification(title, body, intentId, notificationKey);
    }

    @Override
    public void onDeletedMessages(){

    }

    private void sendNotification(String title,String messageBody, String intentId, String key) {
        int val = Integer.parseInt(key);
        if (val == 1){
            intent = new Intent(this, RequestDetail.class);
            intent.putExtra("request", intentId);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
