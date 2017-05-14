package es.uam.eps.dadm.jorgecifuentes.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * Servicio de mensajeria para capturar la actualizacion de rondas.
 *
 * @author Jorge Cifuentes
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

        // Estos mensajes los crea el servidor al enviar un newmovement.php
        if (remoteMessage.getData() != null && remoteMessage.getData().get(MSG_TYPE) != null && remoteMessage.getData().get(MSG_TYPE).equals(ROUND_MOVEMENT)) {
            Log.d("debug", "onMessageReceived: ROUND MOVEMENT");

            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();

                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());

                // Pasamos los datos recibidos por FCM al intent mediante LocalBroadcast
                Intent intent = new Intent(MessageFields.ACTION_MESSAGE);
                intent.putExtra(MessageFields.MESSAGE, data.get(CONTENT));
                intent.putExtra(MessageFields.SENDER, data.get(SENDER));
                intent.putExtra(MessageFields.DATE, msString);
                manager.sendBroadcast(intent);
            }
        }
    }
}
