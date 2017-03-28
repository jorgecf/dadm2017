package es.uam.eps.dadm.jorgecifuentes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
    private Switch keepMeLoggedInSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_login);


        // Si si hay nombre almacenado (no devuelve el default), es que se ha hecho login, si ademas
        // el usuario eligio "mantenerme logueado", se salta la pantalla de login.
        if (RoundPreferenceActivity.getPlayerName(this).equals(RoundPreferenceActivity.PLAYERNAME_DEFAULT) == false
                && RoundPreferenceActivity.getKeepLogged(this) == true) {
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }


        this.usernameEditText = (EditText) this.findViewById(R.id.login_username);
        this.passwordEditText = (EditText) this.findViewById(R.id.login_password);
        this.keepMeLoggedInSwitch = (Switch) this.findViewById(R.id.keep_me_logged_in_switch);

        // Buscamos los botones y les pasamos esta actividad como listener, ya que son los botones
        // que van a hacer algo.
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button newuserButton = (Button) findViewById(R.id.new_user_button);

        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        newuserButton.setOnClickListener(this);


        this.repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (this.repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        final String playername = this.usernameEditText.getText().toString();
        final String password = this.passwordEditText.getText().toString();
        final Boolean remember = this.keepMeLoggedInSwitch.isChecked();

        // Callback que se ejecutara al intentar hacer login.
        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {

                    @Override
                    public void onLogin(String playerId) {
                        RoundPreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        RoundPreferenceActivity.setPlayerName(LoginActivity.this, playername);
                        RoundPreferenceActivity.setPlayerPassword(LoginActivity.this, password);
                        RoundPreferenceActivity.setKeepLogged(LoginActivity.this, remember);

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
                this.repository.login(playername, password, loginRegisterCallback);
                break;
            case R.id.cancel_button:
                finish();
                break;
            case R.id.new_user_button:
                this.repository.register(playername, password, loginRegisterCallback);
                break;
        }
    }
}