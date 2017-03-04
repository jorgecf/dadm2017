package es.uam.eps.dadm.jorgecifuentes;

import java.util.ArrayList;

import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.JugadorAleatorio;

/**
 * Main del juego.
 *
 * @author Jorge Cifuentes
 */
public class Main {

    public static void main(String[] args) {

	/**
	 * @TODO darg conviene incluir en el main un menú para
	 * seleccionar opciones de ejecución. Estas opciones de
	 * ejecución son: jugar con dos humanos, dos aleatorios, un
	 * humano y un aleatorio. Además, una opción para probar
	 * tableroToString y StringToTablero. Esto es algo que
	 * remarque en clase en varias ocasiones.
	 * 
	 */

        ObservadorReversi observador = new ObservadorReversi("observador");
        JugadorReversi jugadorHumano1 = new JugadorReversi("jugadorH1");
        JugadorReversi jugadorHumano2 = new JugadorReversi("jugadorH2");

	JugadorAleatorio aleatorio = new JugadorAleatorio();
	JugadorAleatorio aleatorio2 = new JugadorAleatorio();
	
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

	/*jugadores.add(jugadorHumano1);
	  jugadores.add(jugadorHumano2);*/

	
        jugadores.add(aleatorio);
	jugadores.add(aleatorio2);

        Partida partida = new Partida(new TableroReversi(), jugadores);
        partida.addObservador(observador);

        partida.comenzar();

	/**
	 * @TODO darg no me queda claro en el código qué haces si uno
	 * de los jugadores no puede mover.
	 * 
	 */

	
    }
}
