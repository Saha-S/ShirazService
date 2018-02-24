package com.rahbaran.shirazservice;

/**
 * Created by AndroidBash on 20-Aug-16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rahbaran.shirazservice.activities.MainActivity;
import com.rahbaran.shirazservice.activities.MyServices;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    private String msg , title;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
          //  msg = remoteMessage.getNotification().getBody();

        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        //message will contain the Push Message
        String requestId="-";
        try {
            requestId = remoteMessage.getData().get("requestId");
        }
        catch (Exception e){}
        //imageUri will contain URL of the image to be displayed with Notification
      //  String imageUri = remoteMessage.getData().get("image");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
            String mod = remoteMessage.getData().get("mod");
        msg = remoteMessage.getData().get("body");
        title = remoteMessage.getData().get("title");

        //To get a Bitmap image from the URL received
      //  bitmap = getBitmapfromUrl(imageUri);

        sendNotification(title,msg,mod, requestId);

    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageTitle,String messageBody,String mod, String requestId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("mod", mod);
       // intent.putExtra("requestId", requestId);
        MyServices.requestId = requestId;
        MainActivity.requestId = requestId;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(messageTitle)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentText(messageBody);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());


       // notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}


