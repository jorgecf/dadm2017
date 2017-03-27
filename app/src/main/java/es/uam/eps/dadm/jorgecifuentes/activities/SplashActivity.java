package es.uam.eps.dadm.jorgecifuentes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;

/**
 * Created by jorgecf on 27/03/17.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Make sure this is before calling super.onCreate
        this.setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

    }
}