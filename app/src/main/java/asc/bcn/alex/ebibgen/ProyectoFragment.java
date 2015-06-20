package asc.bcn.alex.ebibgen;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class ProyectoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = ProyectoFragment.class.getSimpleName();
    static final String PROYECTO_URI = "IDPROYECTO";

    private static final String[] PROYECTO_COLUMNS = {
            BibliografiaContract.ProyectoEntry.TABLE_NAME + "." + BibliografiaContract.ProyectoEntry._ID,
            BibliografiaContract.ProyectoEntry.COLUMN_TITULO
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_PROYECTO_ID = 0;
    static final int COL_PROYECTO_TITULO = 1;

    private Uri mUri;


    private static final int PROYECTO_LOADER = 0;

    private EditText mTitulo;

    public ProyectoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(ProyectoFragment.PROYECTO_URI);
//            Log.e(LOG_TAG,"-----");
//            Log.e(LOG_TAG,mUri.toString());
        }

        View rootView = inflater.inflate(R.layout.fragment_proyecto, container, false);
        mTitulo = (EditText) rootView.findViewById(R.id.editText_titulo_proyecto);
        mTitulo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarProyecto();
            }
        });
        return rootView;
//        return inflater.inflate(R.layout.fragment_proyecto, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_proyecto, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_guardar_proyecto) {
            guardarProyecto();
            return true;
        }
        if (id == R.id.action_eliminar_proyecto) {
            eliminarProyecto();
            getActivity().finish();
            return true;
        }
//        if (id == R.id.action_exportar_mla) {
//            exportMLA(Utility.getPreferredIdProyecto(getActivity()));
//            return true;
//        }
//        if (id == R.id.action_exportar_apa) {
//
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PROYECTO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.e(LOG_TAG,"onCreateLoader");

        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    PROYECTO_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Log.e(LOG_TAG,"onLoadFinished");
        if (data != null && data.moveToFirst()) {
            mTitulo.setText(data.getString(COL_PROYECTO_TITULO));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ContentValues getValues() {
        ContentValues values = new ContentValues();

        values.put(BibliografiaContract.ProyectoEntry.COLUMN_TITULO, mTitulo.getText().toString());

        return values;
    }

    public void guardarProyecto() {
        Uri newUri = BibliografiaContract.ProyectoEntry.buildProyectoUri(
                Utility.getPreferredIdProyecto(getActivity()));

        getActivity().getContentResolver().update(
                newUri,
                getValues(),
                null,
                null
        );
    }

    private void eliminarProyecto() {
        // hay que eliminar todos los libros que contiene y de cada libro los autores que contiene

        String[] LIBRO_COLUMNS = {
                BibliografiaContract.LibroEntry.TABLE_NAME + "." + BibliografiaContract.LibroEntry._ID,
                BibliografiaContract.LibroEntry.COLUMN_TITULO
        };


        Uri uriLibros = BibliografiaContract.LibroEntry.buildLibroWithIdProyecto(
                Utility.getPreferredIdProyecto(getActivity()));

        Cursor cursor = getActivity().getContentResolver().query(
                uriLibros,
                LIBRO_COLUMNS,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        Uri uriAutores;
        int idLibro;
        do {
            idLibro = cursor.getInt(0);
            uriAutores = BibliografiaContract.AutorEntry.buildAutorWithIdLibro(idLibro);

            //eliminamos los autores
            getActivity().getContentResolver().delete(
                    uriAutores,
                    null,
                    null
            );

        } while (cursor.moveToNext());

        //eliminamos los libros
        getActivity().getContentResolver().delete(
                uriLibros,
                null,
                null
        );


        Uri newUri = BibliografiaContract.ProyectoEntry.buildProyectoUri(
                Utility.getPreferredIdProyecto(getActivity()));


        // eliminamos proyecto
        getActivity().getContentResolver().delete(
                newUri,
                null,
                null
        );
    }

//    private void exportMLA(int idProyecto) {
//
////        String[] LIBRO_COLUMNS = {
////                BibliografiaContract.LibroEntry.TABLE_NAME + "." + BibliografiaContract.LibroEntry._ID,
////                BibliografiaContract.LibroEntry.COLUMN_TITULO,
////                BibliografiaContract.LibroEntry.COLUMN_DIA,
////                BibliografiaContract.LibroEntry.COLUMN_MES,
////                BibliografiaContract.LibroEntry.COLUMN_ANO,
////                BibliografiaContract.LibroEntry.COLUMN_PAIS,
////                BibliografiaContract.LibroEntry.COLUMN_CIUDAD,
////                BibliografiaContract.LibroEntry.COLUMN_EDITORIAL,
////                BibliografiaContract.LibroEntry.COLUMN_PAGINA_INI,
////                BibliografiaContract.LibroEntry.COLUMN_PAGINA_FIN,
////                BibliografiaContract.LibroEntry.COLUMN_ISBN
////        };
////
////        int COL_LIBRO_ID = 0;
////        int COL_LIBRO_TITULO = 1;
////        int COL_LIBRO_DIA = 2;
////        int COL_LIBRO_MES = 3;
////        int COL_LIBRO_ANO = 4;
////        int COL_LIBRO_PAIS = 5;
////        int COL_LIBRO_CIUDAD= 6;
////        int COL_LIBRO_EDITORIAL = 7;
////        int COL_LIBRO_PAGINA_INI= 8;
////        int COL_LIBRO_PAGINA_FI= 9;
////        int COL_LIBRO_ISBN = 10;
//
//        Log.e(LOG_TAG, "INICIO MLA");
//        Cursor cursorAutores;
//        String bibliografia = "";
//        String autores;
//
//        String sortOrder = BibliografiaContract.LibroEntry.COLUMN_TITULO + " ASC";
//        Uri uriLibros = BibliografiaContract.LibroEntry.buildLibroWithIdProyecto(idProyecto);
//        Cursor cursorLibros = getActivity().getContentResolver().query(
//          uriLibros,
//                LibroFragment.LIBRO_COLUMNS,null,null,null
//        );
//
//
//        Log.e(LOG_TAG, "MLA 1");
//        cursorLibros.moveToFirst();
//        Uri uriAutores;
//        int idLibro;
//        do {
//            Log.e(LOG_TAG, "MLA 2");
//            autores = "";
//            idLibro = cursorLibros.getInt(0);
//            uriAutores = BibliografiaContract.AutorEntry.buildAutorWithIdLibro(idLibro);
//            Log.e(LOG_TAG,uriAutores.toString());
//
//            cursorAutores =  getActivity().getContentResolver().query(
//                    uriAutores,
//                    AutoresFragment.AUTOR_COLUMNS,
//                    null,
//                    null,
//                    null
//            );
//            cursorAutores.moveToFirst();
//            do {
//                Log.e(LOG_TAG, "MLA 3");
//                autores.concat(cursorAutores.getString(AutoresFragment.COL_AUTOR_NOMBRE).concat(", "));
//                Log.e(LOG_TAG, "MLA 3.5");
//            } while (cursorAutores.moveToNext());
//
//            Log.e(LOG_TAG, "MLA 4");
//            bibliografia += autores + ": " + cursorLibros.getString(LibroFragment.COL_LIBRO_TITULO) + ": "
//                    + cursorLibros.getString(LibroFragment.COL_LIBRO_PAIS) + ": " +
//                    cursorLibros.getString(LibroFragment.COL_LIBRO_EDITORIAL) + ", " +
//                    Integer.toString(cursorLibros.getInt(LibroFragment.COL_LIBRO_ANO)) + ". ";
//            Log.e(LOG_TAG, "MLA 4");
//            if(cursorLibros.getInt(LibroFragment.COL_LIBRO_PAGINA_INI) != 0) {
//                bibliografia += Integer.toString(
//                        cursorLibros.getInt(LibroFragment.COL_LIBRO_PAGINA_INI))
//                        + " - " +
//                        Integer.toString(cursorLibros.getInt(LibroFragment.COL_LIBRO_PAGINA_FI));
//            }
//
//            Log.e(LOG_TAG, "MLA 5");
//            bibliografia += "\n";
//
//        } while (cursorLibros.moveToNext());
//
//        Log.e(LOG_TAG, "MLA 6");
//        Log.e(LOG_TAG, bibliografia);
//
//    }
}
