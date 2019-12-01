package com.example.cloudmessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.cloudmessaging.App.FCM_CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TOKEN_BROADCAST = "mytokenbroadcast";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d("Messaging Service", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(token);
    }

    private void storeToken(String token){
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }

    public static final String TAG = "MyTag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: called");

        Log.d(TAG, "onMessageReceived: Message received from: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Notification notification = new NotificationCompat.Builder(this, FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.BLUE)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002, notification);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived: Data Size: " + remoteMessage.getData().size());

            for (String key : remoteMessage.getData().keySet()) {
                Log.d(TAG, "onMessageReceived Key: " + key + " Data: " + remoteMessage.getData().get(key));
            }

            Log.d(TAG, "onMessageReceived: Data: " + remoteMessage.getData().toString());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: called");
    }
}
