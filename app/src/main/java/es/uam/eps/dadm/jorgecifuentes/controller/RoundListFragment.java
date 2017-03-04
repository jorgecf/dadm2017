package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;

/**
 * Created by jorgecf on 27/02/17.
 */
public class RoundListFragment extends Fragment {

    private RecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;

    private Callbacks callbacks;

    public interface Callbacks {
        void onRoundSelected(Round round);
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

        final View view = inflater.inflate(R.layout.fragment_round_list, container, false);
        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // listener de cada item de la cardview
        roundRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // Si la partida esta iniciada, clickar nuevamente sobre su CardView no la reiniciara.
                //  Para ello esta su FAB.
                if (roundAdapter.isCurrent(position) == false) {
                    Round round = RoundRepository.get(getContext()).getRounds().get(position);
                    roundAdapter.setCurrent(position);
                    callbacks.onRoundSelected(round);
                }
            }
        }));

        // listener del dismiss de la cardview
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                roundAdapter.remove(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // Este metodo se llama antes de empezar la animacion del Swipe. Si la partida es la
                //  actual, logicamente no se puede quitar de la lista, asi que se bloquea la
                //  accion.
                if (roundAdapter.isRemovable(viewHolder.getAdapterPosition()) == false) {
                    return 0;
                }

                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(roundRecyclerView);


        // Establecemos la visibilidad del options menu.
        this.setHasOptionsMenu(true);

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    // menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_new_round: // crea ronda al seleccionar item ( + new round )
                Round round = new Round();
                RoundRepository.get(getActivity()).addRound(round);
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // menu


    public void updateUI() {

        RoundRepository repository = RoundRepository.get(getActivity());
        List<Round> rounds = repository.getRounds();

        if (roundAdapter == null) {
            roundAdapter = new RoundAdapter(rounds);
            roundRecyclerView.setAdapter(roundAdapter);
        } else {
            roundAdapter.notifyDataSetChanged();
        }

    }

    // roundadapter
    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder> {

        private List<Round> rounds;
        private int current;


        // roundholder
        public class RoundHolder extends RecyclerView.ViewHolder {

            private TextView idTextView;
            private TextView boardTextView;
            private TextView dateTextView;


            private Round round;

            public RoundHolder(View itemView) {
                super(itemView);

                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                boardTextView = (TextView) itemView.findViewById(R.id.list_item_board);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            }

            public void bindRound(Round round) {
                this.round = round;

                idTextView.setText(round.getTitle());
                boardTextView.setText("REVERSI");//round.getBoard().toSimpleString());
                dateTextView.setText(String.valueOf(round.getDate()).substring(0, 19));
            }
        }


        public RoundAdapter(List<Round> rounds) {
            this.rounds = rounds;
            this.current = -1;
        }

        @Override
        public RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);

            return new RoundHolder(view);
        }


        @Override
        public void onBindViewHolder(RoundAdapter.RoundHolder holder, int position) {
            Round round = this.rounds.get(position);
            holder.bindRound(round);
        }

        // para swipe
        public void remove(int position) {
            this.rounds.remove(position);
            this.notifyItemRemoved(position);
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