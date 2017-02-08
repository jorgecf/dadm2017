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
                break;

            case Evento.EVENTO_TURNO:

                int x = -1;
                int y = -1;
                Scanner in = new Scanner(System.in);

                Movimiento mov;

                // si no hay movimientos posibles, hay que pasar
                //  (podria hacerse una clase AccionPasar, pero para ello habria que modificar Partida.java)
                if (p.getTablero().movimientosValidos().size() == 0) {
                    mov = null; // pasar
                } else {

                    do {
                        System.out.println("Introduzca un movimiento valido:");

                        try {
                            x = in.nextInt();
                            y = in.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Valor no valido, se espera x y");
                            in = new Scanner(System.in);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // comprobamos si el movimiento es valido en nuestro tablero
                        mov = t.getMovimientoValido(x, y);
                    } while (mov == null);
                }

                // realizamos la accion
                try {
                    p.realizaAccion(new AccionMover(this, mov));
                } catch (ExcepcionJuego excepcionJuego) {
                    excepcionJuego.printStackTrace();
                }

                break;
            case Evento.EVENTO_FIN:
                break;
            case Evento.EVENTO_ERROR:
                evento.getPartida().continuar();
                break;
        }
    }
}
