package es.uam.eps.dadm.jorgecifuentes;

import java.util.InputMismatchException;
import java.util.Scanner;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.PartidaListener;

/**
 * Created by jorgecf on 6/02/17.
 */

public class ObservadorReversi implements PartidaListener {

    private String nombre;

    public ObservadorReversi(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

        System.out.println(this.getNombre() + ": " + evento.getDescripcion());

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                System.out.println(evento.getPartida().getTablero().toString());
                break;

            case Evento.EVENTO_TURNO: // observador no tiene turno
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
