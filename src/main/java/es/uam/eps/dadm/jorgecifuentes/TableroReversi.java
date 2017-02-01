package es.uam.eps.dadm.jorgecifuentes;

import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Created by jorgecf on 1/02/17.
 */

public class TableroReversi extends Tablero {

    ArrayList<Coordenada> tablero; // int?

    public TableroReversi(){

    }

    protected void mueve(Movimiento m) throws ExcepcionJuego {
        throw new NotImplementedException();
    }

    public boolean esValido(Movimiento m) {
        throw new NotImplementedException();

    }

    public ArrayList<Movimiento> movimientosValidos() {
        throw new NotImplementedException();
    }

    public String tableroToString() {
        throw new NotImplementedException();

    }

    public void stringToTablero(String cadena) throws ExcepcionJuego {
        throw new NotImplementedException();
    }

    public String toString() {
        throw new NotImplementedException();
    }
}
