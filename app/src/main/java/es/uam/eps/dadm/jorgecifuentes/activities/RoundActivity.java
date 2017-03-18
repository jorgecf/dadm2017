package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.controller.RoundFragment;
import es.uam.eps.dadm.jorgecifuentes.model.Round;

/**
 * Clase que representa la actividad de jugar: una ronda con su tablero.
 *
 * @author Jorge Cifuentes
 */public class RoundActivity extends AppCompatActivity implements RoundFragment.Callbacks {

    public static final String EXTRA_ROUND_ID = "es.uam.eps.dadm.round_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            //TODO gettear demas datos de extras
            RoundFragment roundFragment = RoundFragment.newInstance(roundId); // nuevo fragmento
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, roundFragment).commit();
        }
    }

    /**
     * Devuelve una nueva intencion de esta clase.
     *
     * @param packageContext contexto de la intencion
     * @param roundId        Id de la ronda que se va a asociar a la actividad.
     * @return intencion creada
     */
    public static Intent newIntent(Context packageContext, String roundId, String firstPlayerName, String roundTitle, int roundSize, String roundDate, String roundBoard) {
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        //TODO guardar demas datos en extras
        return intent;
    }

    @Override
    public void onRoundUpdated(Round round) {

    }

}