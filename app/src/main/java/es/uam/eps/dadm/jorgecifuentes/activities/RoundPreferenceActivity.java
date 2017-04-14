package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.controller.RoundPreferenceFragment;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;

/**
 * Actividad que maneja el acceso a preferencias de la aplicacion.
 *
 * @author Jorge Cifuentes
 */
public class RoundPreferenceActivity extends AppCompatActivity {


    /**
     * Id del jugador.
     */
    private static final String PLAYERUUID = "playeruuid";

    /**
     * Nombre del jugador.
     */
    private static final String PLAYERNAME = "playername";

    /**
     * Clave del jugador.
     */
    private static final String PLAYERPASSWORD = "password";

    /**
     * Nombre por defecto.
     */
    protected static final String PLAYERNAME_DEFAULT = "";

    /**
     * Preferencia que, en caso de ser True, hace que se salte la pantalla de login.
     */
    private static final String KEEP_ME_LOGGED_IN = "keepMeLoggedIn";

    /**
     * Preferencia de juego online.
     */
    private static final String PLAY_ONLINE = "playOnline";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RoundPreferenceFragment fragment = new RoundPreferenceFragment();

        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Guarda un string en las preferencias.
     *
     * @param context Contexto de la llamada.
     * @param key     Clave del valor a guardar.
     * @param id      Valor a guardar.
     */
    private static void setString(Context context, String key, String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, id);
        editor.commit();
    }

    /**
     * Guarda un booleano en las preferencias.
     *
     * @param context Contexto de la llamada.
     * @param key     Clave del valor a guardar.
     * @param b       Valor a guardar.
     */
    private static void setBoolean(Context context, String key, Boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * Limpia las preferencias guardadas.
     *
     * @param context Contexto de la llamada.
     */
    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }


    public static void setPlayerUUID(Context context, String id) {
        RoundPreferenceActivity.setString(context, RoundPreferenceActivity.PLAYERUUID, id);
    }

    public static String getPlayerUUID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERUUID, "0");
    }


    public static void setPlayerName(Context context, String name) {

        // Actualizamos en la base de datos.
        RoundRepository r = RoundRepositoryFactory.createRepository(context);

        r.updateUser(RoundPreferenceActivity.getPlayerUUID(context), name, null, null);
        r.close();

        // Actualizamos en los ajustes.
        RoundPreferenceActivity.setString(context, RoundPreferenceActivity.PLAYERNAME, name);
    }


    public static String getPlayerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERNAME, PLAYERNAME_DEFAULT);
    }


    public static void setPlayerPassword(Context context, String password) {

        // Actualizamos en la base de datos.
        RoundRepository r = RoundRepositoryFactory.createRepository(context);

        r.updateUser(RoundPreferenceActivity.getPlayerUUID(context), null, password, null);
        r.close();

        // Actualizamos en los ajustes.
        RoundPreferenceActivity.setString(context, RoundPreferenceActivity.PLAYERPASSWORD, password);
    }


    public static Boolean getKeepLogged(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEEP_ME_LOGGED_IN, false);
    }

    public static void setKeepLogged(Context context, Boolean b) {
        RoundPreferenceActivity.setBoolean(context, RoundPreferenceActivity.KEEP_ME_LOGGED_IN, b);
    }


    public static Boolean getPlayOnline(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PLAY_ONLINE, false);
    }

    public static void setPlayOnline(Context context, Boolean b) {
        RoundPreferenceActivity.setBoolean(context, RoundPreferenceActivity.PLAY_ONLINE, b);
    }
}