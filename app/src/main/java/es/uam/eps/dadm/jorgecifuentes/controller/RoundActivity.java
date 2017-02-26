package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 26/02/17.
 */

public class RoundActivity extends AppCompatActivity implements PartidaListener {

    private final int ids[][] = {
            {R.id.er1, R.id.er2, R.id.er3},
            {R.id.er4, R.id.er5, R.id.er6},
            {R.id.er7, R.id.er8, R.id.er9}};


    private int SIZE = 3;
    // private int size;
    private Partida game;
    private TableroReversi board;
    private Round round;


    public static final String BOARDSTRING = "es.uam.eps.dadm.board";
    public static final String EXTRA_ROUND_ID = "es.uam.eps.dadm.round_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        startRound();

        String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
        round = RoundRepository.get(this).getRound(roundId);
        // size = round.getSize();
        TextView roundTitleTextView = (TextView) findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BOARDSTRING, board.tableroToString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        try {
            board.stringToTablero(savedInstanceState.getString(BOARDSTRING));
            updateUI();
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
    }

    /**
     *
     */
    private void registerListeners(ReversiLocalPlayer local) {
        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                button.setOnClickListener(local);
            }
    }

    public static Intent newIntent(Context packageContext, String roundId) {
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        return intent;
    }


    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ReversiLocalPlayer localPlayer = new ReversiLocalPlayer();

        players.add(randomPlayer);
        players.add(localPlayer);

        board = new TableroReversi();
        game = new Partida(board, players);
        game.addObservador(this);

        localPlayer.setPartida(game);
        registerListeners(localPlayer);

        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    private void updateUI() {

        ImageButton button;

        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                if (board.getTablero(i, j) == TableroReversi.Color.BLANCO) {
                    button.setBackgroundResource(R.drawable.white_button_48dp);
                } else if (board.getTablero(i, j) == TableroReversi.Color.NEGRO) {
                    button.setBackgroundResource(R.drawable.black_button_48dp);
                } else
                    button.setBackgroundResource(R.drawable.void_button_48dp);
            }
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                updateUI();
                break;
            case Evento.EVENTO_FIN:
                updateUI();
                Snackbar.make(findViewById(R.id.round_title), R.string.game_over, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

}