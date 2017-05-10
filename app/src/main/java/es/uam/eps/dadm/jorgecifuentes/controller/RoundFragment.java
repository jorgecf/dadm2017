package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.activities.RoundPreferenceActivity;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;
import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.dadm.jorgecifuentes.server.ServerInterface;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
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

    public static final String ARG_FIRST_PLAYER_NAME = "es.uam.eps.dadm.first_player_name";
    public static final String ARG_ROUND_TITLE = "es.uam.eps.dadm.round_title";
    public static final String ARG_ROUND_DATE = "es.uam.eps.dadm.round_date";
    public static final String ARG_ROUND_BOARD = "es.uam.eps.dadm.round_board";
    public static final String ARG_ROUND_UUID = "es.uam.eps.dadm.rounduuid";
    public static final String ARG_RIVAL_UUID = "es.uam.eps.dadm.rivaluuid";

    public static final String BOARDSTRING = "es.uam.eps.dadm.boardstring";


    private Round round;
    private Partida game;

    private ReversiView boardView;

    private Callbacks callbacks;

    private String firstPlayerName;
    private String roundTitle;
    private String roundDate;
    private String roundBoard;
    private String rounduuid;
    private String rivaluuid;

    /**
     * Interfaz que define que hacer al actualizar el contenido de una ronda.
     */
    public interface Callbacks {
        void onRoundUpdated(Round round);
    }


    public RoundFragment() {

    }

    /**
     * Devuelve una nueva instancia de este fragmento.
     *
     * @param firstPlayerName
     * @param roundTitle
     * @param roundDate
     * @param roundBoard
     * @return la nueva instancia
     */
    public static RoundFragment newInstance(String rounduuid, String firstPlayerName, String roundTitle, String roundDate, String roundBoard, String rivaluuid) {

        // Almacenamos los datos de la ronda en el Bundle.
        Bundle args = new Bundle();

        args.putString(ARG_FIRST_PLAYER_NAME, firstPlayerName);
        args.putString(ARG_ROUND_TITLE, roundTitle);
        args.putString(ARG_ROUND_DATE, roundDate);
        args.putString(ARG_ROUND_BOARD, roundBoard);
        args.putString(ARG_RIVAL_UUID, rivaluuid);

        args.putString(ARG_ROUND_UUID, rounduuid);

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
        if (getArguments().containsKey(ARG_FIRST_PLAYER_NAME)) {
            firstPlayerName = getArguments().getString(ARG_FIRST_PLAYER_NAME);
        }

        if (getArguments().containsKey(ARG_ROUND_TITLE)) {
            roundTitle = getArguments().getString(ARG_ROUND_TITLE);
        }

        if (getArguments().containsKey(ARG_ROUND_DATE)) {
            roundDate = getArguments().getString(ARG_ROUND_DATE);
        }

        if (getArguments().containsKey(ARG_ROUND_BOARD)) {
            roundBoard = getArguments().getString(ARG_ROUND_BOARD);
        }

        if (getArguments().containsKey(ARG_ROUND_UUID)) {
            rounduuid = getArguments().getString(ARG_ROUND_UUID);
        }

        if (getArguments().containsKey(ARG_RIVAL_UUID)) {
            rivaluuid = getArguments().getString(ARG_RIVAL_UUID);
        }

        // Tablero guardado en la rotacion.
        String boardString = roundBoard;
        if (savedInstanceState != null) {
            boardString = savedInstanceState.getString(BOARDSTRING);
        }


        // Cargamos la ronda contenida en este fragmento.
        this.round = new Round(firstPlayerName, roundTitle, roundDate, RoundPreferenceActivity.getPlayerUUID(getActivity()), rivaluuid, rounduuid);

        // Cargamos el tablero.
        this.round.setBoard(new TableroReversi());
        try {
            this.round.getBoard().stringToTablero(boardString);
        } catch (ExcepcionJuego e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // inicializaciones graficas, va despues de onCreate

        final View rootView = inflater.inflate(R.layout.fragment_round, container, false);

        TextView roundTitleTextView = (TextView) rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(getText(R.string.round) + round.getTitle());

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

        ArrayList<Jugador> players = new ArrayList<>();

        JugadorAleatorio randomPlayer = new JugadorAleatorio(this.getContext().getString(R.string.random_player_default_name));
        ReversiLocalPlayer localPlayer = new ReversiLocalPlayer(this.getContext(), firstPlayerName);

        ReversiLocalServerPlayer localServerPlayer = new ReversiLocalServerPlayer(this.getContext(), this.round.getRoundUUID());
        ReversiRemotePlayer remote = new ReversiRemotePlayer(this.getContext(), this.round.getPlayerUUID(), this.round.getRoundUUID());


        if (RoundPreferenceActivity.getPlayOnline(this.getContext()) == false) {
            // Partida local
            players.add(localPlayer);
            players.add(randomPlayer);
        } else {
            // Partida online
            if (RoundPreferenceActivity.getPlayerName(this.getContext()).equals(this.round.getPlayername())) {

                localServerPlayer.setPlayername(this.round.getPlayername()); // Yo
                remote.setPlayername(this.round.getRivalUUID());

                players.add(localServerPlayer);
                players.add(remote);
            } else {

                localServerPlayer.setPlayername(this.round.getPlayername());
                remote.setPlayername(RoundPreferenceActivity.getPlayerName(this.getContext())); // Yo

                players.add(remote);
                players.add(localServerPlayer);

                // Agregamos al jugador a la partida creada por otro, si es la primera vez que entra
                if (this.round.getRivalUUID().equals("")) {
                    ServerInterface.getServer(this.getContext()).addPlayerToRound(Integer.parseInt(this.round.getRoundUUID()), this.round.getPlayerUUID(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("debug", "onResponse: add new player to round: " + s + ", " + round.getTitle());
                            round.setRivalUUID(RoundPreferenceActivity.getPlayerUUID(getContext()));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //TODO
                        }
                    });
                }
            }
        }


        game = new Partida(this.round.getBoard(), players);

        game.addObservador(this);

        // Vista del Tablero.
        boardView = (ReversiView) this.getView().findViewById(R.id.board_reversiview);
        boardView.setBoard(round.getBoard());

        if (players.contains(localPlayer)) {
            boardView.setOnPlayListener(localPlayer);
            localPlayer.setPartida(game);
        } else {
            boardView.setOnPlayListener(localServerPlayer);
            localServerPlayer.setPartida(game);
        }


        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    private void updateRound() {

        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {

            @Override
            public void onResponse(boolean response) {
                if (response == false)
                    Snackbar.make(getView(), R.string.error_updating_round, Snackbar.LENGTH_LONG).show();
            }
        };

        repository.updateRound(round, callback);
        repository.close();
    }

    @Override
    public void onCambioEnPartida(Evento evento) { // esta funcion se encarga de pintar el tablero

        // Actualizamos el marcador.
        View v = this.getView();

        if (v != null) {
            TextView black_score = (TextView) v.findViewById(R.id.black_score);
            TextView white_score = (TextView) v.findViewById(R.id.white_score);

            black_score.setText(round.getBoard().getFichas(TableroReversi.Color.NEGRO) + " " + game.getJugador(0).getNombre());
            white_score.setText(game.getJugador(1).getNombre() + " " + round.getBoard().getFichas(TableroReversi.Color.BLANCO));

            if (round.getBoard().getTurno() == 0) {
                white_score.setTypeface(null, Typeface.NORMAL);
                black_score.setTypeface(null, Typeface.BOLD);
            } else {
                white_score.setTypeface(null, Typeface.BOLD);
                black_score.setTypeface(null, Typeface.NORMAL);
            }
        }

        // Actualizamos los datos en la base de datos.
        this.updateRound();

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
            case Evento.EVENTO_ERROR:
                Snackbar.make(getView(), "error onPlay", Snackbar.LENGTH_SHORT).show();
                this.game.continuar();
                break;
        }
    }
}