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
 */
public class RoundActivity extends AppCompatActivity implements RoundFragment.Callbacks {

    public static final String EXTRA_FIRST_PLAYERNAME = "es.uam.eps.dadm.first_playername";
    public static final String EXTRA_ROUND_TITLE = "es.uam.eps.dadm.first_playername";
    public static final String EXTRA_ROUND_DATE = "es.uam.eps.dadm.round_date";
    public static final String EXTRA_ROUND_BOARD = "es.uam.eps.dadm.round_board";
    public static final String EXTRA_ROUND_UUID = "es.uam.eps.dadm.round_uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {

            String firstPlayerName = getIntent().getStringExtra(EXTRA_FIRST_PLAYERNAME);
            String roundTitle = getIntent().getStringExtra(EXTRA_ROUND_TITLE);
            String roundDate = getIntent().getStringExtra(EXTRA_ROUND_DATE);
            String roundBoard = getIntent().getStringExtra(EXTRA_ROUND_BOARD);
            String rounduuid = getIntent().getStringExtra(EXTRA_ROUND_UUID);

            RoundFragment roundFragment = RoundFragment.newInstance(rounduuid, firstPlayerName, roundTitle, roundDate, roundBoard); // nuevo fragmento
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, roundFragment).commit();
        }
    }

    /**
     * Devuelve una nueva intencion de esta clase.
     *
     * @param packageContext contexto de la intencion
     * @param rounduuid      Id de la ronda que se va a asociar a la actividad.
     * @return intencion creada
     */
    public static Intent newIntent(Context packageContext, String rounduuid, String firstPlayerName, String roundTitle, String roundDate, String roundBoard) {
        Intent intent = new Intent(packageContext, RoundActivity.class);

        intent.putExtra(EXTRA_FIRST_PLAYERNAME, firstPlayerName);
        intent.putExtra(EXTRA_ROUND_TITLE, roundTitle);
        intent.putExtra(EXTRA_ROUND_DATE, roundDate);
        intent.putExtra(EXTRA_ROUND_BOARD, roundBoard);
        intent.putExtra(EXTRA_ROUND_UUID, rounduuid);

        return intent;
    }

    @Override
    public void onRoundUpdated(Round round) {

    }

}