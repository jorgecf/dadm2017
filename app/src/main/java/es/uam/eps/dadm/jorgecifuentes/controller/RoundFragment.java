package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final View rootView = inflater.inflate(R.layout.fragment_round, container, false);

        // cargamos la vista
        View rootView = this.makeBoard();

        TextView roundTitleTextView = (TextView) rootView.findViewById(0);
        roundTitleTextView.setText(round.getTitle());

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        startRound();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void createButtonCenter(RelativeLayout parent, int id) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        ReversiButton button = new ReversiButton(getActivity());

        button.setId(id);
        button.setBackgroundResource(R.drawable.void_button_48dp);
        button.setLayoutParams(params);

        parent.addView(button);
    }

    private void createButtonBelow(RelativeLayout parent, int below, int id) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, below);
        params.addRule(RelativeLayout.ALIGN_LEFT, below);

        ReversiButton button = new ReversiButton(getActivity());
        button.setId(id);
        button.setBackgroundResource(R.drawable.void_button_48dp);
        button.setLayoutParams(params);

        parent.addView(button);
    }

    private void createButtonLeftOf(RelativeLayout parent, int leftOf, int id) { //TODO simplificar estas 4 funciones
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.LEFT_OF, leftOf);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, leftOf);
        ReversiButton button = new ReversiButton(getActivity());
        button.setId(id);
        button.setBackgroundResource(R.drawable.void_button_48dp);
        button.setLayoutParams(params);
        parent.addView(button);
    }

    private void createButtonRightOf(RelativeLayout parent, int rightOf, int id) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, rightOf);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, rightOf);
        ReversiButton button = new ReversiButton(getActivity());
        button.setId(id);
        button.setBackgroundResource(R.drawable.void_button_48dp);
        button.setLayoutParams(params);
        parent.addView(button);
    }

    private View makeBoard() {

        RelativeLayout.LayoutParams paramsMatch = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(paramsMatch);
        createTitleTextView(root, 0);
        createButtonCenter(root, 2);
        createButtonLeftOf(root, 2, 1);
        createButtonRightOf(root, 2, 3);
        createButtonBelow(root, 2, 5);
        createButtonLeftOf(root, 5, 4);
        createButtonRightOf(root, 5, 6);
        createButtonBelow(root, 5, 8);
        createButtonLeftOf(root, 8, 7);
        createButtonRightOf(root, 8, 9);

        return root;
    }

    private void createTitleTextView(RelativeLayout parent, int id) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView textView = new TextView(getActivity());
        textView.setId(id);
        textView.setTextSize(35);
        textView.setPadding(0, 40, 0, 40);
        textView.setLayoutParams(params);

        parent.addView(textView);
    }

    private void registerListeners(ReversiLocalPlayer local) {

        ReversiButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ReversiButton) this.getView().findViewById(ids[i][j]);
                button.setOnClickListener(local);
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
        */
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

        ReversiButton button;

        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ReversiButton) this.getView().findViewById(ids[i][j]);
                if (board.getTablero(i, j) == TableroReversi.Color.BLANCO) {
                    //button.setBackgroundResource(R.drawable.white_button_48dp);
                    button.randomPlayer();
                } else if (board.getTablero(i, j) == TableroReversi.Color.NEGRO) {
                    //button.setBackgroundResource(R.drawable.black_button_48dp);
                    button.human();
                } else
                    //button.setBackgroundResource(R.drawable.void_button_48dp);
                    button.notClicked();
            }

    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                updateUI();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                updateUI();
                callbacks.onRoundUpdated(round);
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(), "ALERT DIALOG");
                break;
        }
    }

}