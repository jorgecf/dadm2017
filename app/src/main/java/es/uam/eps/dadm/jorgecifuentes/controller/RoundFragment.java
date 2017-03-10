package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * Clase que representa un  fragmento que incluye la ronda: tablero y demas funcionalidad.
 *
 * @author Jorge Cifuentes
 */
public class RoundFragment extends Fragment implements PartidaListener {

    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.round_id";
    public static final String BOARDSTRING = "es.uam.eps.dadm.boardstring";


    private Round round;
    private Partida game;

    private ReversiView boardView;

    private Callbacks callbacks;

    /**
     * Funcion que define que hacer al actualizar el contenido de una ronda.
     */
    public interface Callbacks {
        void onRoundUpdated(Round round);
    }


    public RoundFragment() {

    }

    /**
     * Devuelve una nueva instancia de este fragmento.
     *
     * @param roundId id de la ronda contenida
     * @return la nueva instancia
     */
    public static RoundFragment newInstance(String roundId) {

        // Almacenamos el id de la ronda en el Bundle.
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

        // Recuperamos la ronda guardada, si existe.
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
        JugadorAleatorio randomPlayer = new JugadorAleatorio(this.getContext().getString(R.string.random_player_default_name));
        ReversiLocalPlayer localPlayer = new ReversiLocalPlayer(this.getContext());

        players.add(localPlayer);
        players.add(randomPlayer);

        game = new Partida(this.round.getBoard(), players);

        game.addObservador(this);
        localPlayer.setPartida(game);

        // Vista del Tablero.
        boardView = (ReversiView) this.getView().findViewById(R.id.board_reversiview);
        boardView.setBoard(round.getBoard());
        boardView.setOnPlayListener(localPlayer);


        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    @Override
    public void onCambioEnPartida(Evento evento) { // esta funcion se encarga de pintar el tablero

        // Actualizamos el marcador.
        TextView black_score = (TextView) getView().findViewById(R.id.black_score);
        TextView white_score = (TextView) getView().findViewById(R.id.white_score);

        black_score.setText(round.getBoard().getFichas(TableroReversi.Color.NEGRO) + " " + game.getJugador(0).getNombre());
        white_score.setText(game.getJugador(1).getNombre() + " " + round.getBoard().getFichas(TableroReversi.Color.BLANCO));

        if (round.getBoard().getTurno() == 0) {
            white_score.setTypeface(null, Typeface.NORMAL);
            black_score.setTypeface(null, Typeface.BOLD);
        } else {
            white_score.setTypeface(null, Typeface.BOLD);
            black_score.setTypeface(null, Typeface.NORMAL);
        }

        // Repintamos el tablero.
        this.boardView.setBoard(this.round.getBoard());
        boardView.invalidate();
        callbacks.onRoundUpdated(round);

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                break;
            case Evento.EVENTO_FIN:
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(), "ALERT DIALOG");
                break;
        }
    }
}