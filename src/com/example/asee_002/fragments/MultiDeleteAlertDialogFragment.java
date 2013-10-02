package com.example.asee_002.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.asee_002.R;

//Ejemplo de creacion de un dialogo de confirmacion
public class MultiDeleteAlertDialogFragment extends DialogFragment {

	//Instancia a la que le comunicaremos los eventos que se produzcan en el dialogo: actividad padre
	NoticeDialogListener mListener;

	/* La actividad padre debe implementar este interfaz para poder recibir los eventos.
	 * */
	public interface NoticeDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Sobreescribir Fragment.onAttach() para instanciar el listener con la actividad padre
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verificar que la actividad padre implementa el interfaz definido
		try {
			//Instanciar listener con la actividad padre
			mListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			// Si la actividad no implementa el interfaz, lanzar excepcion
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	//Sobreescribir para configurar el dialogo a crear
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Usar la clase Builder para construir el dialogo
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_multi_delete)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Mandar el click en ok al listener (actividad padre)
				mListener.onDialogPositiveClick(MultiDeleteAlertDialogFragment.this);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Mandar el click en cancel al listener (actividad padre)
				mListener.onDialogNegativeClick(MultiDeleteAlertDialogFragment.this);
			}
		});
		// Crear el objeto AlertDialog y devolverlo
		return builder.create();
	}	

}
