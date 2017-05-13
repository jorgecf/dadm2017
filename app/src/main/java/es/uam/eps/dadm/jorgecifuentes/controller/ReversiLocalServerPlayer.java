package es.uam.eps.dadm.jorgecifuentes.controller;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import es.uam.eps.dadm.jorgecifuentes.R;
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
 * Jugador local para juego en red.
 *
 * @author Jorge Cifuentes
 */
public class ReversiLocalServerPlayer implements Jugador, ReversiView.OnPlayListener {

    private static final String DEBUG = "DEBUG";
    private final View view;

    private Partida game;

    private String playername;
    private Context context;
    private String roundId;

    public ReversiLocalServerPlayer(Context context, View view, String roundId) {
        this.context = context;
        this.roundId = roundId;
        this.view = view;
    }


    public void setPartida(Partida game) {
        this.game = game;
    }


    private boolean isBoardUpToDate(String codedboard) {
        TableroReversi t = (TableroReversi) this.game.getTablero();
        return t.tableroToString().equals(codedboard);
    }

    @Override
    public void onPlay(final int row, final int column) {

        final TableroReversi t = (TableroReversi) this.game.getTablero();
        final Movimiento m = t.getMovimientoValido(row, column);
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

                                // Evitamos jugar movimientos invalidos
                                if (m != null) game.realizaAccion(acc);
                            }

                            // Si el turno es del jugador pero el tablero no está actualizado, actualizar tablero
                            else if (isMyTurn == 1 && isBoardUpToDate(codedboard) == false) {
                                Snackbar.make(view, context.getString(R.string.board_updated_play_again), Snackbar.LENGTH_LONG).show();
                                t.stringToTablero(codedboard);

                                final ReversiView b = (ReversiView) ((Activity) context).findViewById(R.id.board_reversiview);
                                b.setBoard(game.getTablero());
                                b.postInvalidate();

                            } else if (isMyTurn == 0) {
                                // Si el turno no es del jugador, mostrar mensaje
                                Snackbar.make(view, context.getString(R.string.not_your_turn), Snackbar.LENGTH_LONG).show();
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


        String playerId = RoundPreferenceActivity.getPlayerUUID(this.context);
        is.isMyTurn(Integer.parseInt(roundId), playerId, responseListener, errorListener);
    }

    @Override
    public String getNombre() {
        return this.playername;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        // Vacio
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
