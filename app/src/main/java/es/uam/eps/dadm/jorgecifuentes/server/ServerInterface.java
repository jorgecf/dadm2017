package es.uam.eps.dadm.jorgecifuentes.server;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Interfaz de acceso al servidor del juego.
 *
 * @author Jorge Cifuentes
 */
public class ServerInterface {

    private static final String DEBUG = "DEBUG";

    public static final int GAME_ID = 34; // Codigo generado por el servidor

    private static final String BASE_URL = "http://ptha.ii.uam.es/dadm2017/";
    private static final String ACCOUNT_PHP = "account.php";
    private static final String IS_MY_TURN_PHP = "ismyturn.php";
    private static final String OPEN_ROUNDS_PHP = "openrounds.php";
    private static final String ACTIVE_ROUNDS_PHP = "activerounds.php";
    private static final String ROUND_HISTORY_PHP = "roundhistory.php";
    private static final String NEW_MOVEMENT_PHP = "newmovement.php";
    private static final String NEW_ROUND_PHP = "newround.php";
    private static final String ADD_PLAYER_TO_ROUND_PHP = "addplayertoround.php";
    private static final String SEND_MESSAGE = "sendmsg.php";

    private RequestQueue queue;

    private static ServerInterface serverInterface;

    private ServerInterface(Context context) { // privado para Singleton
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static ServerInterface getServer(Context context) {

        if (serverInterface == null) {
            serverInterface = new ServerInterface(context);
        }

        return serverInterface;
    }

    public void login(final String playername, final String password, final String regid, final boolean register, Response.Listener<String> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + ACCOUNT_PHP;
        Log.d(DEBUG, url);


        StringRequest r = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("playername", playername);
                params.put("playerpassword", password);

                if (regid != null && !regid.isEmpty()) params.put("gcmregid", regid);
                if (!register) params.put("login", "");

                return params;
            }
        };

        this.queue.add(r);
    }

    public void sendBoard(int roundid, String playerid, String codedboard, Response.Listener<String> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + NEW_MOVEMENT_PHP + "?roundid=" + roundid + "&playerid=" + playerid + "&codedboard=" + codedboard;
        Log.d(DEBUG, url);

        StringRequest r = new StringRequest(Request.Method.GET, url, callback, errorCallback);
        this.queue.add(r);
    }


    public void isMyTurn(final int roundid, final String playerid, Response.Listener<JSONObject> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + IS_MY_TURN_PHP + "?roundid=" + roundid + "&playerid=" + playerid;
        Log.d(DEBUG, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, callback, errorCallback);
        this.queue.add(jsonObjectRequest);
    }

    public void getOpenRounds(final String playerid, Response.Listener<JSONArray> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + OPEN_ROUNDS_PHP + "?gameid=" + GAME_ID + "&playerid=" + playerid;
        Log.d(DEBUG, url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, callback, errorCallback);
        this.queue.add(jsonArrayRequest);
    }

    public void getActiveRounds(final String playerid, Response.Listener<JSONArray> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + ACTIVE_ROUNDS_PHP + "?gameid=" + GAME_ID + "&playerid=" + playerid;
        Log.d(DEBUG, url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, callback, errorCallback);
        this.queue.add(jsonArrayRequest);
    }

    public void newRound(String playerid, String codedboard, Response.Listener<String> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + NEW_ROUND_PHP + "?gameid=" + GAME_ID + "&playerid=" + playerid;
        if (codedboard != null && codedboard.length() > 0) {
            url += "&codedboard=" + codedboard;
        }

        Log.d(DEBUG, url);

        StringRequest r = new StringRequest(Request.Method.GET, url, callback, errorCallback);
        this.queue.add(r);
    }

    public void addPlayerToRound(int roundid, String playerid, Response.Listener<String> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + ADD_PLAYER_TO_ROUND_PHP + "?roundid=" + roundid + "&playerid=" + playerid;
        Log.d(DEBUG, url);

        StringRequest r = new StringRequest(Request.Method.GET, url, callback, errorCallback);
        this.queue.add(r);
    }

    public void sendMsg(final String playerId, String playerDest, String message, Response.Listener<String> callback, Response.ErrorListener errorCallback) {

        String url = BASE_URL + SEND_MESSAGE + "?playerid=" + playerId + "&to=" + playerDest + "&msg=" + message;

        Log.d("debug", "Enviando msg :" + url);

        StringRequest r = new StringRequest(Request.Method.GET, url, callback, errorCallback);
        this.queue.add(r);
    }
}