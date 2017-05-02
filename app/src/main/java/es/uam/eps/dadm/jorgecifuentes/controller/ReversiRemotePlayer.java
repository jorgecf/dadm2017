package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.server.ServerInterface;
import es.uam.eps.dadm.jorgecifuentes.server.ServerRepository;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 24/04/17.
 */

public class ReversiRemotePlayer implements Jugador {

    private Round round;
    private Context context;

    public ReversiRemotePlayer(Round round, Context context) {
        this.round = round;
        this.context = context;
    }

    @Override
    public String getNombre() {
        return "cambiame";
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_TURNO:
                Log.d("debug", "onCambioEnPartida: evento turno ha llegado");

                //     ServerRepository.getInstance(this.context).updateRound();
                ServerInterface.getServer(this.context).sendBoard(Integer.parseInt(round.getRoundUUID()), round.getPlayerUUID(), round.getBoard().tableroToString(), new Response.Listener<String>() {
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
        }
    }
}
