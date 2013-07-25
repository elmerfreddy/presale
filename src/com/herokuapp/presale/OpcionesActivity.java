package com.herokuapp.presale;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class OpcionesActivity extends PreferenceActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadUserPreferences();
    }

	@SuppressWarnings("deprecation")
	private void loadUserPreferences() {
		addPreferencesFromResource(R.xml.opciones);
	}
}
