package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * Created by jorgecf on 27/02/17.
 */

public class RoundFragment extends Fragment implements PartidaListener {

    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.round_id";

    private final int ids[][] = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}};

    private int SIZE = 8;
    private int size;
    private Round round;
    private Partida game;
    private TableroReversi board;

    private ReversiView boardView;

    private Callbacks callbacks;

    public interface Callbacks {
        void onRoundUpdated(Round round);
    }


    public RoundFragment() {
    }

    public static RoundFragment newInstance(String roundId) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);

        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);

        return roundFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ROUND_ID)) {
            String roundId = getArguments().getString(ARG_ROUND_ID);
            round = RoundRepository.get(getActivity()).getRound(roundId);
            //size = round.getSize();
        }

        //  startRound();

    }


    @Override
    public void onStart() {
        super.onStart();
        startRound();
    }

    /*
            @Override
            public void onResume() {
                super.onResume();
                updateUI();
            }
            */
    private void registerListeners() {

        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) this.getView().findViewById(ids[i][j]);
                //    button.setOnClickListener(local);
            }

        // listener del boton flotante
        /* TODO ponerlo desde java
        FloatingActionButton resetButton = (FloatingActionButton) getView().findViewById(R.id.reset_round_fab);

        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (round.getBoard().getEstado() != Tablero.EN_CURSO) {
                    Snackbar.make(getView(), R.string.round_already_finished, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                round.getBoard().reset();
                startRound();
                callbacks.onRoundUpdated(round);
                updateUI();
                Snackbar.make(getView(), R.string.round_restarted, Snackbar.LENGTH_SHORT).show();
            }

        });

    }

*/

    }

    void startRound() {

        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ReversiLocalPlayer localPlayer = new ReversiLocalPlayer();

        players.add(randomPlayer);
        players.add(localPlayer);

        game = new Partida(round.getBoard(), players);
        game.addObservador(this);

        localPlayer.setPartida(game);

        boardView = (ReversiView) getView().findViewById(R.id.board_reversiview);
        boardView.setBoard(size, round.getBoard());
        boardView.setOnPlayListener(localPlayer);
        registerListeners();


        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();

    }


    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.game_over, Snackbar.LENGTH_SHORT).show();
                break;

        }
    }

}