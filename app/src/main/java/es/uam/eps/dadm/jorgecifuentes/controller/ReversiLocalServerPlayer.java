package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import es.uam.eps.dadm.jorgecifuentes.server.ServerInterface;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 24/04/17.
 */

public class ReversiLocalServerPlayer implements Jugador, ReversiView.OnPlayListener {

    private static final String DEBUG = "DEBUG";
    private Partida game;
    private Context context;
    private String roundId;

    public ReversiLocalServerPlayer(Context context, String roundId) {
        this.context = context;
        this.roundId = roundId;
    }

    private boolean isBoardUpToDate(String codedboard) {
        return game.getTablero().tableroToString().equals(codedboard);
    }

    @Override
    public void onPlay(final int row, final int column) {

        ServerInterface is = ServerInterface.getServer(context);

        Response.Listener<JSONObject> responseListener = new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Si el turno es del jugador y el tablero está actualizado realiza movimiento
                            // Si el turno es del jugador pero el tablero no está actualizado, actualizar tablero
                            // Si el turno no es del jugador, mostrar mensaje
                        } catch (Exception e) {
                            Log.d(DEBUG, "" + e);
                        }
                    }
                };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        //...

        is.isMyTurn(Integer.parseInt(roundId), playerId, responseListener, errorListener);
    }

    @Override
    public String getNombre() {
        return null; //TODO
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        //TODO
    }
}
