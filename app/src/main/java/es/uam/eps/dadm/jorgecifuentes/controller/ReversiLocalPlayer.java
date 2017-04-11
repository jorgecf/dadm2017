package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.util.Log;

import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;


/**
 * Clase que representa un jugador local de Reversi.
 *
 * @author Jorge Cifuentes
 */
public class ReversiLocalPlayer implements ReversiView.OnPlayListener, Jugador {

    private Partida game;
    private Context context;
    private String name;

    public ReversiLocalPlayer() {

    }

    public ReversiLocalPlayer(Context c, String newName) {
        this.context = c;
        this.name = newName;
    }

    public void setPartida(Partida game) {
        this.game = game;
    }

    @Override
    public String getNombre() {
        return this.name;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

    }


    @Override
    public void onPlay(int row, int column) {

        try {
            TableroReversi t = (TableroReversi) game.getTablero();
            Movimiento m = t.getMovimientoValido(row, column);

            // Si SI hay movimientos validos y este no es uno de ellos, no hacemos nada,
            //  el turno sigue siendo de este jugador.
            if (m == null && t.movimientosValidos().size() > 0) {
                Log.d("reversi", "onPlay: movimiento no valido");
            } else {
                game.realizaAccion(new AccionMover(this, m));
            }
        } catch (Exception e) {

            /**
             * @TODO darg aquí cabría notificar a la vista y mostrar desde allí un mensaje vía Snackbar. Basta crear un evento EVENTO_ERROR, enviarlo usando onCambioEnPartida() y capturarlo en RoundFragment(). Allí, se genera el mensaje con el Snackbar y luego se llama al método continuar() de Partida.
             */
        }
    }
}