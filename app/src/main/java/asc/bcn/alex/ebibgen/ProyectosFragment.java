package asc.bcn.alex.ebibgen;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class ProyectosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int PROYECTO_LOADER = 0;
    private static final String[] PROYECTO_COLUMNS = {
            BibliografiaContract.ProyectoEntry.TABLE_NAME + "." + BibliografiaContract.ProyectoEntry._ID,
            BibliografiaContract.ProyectoEntry.COLUMN_TITULO
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_PROYECTO_ID = 0;
    static final int COL_PROYECTO_TITULO = 1;


    private ProyectosAdapter mProyectoAdapter;


    private TextView resultText;


    public ProyectosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_proyectos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_proyecto) {
//            updateWeather();
            //Aqui tenemos que mostrar n popup para poner el nombre del proyecto,
            // y a continuación llamar a contentProvider para insertar el nuevo proyecto
            showInputDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mProyectoAdapter = new ProyectosAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_proyectos);
        listView.setAdapter(mProyectoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
////                    String locationSetting = Utility.getPreferredLocation(getActivity());
//                    Log.e(LOG_TAG,Integer.toString(cursor.getInt(COL_PROYECTO_ID)));
                    Intent intent = new Intent(getActivity(), ProyectoActivity.class)
                            .setData(BibliografiaContract.ProyectoEntry.buildProyectoUri(
                                    cursor.getInt(COL_PROYECTO_ID)));
//                    intent.putExtra("idProyecto",cursor.getInt(COL_PROYECTO_ID));
                    Utility.setPreferredIdProyecto(getActivity(), cursor.getInt(COL_PROYECTO_ID));
//                    Log.e(LOG_TAG,"listen");
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        onUpdateProyecto();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PROYECTO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = BibliografiaContract.ProyectoEntry.COLUMN_TITULO + " ASC";
        Uri proyectoUri = BibliografiaContract.ProyectoEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),
                proyectoUri,
                PROYECTO_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProyectoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProyectoAdapter.swapCursor(null);
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        resultText.setText("Hello, " + editText.getText());
                        Log.e(LOG_TAG, editText.getText().toString());
                        // Comprovamos que no este nulo el valor in lo insertamos en la tabla proyectos con el content
                        //resolver. habra que actualizar la vista para que se vea reflejado.

                        addProyecto(editText.getText().toString());
//                        mProyectoAdapter.notifyDataSetChanged();
                        onUpdateProyecto();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private long addProyecto(String titulo) {
        long proyectoId;
        ContentValues proyectoValues = new ContentValues();
        proyectoValues.put(BibliografiaContract.ProyectoEntry.COLUMN_TITULO, titulo);

        // Finally, insert location data into the database.
        Uri insertedUri = getActivity().getContentResolver().insert(
                BibliografiaContract.ProyectoEntry.CONTENT_URI,
                proyectoValues
        );
        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
        proyectoId = ContentUris.parseId(insertedUri);
        return proyectoId;
    }

    public void onUpdateProyecto() {
        getLoaderManager().restartLoader(PROYECTO_LOADER, null, this);
    }

}
