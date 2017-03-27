package es.uam.eps.dadm.jorgecifuentes.controller;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import es.uam.eps.dadm.jorgecifuentes.R;


/**
 * Created by jorgecf on 13/03/17.
 */

public class RoundPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Cargamos los ajustes desde el xml.
        this.addPreferencesFromResource(R.xml.settings);
    }
}
