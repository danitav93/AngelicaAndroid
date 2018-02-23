package com.example.danieletavernelli.angelica.firebase;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.danieletavernelli.angelica.utility.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
  Created by Daniele Tavernelli on 2/21/2018.
 */

public class MessageService extends FirebaseMessagingService {

    public static final String TAG = "FB Message Service";

    public static final String ACTION_CHAT_MESSAGE_RECEIVED_FROM_SERVER = "acmrfs";

    public static final String EXTRA_ID_MESSAGGIO = "eim";

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Intent intent = new Intent();
            intent.setAction(ACTION_CHAT_MESSAGE_RECEIVED_FROM_SERVER);
            intent.putExtra(EXTRA_ID_MESSAGGIO,Long.parseLong(remoteMessage.getData().get(Constants.KEY_ID_MESSAGGIO)));
            broadcaster.sendBroadcast(intent);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}
