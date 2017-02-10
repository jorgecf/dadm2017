package es.uam.eps.dadm.jorgecifuentes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

/**
 * Clase que representa un tablero en el reversi.
 *
 * @author Jorge Cifuentes
 */
public class TableroReversi extends Tablero {


    /**
     * Enumeracion privada que representa el Color de cada posicion del tablero.
     */
    public enum Color {
        NEGRO, BLANCO, VACIO;

        /**
         * Obtiene el Color dado un indice.
         *
         * @param index indice del color, segun su posicion en el enum
         * @return Color especificado o vacio en caso de que no exista
         */
        public static Color get(int index) {
            return index == 0 ? NEGRO : (index == 1 ? BLANCO : VACIO);
        }

        /**
         * Obtiene EL color asociado a un caracter identificativo.
         *
         * @param c char inicial del nombre del Color
         * @return el Color encontrado o vacio si no existe
         */
        public static Color getFromChar(char c) {
            Optional<Color> opt = Arrays.stream(Color.values()).filter(c2 -> c2.toChar() == c).findFirst();
            if (opt.isPresent()) return opt.get();
            else return VACIO;
        }

        /**
         * Devuelve la representacion en caracter del color.
         *
         * @return primera letra del nombre del color
         */
        public char toChar() {
            return this.name().charAt(0);
        }

        /**
         * Comprueba si un color es valido en este tablero.
         *
         * @param c char del color a comprobar
         * @return true o false
         */
        public static boolean comprobarColor(char c) {
            return Arrays.stream(Color.values()).anyMatch(c2 -> c2.toChar() == c);
        }

        /**
         * Obtiene el color del reversi contrario al pasado (Blanco <-> Negro).
         *
         * @return el color obtenido
         */
        public Color colorContrario() { // solo ByN
            return this.values()[(this.ordinal() + 1) % 2];
        }
    }

    /*  cada lado del tablero */
    private int lado = 8;

    /* matriz que representa el tablero */
    private Color[][] tablero;

    /**
     * Constructor del TableroReversi. LLena el tablero de VACIO, menos las 4 casillas que vienen
     * predefinidias por las reglas del juego como BLANCO o NEGRO.
     */
    public TableroReversi() {

        this.turno = 0; // comienzan negras

        this.tablero = new Color[lado][lado];
        for (int i = 0; i < lado; i++) {
            Arrays.fill(this.tablero[i], Color.VACIO);
        }

        this.estado = EN_CURSO; // iniciamos la partida con el cambio de estado

        // casillas iniciales predefinidas del juego reversi
        this.tablero[3][3] = Color.BLANCO;
        this.tablero[3][4] = Color.NEGRO;
        this.tablero[4][3] = Color.NEGRO;
        this.tablero[4][4] = Color.BLANCO;

    }

    /**
     * A partir de tablero (matriz de Color) crea un Stream<Color> que contiene las 64 casillas.
     * Esto se hace con un stream de cada fila y luego un stream de dicho stream de cada fila, que
     * se aplana (flatten).
     *
     * @return el stream del tablero
     */
    private Stream<Color> flatStream() {
        return Arrays.stream(this.tablero).flatMap(fila -> Arrays.stream(fila));
    }

    @Override
    protected void mueve(Movimiento movimiento) throws ExcepcionJuego {

        if (this.esValido(movimiento) == true) {

            MovimientoReversi m = (MovimientoReversi) movimiento;

            int indexIni = 0;
            for (CoordenadaReversi p_ini : m.getInicio()) { // iteramos sobre cada inicio del movimiento

                int despl_i = 0;
                int despl_j = 0;

                // obtenemos el sentido del deplazamiento de cada eje (x,y) desde inicio hasta destino:
                //  1 si crece, -1 si decrece o 0 si no se mueve sobre ese eje
                despl_i = m.getDestino().getX() > p_ini.getX() ? 1 : (m.getDestino().getX() < p_ini.getX() ? -1 : 0);
                despl_j = m.getDestino().getY() > p_ini.getY() ? 1 : (m.getDestino().getY() < p_ini.getY() ? -1 : 0);

                int i = p_ini.getX();
                int j = p_ini.getY();

                // para el numero total de pasos, volteamos las fichas
                for (int p = 0; p < m.getPasos().get(indexIni); p++) {
                    i += despl_i;
                    j += despl_j;
                    this.tablero[i][j] = Color.get(this.turno);
                }

                indexIni++;
            }
        }

        // modificamos el estado del juego
        this.estado = comprobarEstado();
        this.ultimoMovimiento = movimiento;
    }

    /**
     * Comprueba el estado del juego y modifica el turno. Para que finalice el reversi ha de darse alguno de las siguientes casos:
     * 1) ningun jugador puede mover (aun con el tablero sin llenar)
     * 2) el tablero esta ya lleno
     *
     * @return En caso de terminar el juego, TABLAS o FINALIZADA, en caso contrario EN_JUEGO
     */
    private int comprobarEstado() {

        // si el tablero esta lleno, el juego ha acabado
        boolean flag_vacio = false;
        flag_vacio = this.flatStream().anyMatch(cas -> cas == Color.VACIO);

        // si ningun jugador puede mover, el juego ha acabado
        if (flag_vacio == false ||
                (HelperMovimientosValidos(Color.get(this.turno)).size() == 0
                        && HelperMovimientosValidos(Color.get((this.turno + 1) % this.numJugadores)).size() == 0)) {

            // comprobamos quien ha ganado
            int empate = (lado * lado) / 2;
            empate = (int) this.flatStream().filter(cas -> cas == Color.get(this.turno)).count() - empate;

            // si el juego ha acabado, el jugador que Partida da por ganador es el ultimo que ha jugado,
            //  por lo que solo se cambia el turno si ha ganado el rival
            if (empate < 0) this.cambiaTurno();

            if (empate == 0) return TABLAS;
            else return FINALIZADA;
        }

        // sigue el juego de manera normal
        this.cambiaTurno();
        return EN_CURSO;
    }

    @Override
    public boolean esValido(Movimiento movimiento) {
        return this.movimientosValidos().contains(movimiento);
    }


    /**
     * Devuelve el movimiento valido asociado a un destino (solo hay uno en reversi, que agrupa
     * todos los inicios)
     *
     * @param dest_x coordenada x del destino
     * @param dest_y coordenada y del destino
     * @return el movimiento valido o null
     */
    public Movimiento getMovimientoValido(int dest_x, int dest_y) {

        // buscamos un movimiento valido con igual destino al pasado por parametro
        Optional<Movimiento> mr = this.movimientosValidos().stream().filter(mov -> ((MovimientoReversi) mov).getDestino().equals(new CoordenadaReversi(dest_x, dest_y))).findFirst();

        if (mr.isPresent()) return mr.get();
        else return null;
    }

    @Override
    public ArrayList<Movimiento> movimientosValidos() {
        return HelperMovimientosValidos(Color.get(this.turno));
    }

    /**
     * Funcion privada de ayuda que obtiene los movimientos validos en una direccion desde una
     * casilla dada, y despues aplana el resultado.
     *
     * @param part  coleccion de movimientos parcial, debe ser valida
     * @param i     coordenada x de la casilla
     * @param j     coordenada y de la casilla
     * @param dir_i direccion de i: +-1 o 0 (crece, decrece, no se mueve)
     * @param dir_j direccion de j: +-1 o 0 (crece, decrece, no se mueve)
     * @param c     color de fichas sobre el que comprobar los movimientos validos
     */
    private void DireccionMovimientosValidos(ArrayList<Movimiento> part, int i, int j, int dir_i, int dir_j, Color c) {

        if (part == null) return;

        boolean flag_rival = false; // indica si nos hemos encontrado con una ficha rival
        int pasos = 0; // numero de pasos del inicio al destino

        for (int k = 1; k < this.lado; k++) {

            // nos movemos de uno en uno en la direccion dada
            int pos_i = i + (k * dir_i);
            int pos_j = j + (k * dir_j);
            pasos++;

            if (pos_i > lado - 1 || pos_i < 0 || pos_j > lado - 1 || pos_j < 0)
                break; // fuera de limites


            // si nos encontramos un vacio despues de encontrar una o varias fichas rivales,
            //  tenemos que parar y colocar ahi nuestra ficha
            if (this.tablero[pos_i][pos_j] == Color.VACIO && flag_rival == true) {


                // aplanamos la lista: si ret ya tiene un movimiento con nuestro destino, lo
                //  actualizamos poniendo el nuevo inicio y pasos
                boolean flag_mod = false;
                for (Iterator<Movimiento> iter = part.iterator(); iter.hasNext(); ) {

                    MovimientoReversi mr = (MovimientoReversi) iter.next();

                    if (mr.getDestino().getX() == pos_i && mr.getDestino().getY() == pos_j) {
                        mr.getInicio().add(new CoordenadaReversi(i, j));
                        mr.getPasos().add(pasos);
                        flag_mod = true;
                        break;
                    }
                }

                // si no existia uno antes, agregamos el movimiento a la lista de validos
                if (flag_mod == false)
                    part.add(new MovimientoReversi(pos_i, pos_j, i, j, pasos));
                break;

            }
            // comprobamos si hay una casilla ocupada por el rival
            else if (this.tablero[pos_i][pos_j] == c.colorContrario()) {
                flag_rival = true;

            }
            // si la ficha es del jugador que esta jugando o es vacio, y ademas no nos hemos encontrado una ficha del rival
            // (flag_rival es 0), salimos, por aqui no hay jugada posible
            else if (this.tablero[pos_i][pos_j] == c || this.tablero[pos_i][pos_j] == Color.VACIO) {
                break;
            }
        }
    }

    /**
     * Funcion privada de ayuda que obtiene los movimientos validos para un color de fichas concreto
     *
     * @param c color de las fichas del jugador
     * @return coleccion de sus movimietos validos
     */
    private ArrayList<Movimiento> HelperMovimientosValidos(Color c) {

        ArrayList<Movimiento> ret = new ArrayList<Movimiento>();

        // iteramos sobre todas las casillas
        for (int i = 0; i < lado; i++) {
            for (int j = 0; j < lado; j++) {

                if (this.tablero[i][j] == c) {

                    // para cada casilla, comprobamos sus ocho direcciones
                    this.DireccionMovimientosValidos(ret, i, j, 0, -1, c); // norte
                    this.DireccionMovimientosValidos(ret, i, j, 0, 1, c); // sur
                    this.DireccionMovimientosValidos(ret, i, j, -1, 0, c); // este
                    this.DireccionMovimientosValidos(ret, i, j, 1, 0, c); // oeste
                    this.DireccionMovimientosValidos(ret, i, j, -1, -1, c); // noreste
                    this.DireccionMovimientosValidos(ret, i, j, 1, -1, c); // noroeste
                    this.DireccionMovimientosValidos(ret, i, j, -1, 1, c); // sureste
                    this.DireccionMovimientosValidos(ret, i, j, 1, 1, c); //suroeste
                }
            }
        }

        return ret;
    }

    @Override
    public String tableroToString() {

        String ret = ""; // representacion interna

        for (int i = 0; i < lado; i++) {
            for (int j = 0; j < lado; j++) {
                ret += this.tablero[j][i].toChar();
            }
        }

        ret += this.turno;
        ret += this.numJugadas;
        return ret;
    }

    @Override
    public void stringToTablero(String s) throws ExcepcionJuego {

        if (s.length() < lado * lado + 2 || s.length() > lado * lado + 3)
            throw new ExcepcionJuego("cadena de entrada de longitud incorrecta");


        for (int i = 0; i < lado; i++) {
            for (int j = 0; j < lado; j++) {

                char c = s.charAt(i + j + ((lado - 1) * i));

                // comprobamos que se introduce un valor correcto
                if (Color.comprobarColor(c) == false)
                    throw new ExcepcionJuego("cadena de entrada mal formada");

                this.tablero[j][i] = Color.getFromChar(c);
            }
        }

        // obtenemos los atributos de la partida
        String atrib = s.substring(lado * lado);

        this.turno = Character.getNumericValue(atrib.charAt(0)); // un caracter
        this.numJugadas = Integer.parseInt(atrib.substring(1)); // uno o dos caracteres
    }

    @Override
    public String toString() {
        String ret = "     A B C D E F G H \n     _ _ _ _ _ _ _ _\n             \n";

        for (int j = 0; j < lado; j++) {

            ret += " " + (j + 1) + " | ";

            for (int i = 0; i < lado; i++) {
                ret += this.tablero[i][j].toChar() + " ";
            }
            ret += "\n";
        }

        ret += "Turno de: " + Color.get(this.turno).toString();
        ret += " - numero de jugadas: " + this.getNumJugadas();
        ret += "\n";
        return ret;
    }
}