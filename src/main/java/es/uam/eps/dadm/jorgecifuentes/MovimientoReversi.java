package es.uam.eps.dadm.jorgecifuentes;

import es.uam.eps.multij.Movimiento;

/**
 * Created by jorgecf on 1/02/17.
 */

public class MovimientoReversi extends Movimiento {

    int x, y; // coordenada destino en el tablero

    public String toString() {
        return String.valueOf(this.x) + String.valueOf(this.y);
    }

    public boolean equals(Object o) {

        if (o instanceof MovimientoReversi) {
            if (this.x == ((MovimientoReversi) o).x && this.y == ((MovimientoReversi) o).y) {
                return true;
            }
        }

        return false;
    }
}
