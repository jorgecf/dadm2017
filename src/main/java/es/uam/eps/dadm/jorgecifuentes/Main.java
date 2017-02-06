package es.uam.eps.dadm.jorgecifuentes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;

/**
 * Created by jorgecf on 4/02/17.
 */

public class Main {


    public static void main(String[] args) {

        // ...
        ObservadorReversi observador = new ObservadorReversi("observador");
        JugadorReversi jugadorAleatorio = new JugadorReversi("jugador1");
        JugadorReversi jugadorHumano = new JugadorReversi("jugador2");

        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        jugadores.add(jugadorHumano);
        jugadores.add(jugadorAleatorio);

        Partida partida = new Partida(new TableroReversi(), jugadores);
        partida.addObservador(observador);

        try {
            partida.getTablero().stringToTablero(
                    "NNNNNNNN" +
                            "NNNNNNNN" +
                            "NNNNNNNN" +
                            "NNNNNNNN" +
                            "NNNNNNNB" +
                            "VVVBVBVV" +
                            "VVNVVVNV" +
                            "NNNNNNNN" +
                            "0");
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }


     /*   try {
            partida.getTablero().stringToTablero(
                    "BBBBBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBBB" +
                            "0");
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }*/


        partida.comenzar();
        // ...
    }
}
