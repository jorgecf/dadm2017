package es.uam.eps.dadm.jorgecifuentes.controller;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import es.uam.eps.dadm.jorgecifuentes.R;


/**
 * Created by jorgecf on 13/03/17.
 */

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.settings);
    }
}
