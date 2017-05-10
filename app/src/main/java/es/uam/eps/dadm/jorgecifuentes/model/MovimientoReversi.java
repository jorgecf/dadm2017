package es.uam.eps.dadm.jorgecifuentes.model;

import java.util.ArrayList;
/**
 * @TODO darg sobre este import
 */
import java.util.stream.Collectors;

import es.uam.eps.multij.Movimiento;

/**
 * Clase que representa un movimiento en el juego del Reversi. Consta de:
 * - una coordenada destino
 * - una lista de inicios (ya que a un destino se puede llegar desde varias coordenadas,
 * y todas ellas tienen que ser consideradas)
 * - una lista de pasos desde cada inicio hasta el destino, por simplicidad
 *
 * @author Jorge Cifuentes
 */
public class MovimientoReversi extends Movimiento {

    private CoordenadaReversi destino; // coordenada destino en el tablero
    private ArrayList<CoordenadaReversi> inicio; // lista de inicios posibles
    private ArrayList<Integer> pasos; // lista de pasos desde cada inicio

    public CoordenadaReversi getDestino() {
        return destino;
    }

    public ArrayList<CoordenadaReversi> getInicio() {
        return inicio;
    }

    public ArrayList<Integer> getPasos() {
        return pasos;
    }

    public MovimientoReversi(int dest_x, int dest_y, int ini_x, int ini_y, int pasos) {

        if (this.inicio == null) this.inicio = new ArrayList<CoordenadaReversi>();
        if (this.pasos == null) this.pasos = new ArrayList<Integer>();

        this.destino = new CoordenadaReversi(dest_x, dest_y);
        this.inicio.add(new CoordenadaReversi(ini_x, ini_y));
        this.pasos.add(pasos);
    }

    @Override
    public String toString() {
        return "Movimiento: " +  /*+ this.inicio.stream().map(ini -> ini.toString()).collect(Collectors.joining(", ")) + */" hasta: " + this.destino.toString();
    }


    @Override
    public boolean equals(Object o) {

        if (o instanceof MovimientoReversi){
            MovimientoReversi mo = (MovimientoReversi) o;
            return (mo.inicio.equals(this.inicio) && mo.pasos.equals(this.pasos) && mo.destino.equals(this.destino));
        }

        return false;
    }

}