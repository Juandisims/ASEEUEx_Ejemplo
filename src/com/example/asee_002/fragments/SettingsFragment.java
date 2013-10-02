package com.example.asee_002.fragments;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.asee_002.R;

//Ejemplo de creacion de un fragmento para las preferencias
public class SettingsFragment extends PreferenceFragment {
	
	//Sobreescribir onCreate para cargar el esquema de las preferencias
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Cargar el esquema de las preferencias de un recurso XML 
	        addPreferencesFromResource(R.xml.preferences);
	    }
}
