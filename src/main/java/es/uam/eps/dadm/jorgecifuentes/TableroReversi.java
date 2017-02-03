package es.uam.eps.dadm.jorgecifuentes;

import java.util.ArrayList;
import java.util.Arrays;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;


/**
 * Created by jorgecf on 1/02/17.
 */


public class TableroReversi extends Tablero {

    public enum Color {
        VACIO, NEGRO, BLANCO;

        public char toChar() {
            return this.name().charAt(0);
        }

    }

    private int lado = 8;
    char[][] tablero;


    public TableroReversi() {
        this.tablero = new char[lado][lado];

        for (int i = 0; i < lado; i++) {
            Arrays.fill(this.tablero[i], Color.VACIO.toChar());
        }
    }

    @Override
    protected void mueve(Movimiento movimiento) throws ExcepcionJuego {

        if (this.esValido(movimiento) == false) return;
        //TODO actualizar tablero y turno y estado y etc
    }

    @Override
    public boolean esValido(Movimiento movimiento) {
        return this.movimientosValidos().contains(movimiento);
    }

    @Override
    public ArrayList<Movimiento> movimientosValidos() {

        ArrayList<Movimiento> ret = new ArrayList<Movimiento>();

        // iteramos sobre todas las casillas
        for (int i = 0; i < lado; i++) {
            for (int j = 0; j < lado; j++) {

                if (this.tablero[i][j] == Color.BLANCO.toChar() || this.tablero[i][j] == Color.VACIO.toChar()) { // TODO color RIVAL
                    // System.out.println("COLOR RIVAL o BLANCO, no juega");
                } else {

                    // para cada casilla, comprobamos sus ocho direcciones
                    ret.addAll(DireccionMovimientosValidos(i, j, 0, -1)); // norte
                    ret.addAll(DireccionMovimientosValidos(i, j, 0, 1)); // sur
                    ret.addAll(DireccionMovimientosValidos(i, j, -1, 0)); // este
                    ret.addAll(DireccionMovimientosValidos(i, j, 1, 0)); // oeste
                    ret.addAll(DireccionMovimientosValidos(i, j, -1, -1)); // noreste
                    ret.addAll(DireccionMovimientosValidos(i, j, 1, -1)); // noroeste
                    ret.addAll(DireccionMovimientosValidos(i, j, -1, 1)); // sureste
                    ret.addAll(DireccionMovimientosValidos(i, j, 1, 1)); //suroeste
                }
            }
        }


        return ret;
    }

    /**
     * @param i
     * @param j
     * @param dir_i
     * @param dir_j
     * @return
     */
    private ArrayList<Movimiento> DireccionMovimientosValidos(int i, int j, int dir_i, int dir_j) {

        ArrayList<Movimiento> ret = new ArrayList<Movimiento>();

        int flag_rival = 0;
        for (int k = 1; k < this.lado; k++) {

            int pos_i = i + (k * dir_i);
            int pos_j = j + (k * dir_j);


            if (pos_i > lado - 1 || pos_i < 0 || pos_j > lado - 1 || pos_j < 0)
                break; //out of bounds

            if (this.tablero[pos_i][pos_j] == Color.VACIO.toChar() && flag_rival == 1) {
                // add a validos
                System.out.println("Mov valido FUNC " + String.valueOf(i) + ", " + String.valueOf(j) + "== " + String.valueOf(pos_i) + ", " + String.valueOf(j + (k * dir_j)));
                ret.add(new MovimientoReversi(pos_i, pos_j));
                break;
            } else if (this.tablero[pos_i][pos_j] == Color.BLANCO.toChar()) { // estado
                flag_rival = 1;
            } else if (this.tablero[pos_i][pos_j] == Color.NEGRO.toChar()) { //TODO rival
                break;
            }
        }

        return ret;
    }

    @Override
    public String tableroToString() {

        String ret = ""; // TODO numero de casilla

        for (int j = 0; j < lado; j++) {
            for (int i = 0; i < lado; i++) {
                ret += this.tablero[i][j] + " ";
            }
            ret += "\n";
        }

        return ret;
    }

    @Override
    public void stringToTablero(String s) throws ExcepcionJuego {

        // TODO throwear excepcion

        for (int i = 0; i < lado; i++) {
            for (int j = 0; j < lado; j++) {
                this.tablero[j][i] = s.charAt(i + j + ((lado - 1) * i));
            }
        }
    }

    @Override
    public String toString() {
        return null;
    }
}
