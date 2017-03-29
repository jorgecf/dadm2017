package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;


/**
 * Actividad que muestra un Splash Screen. No agrega tiempo de carga, solo lo muestra durante el
 * tiempo que la app esta cargandose.
 *
 * @author Jorge Cifuentes
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();
    }
}