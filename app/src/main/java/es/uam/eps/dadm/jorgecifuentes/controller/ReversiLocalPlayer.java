package es.uam.eps.dadm.jorgecifuentes.controller;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.MovimientoReversi;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

public class ReversiLocalPlayer implements ReversiView.OnPlayListener, Jugador {

    /*
    private final int ids[][] = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}};


    private int SIZE = 8;*/

    private Partida game;
    private ReversiView boardView;

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

        //TODO desactivar si no es turno o si acabada partida
        try {
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }

            TableroReversi t = (TableroReversi) game.getTablero();
            Movimiento m = t.getMovimientoValido(row, column);
            game.realizaAccion(new AccionMover(this, m));
        } catch (Exception e) {

        }
    }


}