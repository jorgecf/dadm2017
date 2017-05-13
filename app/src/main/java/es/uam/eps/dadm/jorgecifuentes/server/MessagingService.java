package es.uam.eps.dadm.jorgecifuentes.server;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import es.uam.eps.dadm.jorgecifuentes.activities.MessageActivity;

/**
 * Created by jorgecf on 13/05/17.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String CONTENT = "content";
    private static final String SENDER = "sender";
    private static final String MSG_TYPE = "msgtype";
    private static final String ROUND_MOVEMENT = "1";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("debug", "onCreate: creando msger");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("debug", "onMessageReceived: RECIBIDO!");

   /*     SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msString = dateformat.format(new Date());

        // check if msg contains data payload

        // TODO verify message type
        if(remoteMessage.getMessageType().equals(ROUND_MOVEMENT)) {
            Log.d("debug", "onMessageReceived: ROUND MOV.");
        }

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());

            // Pasamos los datos recibidos por FCM al intent medianto LocalBroadcast
            Intent intent = new Intent(MessageActivity.ACTION_MESSAGE);
            intent.putExtra(MessageActivity.MESSAGE, data.get(CONTENT));
            intent.putExtra(MessageActivity.SENDER, data.get(SENDER));
            intent.putExtra(MessageActivity.DATE, msString);
            manager.sendBroadcast(intent);
        }*/

    }
}
