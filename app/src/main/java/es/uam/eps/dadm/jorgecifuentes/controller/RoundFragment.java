package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
            this.round = RoundRepository.get(getActivity()).getRound(roundId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // inicializaciones graficas, va despues de oncreate
        final View rootView = inflater.inflate(R.layout.fragment_round, container, false);
        TextView roundTitleTextView = (TextView) rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());

        // FAB de reinicio de ronda
        final FloatingActionButton menuButton = (FloatingActionButton) rootView.findViewById(R.id.open_menu_fab);
        final FloatingActionButton resetButton =(FloatingActionButton) rootView.findViewById(R.id.reset_round_fab);
        final FloatingActionButton hintButton =(FloatingActionButton) rootView.findViewById(R.id.hint_fab);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(!isFABOpen){
                    showFABMenu(menuButton, resetButton, hintButton);
               // }else{
             //       closeFABMenu();
              //  }
            }
        });

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
                Snackbar.make(getView(), R.string.round_restarted, Snackbar.LENGTH_SHORT).show();
            }

        });

        //TODO listener hint menu

        return rootView;
    }

    private void showFABMenu(FloatingActionButton fab1, FloatingActionButton fab2, FloatingActionButton fab3) { // TODO lista de fab's
      //  isFABOpen=true;
        fab1.animate().translationY(-20); //TODO bucle
        fab2.animate().translationY(-100 - 100);
        fab3.animate().translationY(-380);
    }


    @Override
    public void onStart() {
        Log.d("reversi", "onStart: entrando"); //TODO quitar
        super.onStart();
        this.startRound();
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        //  updateUI();
    }
*/
    /*
        private void registerListeners(ReversiLocalPlayer local) {

            ImageButton button;
            for (int i = 0; i < SIZE; i++)
                for (int j = 0; j < SIZE; j++) {
                    button = (ImageButton) this.getView().findViewById(ids[i][j]);
                    button.setOnClickListener(local);
                }

            // listener del boton flotante
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
               //     updateUI();
                    Snackbar.make(getView(), R.string.round_restarted, Snackbar.LENGTH_SHORT).show();
                }

            });
        }
    */
    void startRound() {

        ArrayList<Jugador> players = new ArrayList<Jugador>();
        // JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ReversiLocalPlayer localPlayer = new ReversiLocalPlayer();
        ReversiLocalPlayer localPlayer2 = new ReversiLocalPlayer();

        //players.add(randomPlayer);
        players.add(localPlayer);
        players.add(localPlayer2);

        board = new TableroReversi();
        game = new Partida(board, players);

        game.addObservador(this); // observador: se encarga de pintar el tablero con OnCambioEnPartida
        localPlayer.setPartida(game);
        localPlayer2.setPartida(game);

        // custom view
        boardView = (ReversiView) getView().findViewById(R.id.board_reversiview);
        boardView.setBoard(round.getBoard().getSize(), round.getBoard());
        boardView.setOnPlayListener(localPlayer);


        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }


    // es esta quien pinta el tablero al ser listener
    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                this.boardView.setBoard(this.board);
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                this.boardView.setBoard(this.board);
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.game_over, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }


}