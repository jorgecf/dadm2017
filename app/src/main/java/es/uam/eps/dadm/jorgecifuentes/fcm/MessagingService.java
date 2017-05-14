package es.uam.eps.dadm.jorgecifuentes.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import es.uam.eps.dadm.jorgecifuentes.controller.RoundListFragment;


/**
 * Created by jorgecf on 13/05/17.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String CONTENT = "content";
    private static final String SENDER = "sender";
    private static final String MSG_TYPE = "msgtype";
    private static final String ROUND_MOVEMENT = "1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msString = dateformat.format(new Date());

        // estos mensajes los crea el servidor al eniar un newmovement.php
        if (remoteMessage.getData() != null && remoteMessage.getData().get(MSG_TYPE) != null && remoteMessage.getData().get(MSG_TYPE).equals(ROUND_MOVEMENT)) {
            Log.d("debug", "onMessageReceived: ROUND MOVEMENT");

            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();

                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());

                // Pasamos los datos recibidos por FCM al intent mediante LocalBroadcast
                Intent intent = new Intent(RoundListFragment.MessageReceiver.ACTION_MESSAGE);
                intent.putExtra(RoundListFragment.MessageReceiver.MESSAGE, data.get(CONTENT));
                intent.putExtra(RoundListFragment.MessageReceiver.SENDER, data.get(SENDER));
                intent.putExtra(RoundListFragment.MessageReceiver.DATE, msString);
                manager.sendBroadcast(intent);
            }
        }
    }
}
