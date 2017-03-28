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

/**
 * Created by jorgecf on 13/03/17.
 */
public class RoundPreferenceActivity extends AppCompatActivity {

    private static final String PLAYERUUID = "playeruuid";
    private static final String PLAYERNAME = "playername";
    private static final String PLAYERPASSWORD = "password";
    protected static final String PLAYERNAME_DEFAULT = "def";
    private static final String KEEP_ME_LOGGED_IN = "keepMeLoggedIn";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RoundPreferenceFragment fragment = new RoundPreferenceFragment();

        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public static void setPlayerUUID(Context context, String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RoundPreferenceActivity.PLAYERUUID, id);
        editor.commit();
    }

    public static String getPlayerUUID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERUUID, "0"); //TODO defvalue
    }

    public static String getPlayerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERNAME, PLAYERNAME_DEFAULT);
    }

    public static void setPlayerName(Context context, String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RoundPreferenceActivity.PLAYERNAME, name); //TODO guardar tambien en base de datos -> o directamente no guardarlo en preferencias (ver usos)
        editor.commit();
    }

    public static void setPlayerPassword(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RoundPreferenceActivity.PLAYERPASSWORD, password);
        editor.commit();
    }


    public static Boolean getKeepLogged(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEEP_ME_LOGGED_IN, false);
    }

    public static void setKeepLogged(Context context, Boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEEP_ME_LOGGED_IN, b);
        editor.commit();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }
}