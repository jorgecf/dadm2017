package es.uam.eps.dadm.jorgecifuentes;


import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;

/**
 * Created by jorgecf on 1/02/17.
 */

public class Main {

    public static void main(String[] args) {

        System.out.println("Funciona");

        TableroReversi t = new TableroReversi();

        System.out.println(t.tableroToString());

        /*t.tablero[0][0]= TableroReversi.Color.VACIO.toChar();
        t.tablero[0][1]= TableroReversi.Color.BLANCO.toChar();
        t.tablero[0][2]= TableroReversi.Color.NEGRO.toChar();
*/

        try {
            t.stringToTablero(
                    "VBNVVVVV" +
                            "NBBVVVVV" +
                            "VVVVVVBN" +
                            "VVVVVVNB" +
                            "VVVVBVVV" +
                            "VVVBVBVV" +
                            "VVNVVVNV" +
                            "VVVVVVVV");
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }


        System.out.println(t.tableroToString());

        for (Movimiento m : t.movimientosValidos()
                ) {
            System.out.println(m.toString());
        }


    }
}
