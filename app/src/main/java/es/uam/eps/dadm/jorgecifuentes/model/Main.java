package es.uam.eps.dadm.jorgecifuentes.model;

import java.util.ArrayList;

import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;

/**
 * Main del juego.
 *
 * @author Jorge Cifuentes
 */
public class Main {

    public static void main(String[] args) {

        ObservadorReversi observador = new ObservadorReversi("observador");
        JugadorReversi jugadorHumano1 = new JugadorReversi("jugadorH1");
        JugadorReversi jugadorHumano2 = new JugadorReversi("jugadorH2");

        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        jugadores.add(jugadorHumano1);
        jugadores.add(jugadorHumano2);

        Partida partida = new Partida(new TableroReversi(), jugadores);
        partida.addObservador(observador);

        partida.comenzar();
    }
}
