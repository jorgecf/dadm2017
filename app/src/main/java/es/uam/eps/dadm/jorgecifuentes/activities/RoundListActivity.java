package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.controller.RoundFragment;
import es.uam.eps.dadm.jorgecifuentes.controller.RoundListFragment;
import es.uam.eps.dadm.jorgecifuentes.model.Round;

/**
 * Clase que representa la actividad de una lista de rondas.
 *
 * @author Jorge Cifuentes
 */
public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new RoundListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onRoundSelected(Round round) {

        if (this.findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = RoundActivity.newIntent(this, round.getRoundUUID(), round.getPlayername(), round.getTitle(), round.getDate(), round.getBoard().tableroToString(), round.getRivalUUID());
            startActivity(intent);
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getRoundUUID(), round.getPlayername(), round.getTitle(), round.getDate(), round.getBoard().tableroToString(), round.getRivalUUID());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, roundFragment).commit();
        }
    }

    @Override
    public void onPreferencesSelected() {
        Intent intent = new Intent(this, RoundPreferenceActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onHelpSelected() {
        Intent intent = new Intent(this, HelpActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onScoresSelected() {
        Intent intent = new Intent(this, ScoresActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onRoundUpdated(Round round) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }
}