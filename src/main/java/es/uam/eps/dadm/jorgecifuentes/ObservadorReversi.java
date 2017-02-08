package es.uam.eps.dadm.jorgecifuentes;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.PartidaListener;

/**
 * Clase que representa un observador del juego Reversi. Es el unico encargado de imprimir por
 * pantalla tanto las descripciones de los Eventos como el tablero.
 *
 * @author Jorge Cifuentes
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
                break;
            case Evento.EVENTO_ERROR:
                break;
        }
    }
}
