package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.activities.RoundPreferenceActivity;
import es.uam.eps.dadm.jorgecifuentes.fcm.MessageFields;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;
import es.uam.eps.dadm.jorgecifuentes.views.EmptyRecyclerView;
import es.uam.eps.dadm.jorgecifuentes.views.ReversiView;
import es.uam.eps.multij.ExcepcionJuego;

/**
 * Clase que representa un  fragmento que incluye la lista de rondas.
 *
 * @author Jorge Cifuentes
 */
public class RoundListFragment extends Fragment {

    private EmptyRecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;
    private Callbacks callbacks;

    // Receptor de broadcasts locales
    private MessageReceiver receiver;


    public interface Callbacks {
        void onRoundSelected(Round round);

        void onPreferencesSelected();

        void onHelpSelected();

        void onScoresSelected();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.receiver = new MessageReceiver();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflamos la vista con el layout en xml.
        final View view = inflater.inflate(R.layout.fragment_round_list, container, false);
        this.roundRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.round_recycler_view);

        // Vista recicladora.
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        this.roundRecyclerView.setLayoutManager(linearLayoutManager);
        this.roundRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.roundRecyclerView.setEmptyView(view.findViewById(R.id.rounds_empty_view));


        // Inicializamos el listener de la CardView.
        this.roundRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {

                // Si la partida esta iniciada, clickar nuevamente sobre su CardView no la reiniciara.
                //  Para eso esta el FAB.
                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                RoundRepository.RoundsCallback<Round> roundsCallback = new RoundRepository.RoundsCallback<Round>() {

                    @Override
                    public void onResponse(List<Round> rounds) {
                        roundAdapter.setCurrent(position);

                        if (position == -1)
                            callbacks.onRoundSelected(rounds.get(1));
                        else
                            callbacks.onRoundSelected(rounds.get(position));
                    }

                    @Override
                    public void onError(String error) {
                        Snackbar.make(getView(), R.string.error_reading_rounds, Snackbar.LENGTH_LONG).show();
                    }
                };
                String playeruuid = RoundPreferenceActivity.getPlayerUUID(getActivity());
                repository.getRounds(playeruuid, null, null, roundsCallback);
            }
        }));

        // Inicializamos el listener para hacer swipe sobre los elementos de la lista de CardView.
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // Este metodo se llama antes de empezar la animacion del Swipe. Si la partida es la
                //  actual, logicamente no se puede quitar de la lista, asi que se bloquea la
                //  accion (no se puede mover hacia ningun lado).
                if (roundAdapter.isRemovable(viewHolder.getAdapterPosition()) == false) {
                    return 0;
                }

                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Round round = roundAdapter.remove(viewHolder.getAdapterPosition());
                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                repository.removeRound(round, null);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(roundRecyclerView);


        // Establecemos la visibilidad del options menu.
        this.setHasOptionsMenu(true);

        this.updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getContext());
        IntentFilter filter = new IntentFilter(MessageFields.ACTION_MESSAGE);
        manager.registerReceiver(this.receiver, filter);

        this.updateUI();

        // Al cargar la vista de nuevo (solo en movil, en tablet nunca se oculta), reseteamos el
        //  actual.
        if (this.roundAdapter != null)
            this.roundAdapter.setCurrent(-1);
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getContext());
        manager.unregisterReceiver(this.receiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_new_round:

                Round round = new Round(RoundPreferenceActivity.getPlayerUUID(getActivity()), RoundPreferenceActivity.getPlayerName(getContext()));

                RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
                    @Override
                    public void onResponse(boolean ok) {
                        updateUI();
                    }
                };

                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                repository.addRound(round, callback);
                return true;
            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;
            case R.id.menu_help_settings:
                callbacks.onHelpSelected();
                return true;
            case R.id.menu_item_scoreboard:
                callbacks.onScoresSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Actualiza la lista de rondas graficas.
     */
    public void updateUI() {

        // Creamos el callback.
        RoundRepository.RoundsCallback<Round> roundsCallback = new RoundRepository.RoundsCallback<Round>() {
            @Override
            public void onResponse(List<Round> rounds) {
                if (roundAdapter == null) {
                    roundAdapter = new RoundAdapter(rounds);
                    roundRecyclerView.setAdapter(roundAdapter);
                } else {
                    roundAdapter.setRounds(rounds);
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        // Obtenemos las rondas.
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        repository.getRounds(RoundPreferenceActivity.getPlayerUUID(getActivity()), null, null, roundsCallback);
        repository.close();
    }


    /**
     * Esta clase se define como el adaptador de la vista recicladora.
     */
    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder> {

        private List<Round> rounds;
        private int current;


        /**
         * Esta clase se define como el elemento contenedor de la ronda.
         */
        public class RoundHolder extends RecyclerView.ViewHolder {

            private TextView idTextView;
            private TextView boardTextView;
            private TextView dateTextView;

            public RoundHolder(View itemView) {
                super(itemView);

                this.idTextView = (TextView) itemView.findViewById(R.id.list_item_title);
                this.boardTextView = (TextView) itemView.findViewById(R.id.list_item_white);
                this.dateTextView = (TextView) itemView.findViewById(R.id.list_item_black);
            }

            public void bindRound(Round round) {
                this.idTextView.setText(getText(R.string.round) + round.getTitle());
                this.boardTextView.setText(round.getBoard().toSimpleString());
                this.dateTextView.setText(String.valueOf(round.getDate()).substring(0, 19));
            }
        }


        public RoundAdapter(List<Round> rounds) {
            this.rounds = rounds;
            this.current = -1;
        }

        @Override
        public RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) { // llamada al crear el Holder
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_round_item, parent, false);

            return new RoundHolder(view);
        }

        @Override
        public void onBindViewHolder(RoundAdapter.RoundHolder holder, int position) { // llamada al asignar un Holder creado a una ronda
            Round round = this.rounds.get(position);
            holder.bindRound(round);
        }

        public Round remove(int position) {
            Round r = this.rounds.get(position);

            this.rounds.remove(position);
            this.notifyItemRemoved(position);

            return r;
        }

        public void setRounds(List<Round> r) {
            this.rounds.clear();
            this.rounds = r;
            roundAdapter.notifyDataSetChanged();
        }

        /**
         * Actualiza el tablero de una ronda.
         *
         * @param roundUUID id de la ronda a actualizar.
         * @param newBoard  nuevo tablero.
         */
        public void updateRound(String roundUUID, String newBoard) {
            for (Round r : this.rounds) {
                if (r.getRoundUUID().equals(roundUUID)) {
                    try {
                        r.getBoard().stringToTablero(newBoard);
                    } catch (ExcepcionJuego excepcionJuego) {
                        excepcionJuego.printStackTrace();
                    }

                    final ReversiView b = (ReversiView) getActivity().findViewById(R.id.board_reversiview);
                    b.setBoard(r.getBoard());
                    b.invalidate();
                    return;
                }
            }
        }


        public boolean isRemovable(int position) {
            return current != position;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        @Override
        public int getItemCount() {
            return this.rounds.size();
        }

    }

    public class MessageReceiver extends BroadcastReceiver {

        private Context context;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;

            // Informacion del intent
            Bundle extras = intent.getExtras();
            String action = intent.getAction();

            // Obtenemos los datos que nos ha dado nuestro MessagingService
            if (action.equals(MessageFields.ACTION_MESSAGE)) {

                // Actualizamos el tablero requerido
                roundAdapter.updateRound(extras.getString(MessageFields.SENDER), extras.getString(MessageFields.MESSAGE));
            }
        }
    }
}