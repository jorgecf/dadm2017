package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import es.uam.eps.dadm.jorgecifuentes.activities.RoundPreferenceActivity;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.server.ServerInterface;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 24/04/17.
 */

public class ReversiLocalServerPlayer implements Jugador, ReversiView.OnPlayListener {

    private static final String DEBUG = "DEBUG";

    private Partida game;
    private TableroReversi t;
    private String name;
    private Context context;
    private String roundId;

    private ReversiView board;

    public ReversiLocalServerPlayer(TableroReversi t, String name, Context context, String roundId, ReversiView board) {
        this.t = t;
        this.name = name;
        this.context = context;
        this.roundId = roundId;
        this.board = board;
    }

    public void setPartida(Partida game) {
        this.game = game;
    }


    private boolean isBoardUpToDate(String codedboard) {
        return t.tableroToString().equals(codedboard);
    }

    @Override
    public void onPlay(final int row, final int column) {

        Movimiento m = t.getMovimientoValido(row, column);
        final AccionMover acc = new AccionMover(this, m);


        ServerInterface is = ServerInterface.getServer(context);

        Response.Listener<JSONObject> responseListener = new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int isMyTurn = response.getInt("turn");
                            String codedboard = response.getString("codedboard");

                            // Si el turno es del jugador y el tablero está actualizado realiza movimiento
                            if (isMyTurn == 1 && isBoardUpToDate(codedboard)) {
                                Log.d("[debug]", "realizar movimiento");
                                t.stringToTablero(codedboard);

                                game.realizaAccion(acc);
                            }

                            // Si el turno es del jugador pero el tablero no está actualizado, actualizar tablero
                            if (isMyTurn == 1 && isBoardUpToDate(codedboard) == false) {
                                Log.d("[debug]", "actualizar tablero");
                                t.stringToTablero(codedboard); //TODO actualizado graficamente?
                                board.invalidate();
                            }

                            if (isMyTurn == 0) {
                                // Si el turno no es del jugador, mostrar mensaje
                                Log.d("[debug]", "no es turno de RLSplayer");
                            }

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

        String playerId = RoundPreferenceActivity.getPlayerUUID(this.context); //TODO probar
        is.isMyTurn(Integer.parseInt(roundId), playerId, responseListener, errorListener);
    }

    @Override
    public String getNombre() {
        return this.name;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        // Vacio
    }
}
