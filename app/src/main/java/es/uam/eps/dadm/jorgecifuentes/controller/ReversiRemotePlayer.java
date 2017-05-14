package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import es.uam.eps.dadm.jorgecifuentes.server.ServerInterface;
import es.uam.eps.dadm.jorgecifuentes.server.ServerRepository;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Jugador remoto para juego en red. Se encarga de enviar la nueva jugada valida del
 * LocalServerPlayer al servidor.
 *
 * @author Jorge Cifuentes
 */
public class ReversiRemotePlayer implements Jugador {

    private Context context;
    private String roundUUID;
    private String playerUUID;

    private String playername;
    private String rivalName;

    public ReversiRemotePlayer(Context context, String p, String r, String rivalName) {
        this.context = context;
        this.roundUUID = r;
        this.playerUUID = p;
        this.rivalName = rivalName;
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

        Partida p = evento.getPartida();

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:

                if (evento.getCausa() == null) {
                    Log.d("debug", "onCambioEnPartida +  CAMBIO: no ha sido turno del local server player");
                    return;
                }

                // Si el movimiento es correcto, el tablero se envia y ademas notifica mediante firebase fcm
                ServerInterface.getServer(this.context).sendBoard(Integer.parseInt(roundUUID), playerUUID, p.getTablero().tableroToString(),

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                Log.d("debug", "onResponse: remote " + result);
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("debug", "onResponse: remote ERROR!  " + volleyError.getLocalizedMessage());
                            }
                        });

                break;
            case Evento.EVENTO_TURNO:
                break;
        }
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
