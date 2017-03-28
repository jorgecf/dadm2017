package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

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

        Preference access_contacts = this.findPreference("access_contacts");
        Preference logout = this.findPreference("logout");
        Preference switch_theme = this.findPreference("switch_theme");


        //TODO string
        if (access_contacts != null) {
            access_contacts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), ContactsActivity.class));
                    return true;
                }
            });
        }

        if (logout != null) {
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    // Borramos las preferencias y realizamos el logout.
                    startActivity(new Intent(getActivity(), LoginActivity.class));

                    RoundPreferenceActivity.clearPreferences(getActivity());

                    return true;
                }
            });
        }

        // Switch para elegir tema.
        if (switch_theme != null) {
            switch_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    getActivity().getApplication().setTheme(R.style.AppTheme_Dark);
                 //   getActivity().setContentView(R.layout.activity_fragment);


                 //   Snackbar.make(getView(), "Changeado", Snackbar.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

    }
}
