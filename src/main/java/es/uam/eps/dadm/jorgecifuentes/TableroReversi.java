package es.uam.eps.dadm.jorgecifuentes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;


/**
 * Created by jorgecf on 1/02/17.
 */


public class TableroReversi extends Tablero {

    public enum Color {
        NEGRO, BLANCO, VACIO;


        public static Color get(int index) {
            return index == 0 ? NEGRO : (index == 1 ? BLANCO : VACIO);
        }

        public char toChar() {
            return this.name().charAt(0);
        }

    }

    private int lado = 8;
    private char[][] tablero;


    public TableroReversi() {

        this.turno = 0; // comienzan negras

        this.tablero = new char[lado][lado];
        for (int i = 0; i < lado; i++) {
            Arrays.fill(this.tablero[i], Color.VACIO.toChar());
        }
    }

    @Override
    protected void mueve(Movimiento movimiento) throws ExcepcionJuego {


        if (this.esValido(movimiento) == false) return;

        MovimientoReversi m = (MovimientoReversi) movimiento;

        int c = 0;
        for (Coordenada p_ini : m.getInicio()) { // iteramos sobre cada inicio del movimiento

            int despl_i = 0;
            int despl_j = 0;

            despl_i = m.getDestino().getX() > p_ini.getX() ? 1 : (m.getDestino().getX() < p_ini.getX()) ? -1 : 0;
            despl_j = m.getDestino().getY() > p_ini.getY() ? 1 : (m.getDestino().getY() < p_ini.getY() ? -1 : 0);

            int i = (int) p_ini.getX();
            int j = (int) p_ini.getY();

            for (int p = 0; p < m.getPasos().get(c); p++) {
                i += despl_i;
                j += despl_j;
                this.tablero[i][j] = Color.get(this.turno).toChar();
            }

            c++;
        }

        // modificamos el estado del juego
        this.cambiaTurno();
        // TODO !!!
        // TODO COMPROBAR FIN PARTIDA
        // TODO !!!
        // this.estado es tablas, fin etc
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

                if (this.tablero[i][j] == Color.get(this.turno).toChar()) {

                    // para cada casilla, comprobamos sus ocho direcciones
                    DireccionMovimientosValidos(ret, i, j, 0, -1); // norte
                    DireccionMovimientosValidos(ret, i, j, 0, 1); // sur
                    DireccionMovimientosValidos(ret, i, j, -1, 0); // este
                    DireccionMovimientosValidos(ret, i, j, 1, 0); // oeste
                    DireccionMovimientosValidos(ret, i, j, -1, -1); // noreste
                    DireccionMovimientosValidos(ret, i, j, 1, -1); // noroeste
                    DireccionMovimientosValidos(ret, i, j, -1, 1); // sureste
                    DireccionMovimientosValidos(ret, i, j, 1, 1); //suroeste
                }
            }
        }


        return ret;
    }

    /**
     * @param part
     * @param i
     * @param j
     * @param dir_i
     * @param dir_j
     */
    private void DireccionMovimientosValidos(ArrayList<Movimiento> part, int i, int j, int dir_i, int dir_j) { //TODO limpiar

        if (part == null) return;


        int flag_rival = 0;
        int pasos = 0;

        for (int k = 1; k < this.lado; k++) {

            int pos_i = i + (k * dir_i);
            int pos_j = j + (k * dir_j);
            pasos++;

            if (pos_i > lado - 1 || pos_i < 0 || pos_j > lado - 1 || pos_j < 0)
                break; //out of bounds


            if (this.tablero[pos_i][pos_j] == Color.VACIO.toChar() && flag_rival == 1) {

// TODO comment

                int flag_mod = 0;
                for (Iterator<Movimiento> iter = part.iterator(); iter.hasNext(); ) {
                    MovimientoReversi mr = (MovimientoReversi) iter.next();

                    System.out.println(mr);

                    if (mr.getDestino().getX() == pos_i && mr.getDestino().getY() == pos_j) {
                        mr.addInicio(i, j, pasos);
                        flag_mod = 1;
                        break;
                    }
                }

                // agregamos el movimiento a la lista de validos
                if (flag_mod == 0) part.add(new MovimientoReversi(pos_i, pos_j, i, j, pasos));
                break;

            } else if (this.tablero[pos_i][pos_j] == Color.get((this.turno + 1) % numJugadores).toChar()) { // comprobamos si hay una casilla ocupada por el rival
                flag_rival = 1;

            } else if (this.tablero[pos_i][pos_j] == Color.get(this.turno).toChar()) {
                break;
            }
        }
    }

    @Override
    public String tableroToString() {

        String ret = "     A B C D E F G H \n     _ _ _ _ _ _ _ _\n             \n";

        for (int j = 0; j < lado; j++) {

            ret += " " + (j + 1) + " | ";

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
