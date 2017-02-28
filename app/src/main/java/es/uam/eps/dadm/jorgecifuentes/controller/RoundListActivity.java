package es.uam.eps.dadm.jorgecifuentes.controller;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;

public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks {

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
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, roundFragment).commit();
        }
    }


}