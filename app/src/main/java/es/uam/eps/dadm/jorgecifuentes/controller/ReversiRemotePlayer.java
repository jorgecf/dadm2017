package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.util.Log;

import es.uam.eps.dadm.jorgecifuentes.server.ServerInterface;
import es.uam.eps.dadm.jorgecifuentes.server.ServerRepository;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 24/04/17.
 */

public class ReversiRemotePlayer implements Jugador {

    private Context context;

    public ReversiRemotePlayer(Context context) {
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

                break;
        }
    }
}
