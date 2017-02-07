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
        JugadorReversi jugadorHumano1 = new JugadorReversi("jugadorH1");
        JugadorReversi jugadorHumano2 = new JugadorReversi("jugadorH2");

        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        jugadores.add(jugadorHumano1);
        jugadores.add(jugadorHumano2);

        Partida partida = new Partida(new TableroReversi(), jugadores);
        partida.addObservador(observador);

     /*   try {
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
*/

   /*
   A B C D E F G H
     _ _ _ _ _ _ _ _

 1 | B N N N N B B B
 2 | B N B B B B B V
 3 | B N N B B B N B
 4 | B N N N B N N V
 5 | B N N B B B N V
 6 | B N B N B B B B
 7 | B B B B B B B B
 8 | B B B B B B N B
Turno de: BLANCO
*/
       try {
            partida.getTablero().stringToTablero(
                    "BNNNNBBB" +
                            "BNBBBBBV" +
                            "BNNBBBNB" +
                            "BNNNBNNV" +
                            "BNNBBBNV" +
                            "BNBNBBBB" +
                            "BBBBBBBB" +
                            "BBBBBBNB" +
                            "1" +
                            "57");
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
            return;
        }


        partida.comenzar();
        // ...
    }
}
