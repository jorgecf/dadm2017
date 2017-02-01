package es.uam.eps.dadm.jorgecifuentes;

import es.uam.eps.multij.Movimiento;

/**
 * Created by jorgecf on 1/02/17.
 */

public class MovimientoReversi extends Movimiento {

    Coordenada posicion;

    public String toString() {
        return this.posicion.toString();
    }

    public boolean equals(Object o) {

        if (o instanceof MovimientoReversi && ((MovimientoReversi) o).posicion == this.posicion) {
            return true;
        }

        return false;
    }
}
