package asc.bcn.alex.ebibgen;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


public class AutoresFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = AutoresFragment.class.getSimpleName();
    private static final int AUTOR_LOADER = 0;
    static final String PROYECTO_URI = "IDPROYECTO";

    public static final String[] AUTOR_COLUMNS = {
            BibliografiaContract.AutorEntry.TABLE_NAME + "." + BibliografiaContract.AutorEntry._ID,
            BibliografiaContract.AutorEntry.COLUMN_NOMBRE
    };

    static final int COL_AUTOR_ID = 0;
    static final int COL_AUTOR_NOMBRE = 1;

    private AutoresAdapter mAutoresAdapter;
    private Uri mUri;

    private int idProyecto;
    private int idLibro;

    ImageButton imageAddAutor;

    public AutoresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        idLibro = Utility.getPreferredIdAutor(getActivity());
        idProyecto = Utility.getPreferredIdProyecto(getActivity());
        idLibro = Utility.getPreferredIdLibro(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_autores, container, false);
//
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(AutoresFragment.PROYECTO_URI);
        }

        mAutoresAdapter = new AutoresAdapter(getActivity(),null,0);
        ListView listAutores = (ListView) rootView.findViewById(R.id.listView_autores);
        listAutores.setAdapter(mAutoresAdapter);
        listAutores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
//                    Intent intent = new Intent(getActivity(), LibroActivity.class);
//                    Utility.setPreferredIdLibro(getActivity(), cursor.getInt(COL_AUTOR_ID));
//                    startActivity(intent);

                    showInputDialogUpdateAutor(
                            cursor.getInt(COL_AUTOR_ID),cursor.getString(COL_AUTOR_NOMBRE));

                }
            }
        });

        imageAddAutor = (ImageButton) rootView.findViewById(R.id.imageButton_add_autor);
        imageAddAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
//                Intent intent = new Intent(getActivity(), LibroActivity.class);
//                Utility.setPreferredIdLibro(getActivity(), -1);
//                startActivity(intent);
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
        getLoaderManager().initLoader(AUTOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = BibliografiaContract.AutorEntry.COLUMN_NOMBRE + " ASC";
        Uri autoresUri = BibliografiaContract.AutorEntry.buildAutorWithIdLibro(idLibro);

        return new CursorLoader(getActivity(),
                autoresUri,
                AUTOR_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAutoresAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAutoresAdapter.swapCursor(null);
    }

    public void onUpdateProyecto() {
        getLoaderManager().restartLoader(AUTOR_LOADER, null, this);
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_autor, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext_nombre_autor);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        addAutor(editText.getText().toString());
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

    protected void showInputDialogUpdateAutor(final int idAutor, String nombre) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_autor, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        EditText editText2 = (EditText) promptView.findViewById(R.id.edittext_nombre_autor);
        editText2.setText(nombre);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext_nombre_autor);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        guardarAutor(idAutor, editText.getText().toString());
                        onUpdateProyecto();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarAutor(idAutor);
//                        getActivity().finish();
                        onUpdateProyecto();
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private long addAutor(String nombre) {
        long proyectoId;
        ContentValues values = new ContentValues();

        values.put(BibliografiaContract.AutorEntry.COLUMN_ID_LIBRO, idLibro);
        values.put(BibliografiaContract.AutorEntry.COLUMN_NOMBRE, nombre);

        Uri newUri = BibliografiaContract.AutorEntry.buildAutorWithIdLibro(idLibro);
//        Log.e(LOG_TAG,newUri.toString());
        Uri insertedUri = getActivity().getContentResolver().insert(
                newUri,
                values
        );

        proyectoId = ContentUris.parseId(insertedUri);
        return proyectoId;
    }

    public void guardarAutor(int idAutor, String nombre) {
        Uri newUri = BibliografiaContract.AutorEntry.buildAutorUri(idLibro, idAutor);

        ContentValues values = new ContentValues();

        values.put(BibliografiaContract.AutorEntry.COLUMN_NOMBRE, nombre);

        getActivity().getContentResolver().update(
                newUri,
                values,
                null,
                null
        );
    }

    private void eliminarAutor(int idAutor) {
        Uri newUri = BibliografiaContract.AutorEntry.buildAutorUri(idLibro, idAutor);

        getActivity().getContentResolver().delete(
                newUri,
                null,
                null
        );
    }

}
