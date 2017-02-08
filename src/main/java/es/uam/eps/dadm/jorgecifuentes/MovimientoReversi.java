package es.uam.eps.dadm.jorgecifuentes;


import java.util.ArrayList;
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

    private Coordenada destino; // coordenada destino en el tablero
    private ArrayList<Coordenada> inicio; // lista de inicios posibles
    private ArrayList<Integer> pasos; // lista de pasos desde cada inicio

    public Coordenada getDestino() {
        return destino;
    }

    public ArrayList<Coordenada> getInicio() {
        return inicio;
    }

    public ArrayList<Integer> getPasos() {
        return pasos;
    }

    public MovimientoReversi(int dest_x, int dest_y, int ini_x, int ini_y, int pasos) {

        if (this.inicio == null) this.inicio = new ArrayList<Coordenada>();
        if (this.pasos == null) this.pasos = new ArrayList<Integer>();

        this.destino = new Coordenada(dest_x, dest_y);
        this.inicio.add(new Coordenada(ini_x, ini_y));
        this.pasos.add(pasos);
    }

    @Override
    public String toString() {
        return "Movimiento desde " + this.inicio.stream().map(ini -> ini.toString()).collect(Collectors.joining(", ")) + " hasta: " + this.destino.toString();
    }


    @Override
    public boolean equals(Object o) {

        if (o instanceof MovimientoReversi) {

            MovimientoReversi mo = (MovimientoReversi) o;

            if (mo.inicio.equals(this.inicio) == false) {
                return false;
            }

            if (mo.pasos.equals(this.pasos) == false) {
                return false;
            }

            if (mo.destino.equals(this.destino) == false) {
                return false;
            }

            return true;
        }

        return false;
    }

}