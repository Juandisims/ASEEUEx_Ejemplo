package com.example.asee_002;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.asee_002.fragments.MultiDeleteAlertDialogFragment;

/* Ejemplo de Actividad principal de la aplicacion:
 * 	- Contiene una ListView y, por tanto, hereda de ListActivity
 * 	- Contiene un Action Bar con action buttons, overflow, action view y share content provider
 *  - Inicia actividad DetailActivity al seleccionar un item de la lista
 *  - Activa modo de seleccion multiple en Action Bar para ejemplo de CAB
 *  - Crea dialogo de confirmacion para el borrado de multiples elementos de la lista
 *  - Contiene un campo de texto y un boton para ejemplo iniciar actividad DisplayMessage (paso de mensaje)
 *  
 *   */
public class MainActivity extends ListActivity 
implements MultiDeleteAlertDialogFragment.NoticeDialogListener {
	/* 
	 * Implementa interfaz MultiDeleteAlertDialogFragment.NoticeDialogListener para 
	 * poder recibir los eventos del dialogo MultiDeleteAlertDialogFragment 
	 */

	//Constante usada para los mensajes de Log
	private static final String TAG = "com.example.asee_002";

	//Constante usada como clave en los mensajes pasados en los extras de un intent
	public final static String EXTRA_MESSAGE = "com.example.asee_002.MESSAGE";

	//Instancia del ShareActionProvider usado en el Action Bar
	private ShareActionProvider mShareActionProvider;

	//Instancia que referencia a la ListView del Activity para poder interaccionar con ella
	//en el modo contextual (listeners del CAB)
	private ListView mListView;

	//Instancia que mantiene los datos a mostrar en el ListView
	//Se declara aqui para poder usarla en los metodos del modo contextual
	private List<String> mListaEjemplo;
	
	//Sobreescribir onCreate para cargar vista y configurar activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//establecer como vista el layout indicado
		setContentView(R.layout.activity_main);

		// inicializar lista contenidos de ejemplo
		mListaEjemplo = new ArrayList<String>();
		mListaEjemplo.add("Elemento 01");
		mListaEjemplo.add("Elemento 02");
		mListaEjemplo.add("Elemento 03");
		mListaEjemplo.add("Elemento 04");

		// Crear y establecer un adaptador para mListaEjemplo y la ListView
		setListAdapter(new ArrayAdapter<String>(this.getBaseContext(),
				R.layout.list_item,
				R.id.titulo,
				mListaEjemplo));

		// Recuperar la instancia de la ListView
		mListView = (ListView) findViewById(android.R.id.list);		
		// Cambiar su modo de operacion a seleccion multiple (activa respuesta CAB a long press)
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		// Configurar el manejador de eventos para el modo de seleccion multiple (CAB)
		mListView.setMultiChoiceModeListener(mMiListaListener); 

		//Poner mensaje en el logcat
		Log.d(TAG,"Creada Main Activity");
	}

	/* Ejemplo de inicio de Actividad DisplayMessage con paso de mensaje
	 * como respuesta al evento onCLick sobre el boton Send (configurado en el layout)
	 */
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		// Crear intent explicito para activity DisplayMessage
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		
		//Obtener referencia al campo de texto edit_message de la vista
		EditText editText = (EditText) findViewById(R.id.edit_message);
		
		//Obtener el texto del campo
		String message = editText.getText().toString();
		
		//crear un mensaje mediante la estructura EXTRA de un intent
		intent.putExtra(EXTRA_MESSAGE, message);
		
		//iniciar la actividad a partir del intent definido
		startActivity(intent);
	}

	/* 
	 * Sobreescribir onCreateOptionsMenu para indicar el esquema XML que contiene 
	 * el menu de acciones para la Action Bar
	 * 
	 * Ejemplo de la definicion de un Action Bar complejo
	 *  - Cargar menu de acciones
	 *  - Crear un action view de busqueda
	 *  - Crear un share action provider para compartir contenidos
	 *  
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflar el menu y poner acciones en Action Bar si esta.
		getMenuInflater().inflate(R.menu.main, menu);

		// Ejemplo de action view de busqueda
		// Obtener referencia al action view de busqueda
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		// Asignarle un manejador de eventos OnQueryTextListener 
		// para manejar que se envie un texto de busqueda o se cambie el texto de busqueda
		searchView.setOnQueryTextListener(mQueryTextListener);
		
		//Ejemplo de un share action provider
		// Obtener referencia al share action provider
		mShareActionProvider = (ShareActionProvider) menu.
				findItem(R.id.action_share).getActionProvider();
		
		// Configurar su intent para indicar que acciones debe incluir 
		mShareActionProvider.setShareIntent(getDefaultIntent());

		return super.onCreateOptionsMenu(menu);
	}

	// Ejemplo de Action Bar
	// Redefinir onOptionsItemSelected para capturar la seleccion de acciones en el Action Bar
	// Simplemente muestra un Toast indicando la accion seleccionada
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Identificar que accion se ha elegido en base a su id
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast.makeText(this, "Searching", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_compose:
			Toast.makeText(this, "Composing", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/* 
	 * Ejemplo de Action Bar complejo: listener para los eventos del Action View de busqueda
	 * 
	 * Definicion de una instancia de la clase SearchView.OnQueryTextListener
	 * y sobreescritura de los metodos abstractos de atencion de los eventos definidos 
	 * en esa clase
	 * 
	 * Forma comun de crear instancias de listeners a partir de clases abstractas en Java.
	 * 
	 */
	private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {

		// Redefinir para manejar evento de envio de texto de consulta para busqueda
		@Override
		public boolean onQueryTextSubmit(String query) {
			// Respuesta no implementada
			// Simplemente mostrar un mensaje de notificacion mediante un Toast
			Toast.makeText(getBaseContext(), "Searching "+query, Toast.LENGTH_SHORT).show();
			return false;
		}

		// Redefinir para manejar evento de cambio del texto de la consulta (sin envio)
		@Override
		public boolean onQueryTextChange(String newText) {
			// Respuesta no implementada
			return false;
		}
	};
	
	
	/**
	 * Ejemplo de Action Bar complejo: ShareContentProvider
	 *  
	 * Define un share intent de ejemplo para inicializar el action provider.
	 * En este caso es un intent implicito (para que pueda seleccionar multiples activities)
	 * para la accion de enviar contenidos del tipo imagen
	 * 
	 */
	private Intent getDefaultIntent() {
		
		// Crear intent implicito para la accion de enviar
		Intent intent = new Intent(Intent.ACTION_SEND);
		
		// Configurar intent para que el tipo de contenido a enviar sea cualquier formato de imagen
		intent.setType("image/*");
		
		return intent;
	}

	//Responde a los eventos onClick sobre los items de la lista
	@Override 
	protected void onListItemClick(ListView listView, 
			View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		//Obtenemos el item seleccionado
		Object o = getListAdapter().getItem(position);
		//Creamos un intent para iniciar la actividad de detalle
		Intent intent = new Intent(this, DetailActivity.class);
		//Pasamos el titulo del item como informacion extra en el intent para la nueva actividad
		intent.putExtra(EXTRA_MESSAGE, o.toString());
		//iniciamos la actividad
		startActivity(intent);
	}

	/**
	 * Ejemplo de modo de seleccion multiple (CAB)
	 * 
	 * Definicion del listener: crear instancia a partir de listener abstracto 
	 * y redefinir metodos de manejo de eventos y ciclo de vida del CAB
	 * 
	 */
	// Instancia para guardar los items seleccionados en la lista en el modo CAB
	private SparseBooleanArray mSelectedItemPositions;
	
	// Definicion del listener
	private MultiChoiceModeListener mMiListaListener = 
			new MultiChoiceModeListener() {

		// Redefinir para manejar evento de seleccion o deseleccion de un item
		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			// obtenemos numero de items seleccionados
			int count = mListView.getCheckedItemCount();
			
			// cambiamos el titulo del CAB con el numero de items seleccionados
			// se usa un recurso plurals para definir los mensajes a usar para cada valor del contador
			mode.setTitle(getResources().
					getQuantityString(R.plurals.selected_count, count, count));
		}

		// Redefinir para manejar eventos de la seleccion de una accion en la CAB
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			
			// Identificar que accion se ha elegido en base a su id
			switch (item.getItemId()) {
			case R.id.action_delete:
				// Mostrar mensaje en el log sobre numero de items seleccionados
				Log.d(TAG,"deleting "+mListView.getCheckedItemPositions().size()+" items");
				
				// copiar la informacion de los items seleccionados en la instancia
				// para conservar esa informacion despues de cerrar CAB
				mSelectedItemPositions = mListView.getCheckedItemPositions().clone();
				
				// Ejemplo de creacion y uso de un dialogo (DialogFragment)
				// Crear un dialogo de confirmacion de borrado multiple
				DialogFragment newFragment = new MultiDeleteAlertDialogFragment();
				newFragment.show(getFragmentManager(), "multidelete");   			

				//finalizar CAB despues de realizar accion
				mode.finish(); 
				return true;
			default:
				return false;
			}
		}

		// Redefinir para configurar CAB
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflar el menu del CAB a partir del recurso
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context, menu);       
			return true;
		}

		// Redefinir para realizar acciones de actualizacion de la actividad
		// antes de cerrar el CAB
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			
		}

		// Redefinir para realizar actualizaciones del CAD debidas a una peticion invalidate()
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			
			return false;
		}
	};

	/* 
	 * Ejemplo de creacion de dialogo: metodos para manejar eventos generados en el dialogo
	 * Callback del dialogo MultiSelectAlertDialog para boton ok
	 * @see com.example.asee_002.fragments.MultiDeleteAlertDialogFragment.NoticeDialogListener#onDialogPositiveClick(android.app.DialogFragment)
	 */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {

		if (mSelectedItemPositions == null) return;
		Log.d(TAG,"OK CALLBACK: deleting "+mSelectedItemPositions.size()+" items");
		for (int i = mSelectedItemPositions.size(); i>=0; i--)     
			if (mSelectedItemPositions.valueAt(i) == true) 
				mListaEjemplo.remove(i);
		BaseAdapter adapter = (BaseAdapter) mListView.getAdapter();
		adapter.notifyDataSetChanged();

	}

	/*
	 * Callback del dialogo MultiSelectAlertDialog para boton cancel
	 * @see com.example.asee_002.fragments.MultiDeleteAlertDialogFragment.NoticeDialogListener#onDialogNegativeClick(android.app.DialogFragment)
	 */
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		Log.d(TAG,"OK CALLBACK: deletion cancelled");

	}
}
