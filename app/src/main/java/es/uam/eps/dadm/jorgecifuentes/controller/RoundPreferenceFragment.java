package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.activities.ContactsActivity;
import es.uam.eps.dadm.jorgecifuentes.activities.LoginActivity;
import es.uam.eps.dadm.jorgecifuentes.activities.RoundListActivity;
import es.uam.eps.dadm.jorgecifuentes.activities.RoundPreferenceActivity;


/**
 * Created by jorgecf on 13/03/17.
 */

public class RoundPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargamos los ajustes desde el xml.
        this.addPreferencesFromResource(R.xml.settings);

        //TODO string
        this.findPreference("access_contacts").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), ContactsActivity.class));
                return true;
            }
        });

        this.findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                // Borramos las preferencias y realizamos el logout.
                startActivity(new Intent(getActivity(), LoginActivity.class));

               RoundPreferenceActivity.clearPreferences(getActivity());

                return true;
            }
        });

    }
}
