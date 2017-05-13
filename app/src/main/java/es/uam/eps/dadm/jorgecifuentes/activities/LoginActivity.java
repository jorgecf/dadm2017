package es.uam.eps.dadm.jorgecifuentes.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;
import es.uam.eps.dadm.jorgecifuentes.server.MessagingService;

/**
 * Actividad para el login de usuario.
 *
 * @author Jorge Cifuentes
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private RoundRepository repository; //TODO todos los repositorys close en onDestroy
    private TextInputLayout usernameEditText;
    private TextInputLayout passwordEditText;
    private Switch keepMeLoggedInSwitch;
    private Switch playOnlineSwitch;

    public boolean isOnline() { //TODO aunque marque online si no hay conexion abrir bbdd
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_login);


        // FIREBASE
        String token = FirebaseInstanceId.getInstance().getToken();  // Codigo de firebase
        RoundPreferenceActivity.setFirebaseToken(LoginActivity.this, token);

        // Iniciamos SERVICIO DE MENSAJERIA
        startService(new Intent(LoginActivity.this, MessagingService.class));


        // Si sí hay nombre almacenado (no devuelve el default), es que se ha hecho login; si ademas
        // el usuario ha elegido "mantenerme logueado", se salta la pantalla de login.
        if (RoundPreferenceActivity.getPlayerName(this).equals(RoundPreferenceActivity.PLAYERNAME_DEFAULT) == false
                && RoundPreferenceActivity.getKeepLogged(this) == true) {
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }

        // En caso de no estar activado recuerdo de sesion, borramos preferencias y empezamos de nuevo proceso de login.
        RoundPreferenceActivity.clearPreferences(this);

        this.usernameEditText = (TextInputLayout) this.findViewById(R.id.login_username_wrapper);
        this.passwordEditText = (TextInputLayout) this.findViewById(R.id.login_password_wrapper);
        this.keepMeLoggedInSwitch = (Switch) this.findViewById(R.id.keep_me_logged_in_switch);
        this.playOnlineSwitch = (Switch) this.findViewById(R.id.play_online_switch);


        // Buscamos los botones y les pasamos esta actividad como listener, ya que son los botones
        // que van a hacer acciones.
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button newuserButton = (Button) findViewById(R.id.new_user_button);

        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        newuserButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final String playername = this.usernameEditText.getEditText().getText().toString();
        final String password = this.passwordEditText.getEditText().getText().toString();
        final Boolean remember = this.keepMeLoggedInSwitch.isChecked();
        final Boolean online = this.playOnlineSwitch.isChecked();

        // Primero dejamos puesto el modo de juego
        RoundPreferenceActivity.setPlayOnline(LoginActivity.this, online);

        // Control de errores de entrada.
        this.usernameEditText.setError(null);
        this.passwordEditText.setError(null);


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
                            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        // Instanciamos un dialogo de aviso.
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppTheme_DialogAlert);
                        builder.setTitle(R.string.login_error_title);
                        builder.setMessage(error);
                        builder.setPositiveButton(R.string.ok, null);
                        builder.show().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }

                };

        // Creamos el repositorio
        this.repository = RoundRepositoryFactory.createRepository(LoginActivity.this, online);
        if (this.repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error, Toast.LENGTH_SHORT).show();

        // Elegimos la accion dependiendo de la vista clickada.
        switch (v.getId()) {

            case R.id.login_button:
                if (this.checkInputValid(playername, password)) {
                    this.repository.login(playername, password, loginRegisterCallback);
                }
                break;

            case R.id.new_user_button:
                if (this.checkInputValid(playername, password)) {
                    this.repository.register(playername, password, loginRegisterCallback);
                }
                break;

            case R.id.cancel_button:
                finish();
                break;

        }
    }

    /**
     * Comprueba la validez del inicio de sesion antes de interactuar con la base de datos o servidor.
     *
     * @param playername Nombre de usuario introducido.
     * @param password   Clave introducida.
     * @return True con datos validos para la base de datos, false si no.
     */
    private boolean checkInputValid(String playername, String password) {
        if (playername.length() == 0) {
            this.usernameEditText.setError(this.getString(R.string.fill_text));
            this.usernameEditText.requestFocus();
            return false;
        }

        if (password.length() == 0) {
            this.passwordEditText.setError(this.getString(R.string.fill_text));
            this.passwordEditText.requestFocus();
            return false;
        }

        return true;
    }
}