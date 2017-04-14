package es.uam.eps.dadm.jorgecifuentes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;
import es.uam.eps.dadm.jorgecifuentes.model.Triplet;
import es.uam.eps.dadm.jorgecifuentes.views.EmptyRecyclerView;

/**
 * Actividad que muestra en la pantalla informacion sobre las partidas alojadas en la base de datos.
 *
 * @author Jorge Cifuentes
 */
public class ScoresActivity extends AppCompatActivity {

    private EmptyRecyclerView scoresRecyclerView;
    private ScoreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_scores_list);
        this.scoresRecyclerView = (EmptyRecyclerView) this.findViewById(R.id.scores_recycler_view);

        // Vista recicladora.
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.scoresRecyclerView.setLayoutManager(linearLayoutManager);
        this.scoresRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.scoresRecyclerView.setEmptyView(this.findViewById(R.id.scores_empty_view));


        RoundRepository repository = RoundRepositoryFactory.createRepository(this);
        RoundRepository.RoundsCallback<Triplet<String, String, String>> rc = new RoundRepository.RoundsCallback<Triplet<String, String, String>>() {

            @Override
            public void onResponse(List<Triplet<String, String, String>> rounds) {
                adapter = new ScoreAdapter(rounds);
                scoresRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
            }

        };

        repository.getScores(rc);
        repository.close();
    }


    /**
     * Adapter para la vista de Scores.
     */
    public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreHolder> {

        private List<Triplet<String, String, String>> scores;

        public ScoreAdapter(List<Triplet<String, String, String>> scores) {
            this.scores = scores;
        }

        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_scores_item, parent, false);

            return new ScoreHolder(view);
        }

        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Triplet<String, String, String> round = this.scores.get(position);
            holder.bindRound(round, position);
        }

        @Override
        public int getItemCount() {
            return this.scores.size();
        }

        /**
         * Holder de elementos de la vista de Scores
         */
        public class ScoreHolder extends RecyclerView.ViewHolder {

            private TextView titleTextView;
            private TextView blackTextView;
            private TextView whiteTextView;


            public ScoreHolder(View itemView) {
                super(itemView);

                this.titleTextView = (TextView) itemView.findViewById(R.id.list_item_title);
                this.blackTextView = (TextView) itemView.findViewById(R.id.list_item_black);
                this.whiteTextView = (TextView) itemView.findViewById(R.id.list_item_white);
            }

            public void bindRound(Triplet<String, String, String> round, int position) {
                this.titleTextView.setText("(" + position + ") " + getString(R.string.round) + round.getFirst());
                this.blackTextView.setText(getString(R.string.black_player_has) + " " + round.getSecond());
                this.whiteTextView.setText(getString(R.string.white_player_has) + " " + round.getThird());
            }
        }

    }
}