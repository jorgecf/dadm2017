package es.uam.eps.dadm.jorgecifuentes.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;

import es.uam.eps.dadm.jorgecifuentes.fcm.Message;

/**
 * Created by jorgecf on 13/05/17.
 */

public class MessageActivity extends Activity {

    public static final String ACTION_MESSAGE = "actionMessage";
    public static final String MESSAGE = "message";
    public static final String DATE = "date";
    public static final String SENDER = "sender";

    private MessageReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.receiver = new MessageReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter(ACTION_MESSAGE);
        manager.registerReceiver(this.receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(this.receiver);
    }

    public static class MessageReceiver extends BroadcastReceiver {

        private Context context;
        private static int message_id = 0;
        private List<Message> messages;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;

            // Informacion del intent
            Bundle extras = intent.getExtras();
            String action = intent.getAction();

            // Obtenemos los datos que nos ha dado nuestro MessagingService
            if (action.equals(ACTION_MESSAGE)) {
                Message message = new Message(message_id++, extras.getString(MESSAGE), extras.getString(DATE), extras.getString(SENDER));
                messages.add(message);
            }

        }
    }
}