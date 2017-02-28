/*package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;

/**
 * Created by jorgecf on 26/02/17.
 */
/*
public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks {

    private static final int SIZE = 3;
    //  private RecyclerView roundRecyclerView;
    //  private RoundAdapter roundAdapter;

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

        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = RoundActivity.newIntent(this, round.getId());
            startActivity(intent);
        } else { // lo sustituye por Round
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, roundFragment).commit();
        }

    }

    @Override
    public void onRoundUpdated(Round round) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }
}*/