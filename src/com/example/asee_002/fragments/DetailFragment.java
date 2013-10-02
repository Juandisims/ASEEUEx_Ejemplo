package com.example.asee_002.fragments;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asee_002.MainActivity;
import com.example.asee_002.R;

//Ejemplo de creacion de un fragmento 
public class DetailFragment extends Fragment {

	//Sobreescribir para indicar el layout que define la vista del fragmento
	//Sobreescribir siempre
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflar el layout del fragmento
		View view = inflater.inflate(R.layout.item_detail, container, false);
				
		//Recuperar los argumentos pasados el mensaje del intent a partir de los argumentos del fragmento
		String message = this.getArguments().getString(MainActivity.EXTRA_MESSAGE);

		// Inicializar el campo textView01 de la vista con el valor del mensaje
		TextView textView = (TextView) view.findViewById(R.id.textView01);
		textView.setText(message);

		//Devolver la vista creada
		return view;
	}
}
