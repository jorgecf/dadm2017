package es.uam.eps.dadm.jorgecifuentes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;

/**
 * Created by jorgecf on 15/03/17.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passwordEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_login);

        if (!PreferenceActivity.getPlayerName(this).equals(PreferenceActivity.PLAYERNAME_DEFAULT)) {
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }

        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        // Buscamos los botones y les pasamos esta actividad como listener.
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button newuserButton = (Button) findViewById(R.id.new_user_button);

        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        newuserButton.setOnClickListener(this);


        repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {

                    @Override
                    public void onLogin(String playerId) {
                        PreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        PreferenceActivity.setPlayerName(LoginActivity.this, playername);
                        PreferenceActivity.setPlayerPassword(LoginActivity.this, password);

                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        // TODO Se instancia dialogo de alerta
                    }

                };

        switch (v.getId()) {
            case R.id.login_button:
                repository.login(playername, password, loginRegisterCallback);
                break;
            case R.id.cancel_button:
                finish();
                break;
            case R.id.new_user_button:
                repository.register(playername, password, loginRegisterCallback);
                break;
        }
    }
}