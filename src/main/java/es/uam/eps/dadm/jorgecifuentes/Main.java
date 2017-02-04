package es.uam.eps.dadm.jorgecifuentes;

import java.util.ArrayList;

import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 4/02/17.
 */

public class Main {

    public static void main(String[] args) {
        // ...
        JugadorReversi observador = new JugadorReversi("observador");
        JugadorAleatorio jugadorAleatorio = new JugadorAleatorio("jugador1");
        JugadorReversi jugadorHumano = new JugadorReversi("jugador2");

        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        jugadores.add(jugadorHumano);
        jugadores.add(jugadorAleatorio);

        Partida partida = new Partida(new TableroReversi(), jugadores);
        partida.addObservador(observador);

        System.out.println(partida.getTablero().toString());

        partida.comenzar();
        // ...
    }
}
