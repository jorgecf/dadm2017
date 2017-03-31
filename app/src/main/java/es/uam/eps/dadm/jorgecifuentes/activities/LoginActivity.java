package es.uam.eps.dadm.jorgecifuentes.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;

/**
 * Actividad para el login de usuario.
 *
 * @author Jorge Cifuentes
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private RoundRepository repository;
    private TextInputLayout usernameEditText;
    private TextInputLayout passwordEditText;
    private Switch keepMeLoggedInSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_login);

        // Si si hay nombre almacenado (no devuelve el default), es que se ha hecho login, si ademas
        // el usuario ha elegido "mantenerme logueado", se salta la pantalla de login.
        if (RoundPreferenceActivity.getPlayerName(this).equals(RoundPreferenceActivity.PLAYERNAME_DEFAULT) == false //TODO cerrar con keeplogged FALSE ---> clearPref??
                && RoundPreferenceActivity.getKeepLogged(this) == true) {
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }

        this.usernameEditText = (TextInputLayout) this.findViewById(R.id.login_username_wrapper);
        this.passwordEditText = (TextInputLayout) this.findViewById(R.id.login_password_wrapper);
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

        final String playername = this.usernameEditText.getEditText().getText().toString();
        final String password = this.passwordEditText.getEditText().getText().toString();
        final Boolean remember = this.keepMeLoggedInSwitch.isChecked();

        // Control de errores de entrada.

        usernameEditText.setError(null);
        passwordEditText.setError(null);

        if (playername.length() == 0) {
            this.usernameEditText.setError("Rellene username"); //TODO string
            return;
        }

        if (password.length() == 0) {
            this.passwordEditText.setError("Rellene pass");
            return;
        }

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

                        // "Bajamos" el teclado en pantalla.
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        // Instanciamos un dialogo de aviso.
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppTheme_DialogAlert);
                        builder.setTitle("Dialog"); //TODO strings a todo el dialog
                        builder.setMessage(error);
                        builder.setPositiveButton("OK", null);
                        builder.show().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }

                };


        // Elegimos la accion dependiendo de la vista clickada.
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