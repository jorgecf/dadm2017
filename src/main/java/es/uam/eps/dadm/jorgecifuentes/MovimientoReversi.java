package es.uam.eps.dadm.jorgecifuentes;

import es.uam.eps.multij.Movimiento;

/**
 * Created by jorgecf on 1/02/17.
 */

public class MovimientoReversi extends Movimiento {

    int x, y; // coordenada destino en el tablero

    public MovimientoReversi(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Movimiento destino: [" + String.valueOf(this.x) + ", " + String.valueOf(this.y) + "]";
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof MovimientoReversi) {
            if (this.x == ((MovimientoReversi) o).x && this.y == ((MovimientoReversi) o).y) {
                return true;
            }
        }

        return false;
    }
}