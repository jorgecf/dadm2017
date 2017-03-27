package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.jorgecifuentes.R;

/**
 * Created by jorgecf on 27/03/17.
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