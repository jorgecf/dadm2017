package es.uam.eps.dadm.jorgecifuentes.controller;

import android.support.design.widget.Snackbar;
import android.view.View;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.MovimientoReversi;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

public class ReversiLocalPlayer implements View.OnClickListener, Jugador {
    private final int ids[][] = {
            {R.id.er1, R.id.er2, R.id.er3},
            {R.id.er4, R.id.er5, R.id.er6},
            {R.id.er7, R.id.er8, R.id.er9}};

    private int SIZE = 3;
    Partida game;

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

    private int fromViewToI(View view) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (view.getId() == ids[i][j])
                    return i;
        return -1;
    }

    private int fromViewToJ(View view) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (view.getId() == ids[i][j])
                    return j;
        return -1;
    }

    @Override
    public void onClick(View v) {
        try {

            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                Snackbar.make(v, R.string.round_already_finished, Snackbar.LENGTH_SHORT).show();
                return;
            }

            TableroReversi t = (TableroReversi) game.getTablero();
            Movimiento m = t.getMovimientoValido(fromViewToI(v), fromViewToJ(v));
            game.realizaAccion(new AccionMover(this, m));
        } catch (Exception e) {
            Snackbar.make(v, R.string.invalid_movement, Snackbar.LENGTH_SHORT).show();
        }
    }

}