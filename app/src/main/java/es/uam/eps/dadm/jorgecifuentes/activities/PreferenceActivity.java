package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.controller.PreferencesFragment;

/**
 * Created by jorgecf on 13/03/17.
 */
public class PreferenceActivity extends AppCompatActivity {

    public final static String BOARDSIZE_KEY = "boardsize";
    public final static String BOARDSIZE_DEFAULT = "0";
    private static final String PLAYERUUID = "playeruuid";
    private static final String PLAYERNAME = "playername";
    private static final String PLAYERPASSWORD = "password";
    public static final String PLAYERNAME_DEFAULT = "defoe";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PreferencesFragment fragment = new PreferencesFragment();

        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public static void setPlayerUUID(Context context, String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PreferenceActivity.PLAYERUUID, id);
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

        editor.putString(PreferenceActivity.PLAYERNAME, name);
        editor.commit();
    }

    public static void setPlayerPassword(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PreferenceActivity.PLAYERPASSWORD, password);
        editor.commit();
    }


/*
    public static String getBoardSize(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(BOARDSIZE_KEY, BOARDSIZE_DEFAULT);
    }

    public static void setBoardsize(Context context, int size) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PreferenceActivity.BOARDSIZE_KEY, size);
        editor.commit();
    }
*/
}
