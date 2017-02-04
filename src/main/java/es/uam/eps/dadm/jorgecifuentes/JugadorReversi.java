package es.uam.eps.dadm.jorgecifuentes;

import java.util.InputMismatchException;
import java.util.Scanner;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 4/02/17.
 */

public class JugadorReversi implements Jugador {


    private String nombre;
    private static int numJugadores = 0;

    public JugadorReversi(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

        Partida p = evento.getPartida();
        TableroReversi t = (TableroReversi) p.getTablero();

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                // ...
                System.out.println(" evento recibido: " + evento.getDescripcion());
                break;

            case Evento.EVENTO_TURNO:

                System.out.println(evento.getDescripcion());
                //   System.out.println(t.toString());


                int x = -1;
                int y = -1;
                Scanner in = new Scanner(System.in);

                try {
                    x = in.nextInt();
                    y = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Valor no valido, se espera x y");
                    // TODO ? in = new Scanner(System.in);
                } catch (Exception e) {
                    //   ...
                }

                // ...?

                // comprobamos si el movimiento es valido es nuestro tablero

                Movimiento mov = t.getMovimientoValido(x, y);

                System.out.println(mov);


                try {
                    p.realizaAccion(new AccionMover(this, mov));
                } catch (ExcepcionJuego excepcionJuego) {
                    excepcionJuego.printStackTrace();
                }

                break;
            case Evento.EVENTO_FIN:
                //...
                break;
            case Evento.EVENTO_ERROR:
                //...
                break;
        }
    }
}
