package es.uam.eps.dadm.jorgecifuentes;


import com.google.common.collect.Table;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;

/**
 * Created by jorgecf on 1/02/17.
 */

public class Main2 {

    public static void main2(String[] args) {

        System.out.println("Funciona");

        TableroReversi t = new TableroReversi();

        System.out.println(t.toString());

        try {
            t.stringToTablero(
                    "VVVVVVVV" +
                            "VBBBVVVV" +
                            "NVNVNVVV" +
                            "VVVVVVNB" +
                            "VVVVBVVV" +
                            "VVVBVBVV" +
                            "VVNVVVNV" +
                            "VVVVVVVV" +
                            "0");
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }


        System.out.println(t.tableroToString());


        try {
            t.mueve(t.movimientosValidos().get(0));
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }

        System.out.println(t.toString());
        System.out.println("representacion interna: \n" + t.tableroToString());


        // TODO -> hacer test con esto
        TableroReversi t2 = new TableroReversi();

        try {
            t2.stringToTablero(t.tableroToString());
            System.out.println(t2.toString());
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }


    }
}
