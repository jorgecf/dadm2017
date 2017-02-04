package es.uam.eps.dadm.jorgecifuentes;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 4/02/17.
 */

public class JugadorReversi implements Jugador {



    @Override
    public String getNombre() {
        return null;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return false;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

    }
}
