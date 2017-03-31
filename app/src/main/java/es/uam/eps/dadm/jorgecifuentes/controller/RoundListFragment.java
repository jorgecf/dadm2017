package es.uam.eps.dadm.jorgecifuentes.controller;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;
import es.uam.eps.dadm.jorgecifuentes.views.EmptyRecyclerView;

/**
 * Clase que representa un  fragmento que incluye la lista de rondas.
 *
 * @author Jorge Cifuentes
 */
public class RoundListFragment extends Fragment {

    // private RecyclerView roundRecyclerView;
    private EmptyRecyclerView roundRecyclerView;

    private RoundAdapter roundAdapter;

    private Callbacks callbacks;

    public interface Callbacks {
        void onRoundSelected(Round round);

        void onPreferencesSelected();

        void onHelpSelected();

        void onScoresSelected();
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
        roundRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.round_recycler_view);

        // Vista recicladora.
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());
        roundRecyclerView.setEmptyView(view.findViewById(R.id.rounds_empty_view));


        // Inicializamos el listener de la CardView.
        roundRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {

                // Si la partida esta iniciada, clickar nuevamente sobre su CardView no la reiniciara.
                //  Para eso esta el FAB.
                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback() {

                    @Override
                    public void onResponse(List<Round> rounds) {
                        roundAdapter.setCurrent(position);
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

                // esta vacio???
                //if(repository.getRounds)
                TextView empty = new TextView(getActivity());
                empty.setText("EMPTY");
                empty.setVisibility(View.VISIBLE);
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
        this.updateUI();

        // Al cargar la vista de nuevo (solo en movil, en tablet nunca se oculta), reseteamos el
        //  actual.
        if (this.roundAdapter != null)
            this.roundAdapter.setCurrent(-1);
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

                Round round = new Round(RoundPreferenceActivity.getPlayerUUID(getActivity()));

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
        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback() {
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

            private Round round;

            public RoundHolder(View itemView) {
                super(itemView);

                this.idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                this.boardTextView = (TextView) itemView.findViewById(R.id.list_item_board);
                this.dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            }

            public void bindRound(Round round) {
                this.round = round;

                this.idTextView.setText(round.getTitle());
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
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);

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


        public boolean isRemovable(int position) {
            return current != position;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public boolean isCurrent(int position) {
            return position == this.current;
        }

        @Override
        public int getItemCount() {
            return this.rounds.size();
        }

    }
}