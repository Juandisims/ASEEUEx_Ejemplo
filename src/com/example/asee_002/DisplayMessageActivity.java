package com.example.asee_002;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

//Ejemplo de actividad sencilla que recupera informacion del intent
public class DisplayMessageActivity extends Activity {

	//Sobreescribir onCreate para cargar vista y configurar activity
	@SuppressLint("NewApi") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Obtener el mensaje del intent que inicio el activity
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		// Crear un campo text view e inicializarlo con el mensaje del intent
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(message);

		// Establecer el text view como el layout del activity
		setContentView(textView);

		//Configurar action bar
		setupActionBar();
	}

	/**
	 * Configurar Action Bar
	 */
	private void setupActionBar() {

		//Activar el boton de navegacion Arriba (Up)
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	//Sobreescribir onOptionsItemSelected para capturar la seleccion de acciones en el Action Bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		//Identificar que accion se ha elegido en base a su id
		switch (item.getItemId()) {

			case android.R.id.home: //id del sistema que identifica el boton Arriba (Up)
			// Navegar siguiendo la jerarquia definida entre actividades
				NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
