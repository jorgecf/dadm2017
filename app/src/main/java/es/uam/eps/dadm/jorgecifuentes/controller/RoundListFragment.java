package es.uam.eps.dadm.jorgecifuentes.controller;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

        View view = inflater.inflate(R.layout.fragment_round_list, container, false);
        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());

        updateUI();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    protected void updateUI() {

        RoundRepository repository = RoundRepository.get(getActivity());
        List<Round> rounds = repository.getRounds();

        if (roundAdapter == null) {
            roundAdapter = new RoundAdapter(rounds);
            roundRecyclerView.setAdapter(roundAdapter);
        } else {
            roundAdapter.notifyDataSetChanged();
        }

    }

    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder> {

        private List<Round> rounds;

        public class RoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
                boardTextView.setText("REVERSI");//round.getBoard().toString()); TODO
                dateTextView.setText(String.valueOf(round.getDate()).substring(0, 19));
            }

            @Override
            public void onClick(View v) {
                Log.d("log", "onclick de roundholder");
                callbacks.onRoundSelected(round);
            }
        }

        public RoundAdapter(List<Round> rounds) {
            this.rounds = rounds;
        }

        @Override
        public RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
            return new RoundHolder(view);
        }

        @Override
        public void onBindViewHolder(RoundAdapter.RoundHolder holder, int position) {
            Round round = rounds.get(position);
            holder.bindRound(round);
        }

        @Override
        public int getItemCount() {
            return rounds.size();
        }
    }
}