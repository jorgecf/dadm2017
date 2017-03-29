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
    private static final String PLAY_ONLINE = "playOnline";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RoundPreferenceFragment fragment = new RoundPreferenceFragment();

        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }


    private static void setString(Context context, String key, String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, id);
        editor.commit();
    }

    private static void setBoolean(Context context, String key, Boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, b);
        editor.commit();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }


    public static void setPlayerUUID(Context context, String id) {
       /* SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RoundPreferenceActivity.PLAYERUUID, id);
        editor.commit();*/
        RoundPreferenceActivity.setString(context, RoundPreferenceActivity.PLAYERUUID, id);
    }

    public static String getPlayerUUID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERUUID, "0"); //TODO defvalue
    }

    public static void setPlayerName(Context context, String name) {
      /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RoundPreferenceActivity.PLAYERNAME, name);
        editor.commit();*/
        //TODO guardar tambien en base de datos
        RoundPreferenceActivity.setString(context, RoundPreferenceActivity.PLAYERNAME, name);
    }

    public static String getPlayerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERNAME, PLAYERNAME_DEFAULT);
    }


    public static void setPlayerPassword(Context context, String password) {
     /*   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RoundPreferenceActivity.PLAYERPASSWORD, password);
        editor.commit();*/
        RoundPreferenceActivity.setString(context, RoundPreferenceActivity.PLAYERPASSWORD, password);
    }


    public static Boolean getKeepLogged(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEEP_ME_LOGGED_IN, false);
    }

    public static void setKeepLogged(Context context, Boolean b) {
     /*   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEEP_ME_LOGGED_IN, b);
        editor.commit();*/
        RoundPreferenceActivity.setBoolean(context, RoundPreferenceActivity.KEEP_ME_LOGGED_IN, b);
    }

    public static Boolean getPlayOnline(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PLAY_ONLINE, false);
    }

    public static void setPlayOnline(Context context, Boolean b) {
     /*   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(PLAY_ONLINE, b);
        editor.commit();*/
        RoundPreferenceActivity.setBoolean(context, RoundPreferenceActivity.PLAY_ONLINE, b);
    }
}