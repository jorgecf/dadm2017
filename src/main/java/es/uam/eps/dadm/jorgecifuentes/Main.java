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

        try {
            t.stringToTablero(
                            "VVVVVVVV" +
                            "VBBBVVVV" +
                            "NVNVNVVV" +
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

        try {
            t.mueve(t.movimientosValidos().get(0));
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }

        System.out.println(t.tableroToString());



    }
}
