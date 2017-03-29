package es.uam.eps.dadm.jorgecifuentes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;


/**
 * Actividad que muestra la ayuda e información de la app.
 *
 * @author Jorge Cifuentes
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_help);
    }
}