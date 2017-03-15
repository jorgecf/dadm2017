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
