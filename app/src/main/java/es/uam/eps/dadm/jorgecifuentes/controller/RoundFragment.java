package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
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
    public static final String BOARDSTRING = "es.uam.eps.dadm.boardstring";


    private Round round;
    private Partida game;

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
        FloatingActionButton resetButton = (FloatingActionButton) rootView.findViewById(R.id.reset_round_fab);
        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (round.getBoard().getEstado() != Tablero.EN_CURSO) {
                    Snackbar.make(getView(), R.string.round_already_finished, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                round.setBoard(new TableroReversi()); // nuevo tablero
                startRound();
                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.round_restarted, Snackbar.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // Instanciamos el tablero. Si no hay informacion previa en el Bundle, startRound, iniciara
        //  con este board recien creado (nueva partida).
        this.round.setBoard(new TableroReversi());

        if (savedInstanceState != null) {
            try {
                // En el caso de que hubiese informacion previa, se agrega, por lo que startRound
                //  iniciara la partida donde se dejo antes de destruirla vista (onDestroyView).
                this.round.getBoard().stringToTablero(savedInstanceState.getString(BOARDSTRING));
            } catch (ExcepcionJuego excepcionJuego) {
                excepcionJuego.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Llamado despues de onActivityCreated, iniciamos la ronda.
        this.startRound();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Guardamos el tablero. Para este juego en concreto es lo unico necesario, ya que tanto como
        //  jugadores como la propia partida pueden reinicializarse de nuevo sin perder datos, solo
        //  con la informacion del estado del tablero.
        outState.putString(BOARDSTRING, this.round.getBoard().tableroToString());

        super.onSaveInstanceState(outState);
    }

    void startRound() {

        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ReversiLocalPlayer localPlayer = new ReversiLocalPlayer();
        //ReversiLocalPlayer localPlayer2 = new ReversiLocalPlayer();

        players.add(randomPlayer);
        players.add(localPlayer);
        //players.add(localPlayer2);

        // Si el tablero es nulo se crea. Solo sera nulo en la primera llamada, con la rotacion se
        //  restablecera en onActivityCreated para continuar desde el mismo punto.
        game = new Partida(this.round.getBoard(), players);

        game.addObservador(this);
        localPlayer.setPartida(game);
        // localPlayer2.setPartida(game);


        // Vista del Tablero.
        boardView = (ReversiView) this.getView().findViewById(R.id.board_reversiview);
        boardView.setBoard(round.getBoard().getSize(), round.getBoard());
        boardView.setOnPlayListener(localPlayer);


        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }


    // es esta quien pinta el tablero al ser listener
    @Override
    public void onCambioEnPartida(Evento evento) {

       // TextView scoreTitleTextView = (TextView) this.getView().findViewById(R.id.score_title);
       // scoreTitleTextView.setText(this.round.getState());

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                this.boardView.setBoard(this.round.getBoard());
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                this.boardView.setBoard(this.round.getBoard());
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.game_over, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }


}