package es.uam.eps.dadm.jorgecifuentes.controller;

import android.support.design.widget.Snackbar;
import android.util.Log;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

public class ReversiLocalPlayer implements ReversiView.OnPlayListener, Jugador {

    private Partida game;

    public ReversiLocalPlayer() {

    }

    public void setPartida(Partida game) {
        this.game = game;
    }


    @Override
    public String getNombre() {
        return "Local player";
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

        }
    }
}