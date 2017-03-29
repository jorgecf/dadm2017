package es.uam.eps.dadm.jorgecifuentes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;

/**
 * Created by jorgecf on 27/03/17.
 */

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_scores);


        final TextView playername = (TextView) this.findViewById(R.id.playername);
        final TextView playername_subtitle = (TextView) this.findViewById(R.id.playername_subtitle);

        RoundRepository repository = RoundRepositoryFactory.createRepository(this);

        RoundRepository.RoundsCallback rc = new RoundRepository.RoundsCallback() { //TODO nueva func de repo solo para el num de rondas? o no?
            @Override
            public void onResponse(List<Round> rounds) {
                playername_subtitle.setText(String.valueOf(rounds.size()) + " rounds played"); //TODO string
            }

            @Override
            public void onError(String error) {
                playername_subtitle.setText("No" + " rounds played");
            }
        };


        playername.setText(RoundPreferenceActivity.getPlayerName(this));
        repository.getRounds(RoundPreferenceActivity.getPlayerUUID(this), null, null, rc);


    }
}