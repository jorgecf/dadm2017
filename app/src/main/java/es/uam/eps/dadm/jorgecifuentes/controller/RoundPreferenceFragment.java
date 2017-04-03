package es.uam.eps.dadm.jorgecifuentes.controller;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

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
        EditTextPreference change_password = (EditTextPreference) this.findPreference("change_password");


        // Preferencia de cambio de nombre.
        if (select_name != null) {

            // Al cambiar el valor del cambio de nombre, actualizamos las preferencias y bbdd.
            select_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    RoundPreferenceActivity.setPlayerName(getActivity(), (String) newValue);
                    return true;
                }
            });

            // Al pulsar en la preferencia, queremos que el nombre de usuario actual salga por defecto.
            select_name.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    select_name.setText(RoundPreferenceActivity.getPlayerName(getActivity()));
                    return true;
                }
            });
        }

        // Preferencia de cambio de password.
        if (change_password != null) {
            change_password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    RoundPreferenceActivity.setPlayerPassword(getActivity(), (String) newValue);
                    return true;
                }
            });
        }


        // Preferencia para mostrar la lista de contactos.
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
                    RoundPreferenceActivity.clearPreferences(getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);

                    // FLAG_ACTIVITY_CLEAR_TASK borra todas las actividades menos la del intent. Para
                    // que esto funcione, la actual deberia estar aun en stack; es decir no haber sido
                    // acabada, o bien usar la bandera FLAG_ACTIVITY_NEW_TASK.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

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