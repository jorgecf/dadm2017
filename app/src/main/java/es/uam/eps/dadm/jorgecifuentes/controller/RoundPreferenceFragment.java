package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.activities.ContactsActivity;
import es.uam.eps.dadm.jorgecifuentes.activities.LoginActivity;
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
        final EditTextPreference select_name = (EditTextPreference) this.findPreference("select_name");
        Preference access_contacts = this.findPreference("access_contacts");
        Preference logout = this.findPreference("logout");
        Preference switch_online = this.findPreference("switch_online");


        // Preferencia de cambio de nombre.
        if (select_name != null) {
            select_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    RoundPreferenceActivity.setPlayerName(getActivity(), (String) newValue);
                    return true;
                }
            });
        }

        // Preferencia para mostrar a lista de contactos.
        if (access_contacts != null) {
            access_contacts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), ContactsActivity.class));
                    return true;
                }
            });
        }

        // Preferencia para salir a la pantalla de login.
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

        // Switch de eleccion de juego online.
        if (switch_online != null) {
            switch_online.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    RoundPreferenceActivity.setPlayOnline(getActivity(), (boolean) newValue);
                    return true;
                }
            });
        }

    }
}