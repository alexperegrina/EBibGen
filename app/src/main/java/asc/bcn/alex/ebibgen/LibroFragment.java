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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class LibroFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = LibrosFragment.class.getSimpleName();
    private static final int LIBRO_LOADER = 0;
    static final String PROYECTO_URI = "IDPROYECTO";

    private static final String[] LIBRO_COLUMNS = {
            BibliografiaContract.LibroEntry.TABLE_NAME + "." + BibliografiaContract.LibroEntry._ID,
            BibliografiaContract.LibroEntry.COLUMN_TITULO,
            BibliografiaContract.LibroEntry.COLUMN_DIA,
            BibliografiaContract.LibroEntry.COLUMN_MES,
            BibliografiaContract.LibroEntry.COLUMN_ANO,
            BibliografiaContract.LibroEntry.COLUMN_PAIS,
            BibliografiaContract.LibroEntry.COLUMN_CIUDAD,
            BibliografiaContract.LibroEntry.COLUMN_EDITORIAL,
            BibliografiaContract.LibroEntry.COLUMN_PAGINA_INI,
            BibliografiaContract.LibroEntry.COLUMN_PAGINA_FIN,
            BibliografiaContract.LibroEntry.COLUMN_ISBN
    };

    static final int COL_LIBRO_ID = 0;
    static final int COL_LIBRO_TITULO = 1;
    static final int COL_LIBRO_DIA = 2;
    static final int COL_LIBRO_MES = 3;
    static final int COL_LIBRO_ANO = 4;
    static final int COL_LIBRO_PAIS = 5;
    static final int COL_LIBRO_CIUDAD= 6;
    static final int COL_LIBRO_EDITORIAL = 7;
    static final int COL_LIBRO_PAGINA_INI= 8;
    static final int COL_LIBRO_PAGINA_FI= 9;
    static final int COL_LIBRO_ISBN = 10;



    private Uri mUri;

    private EditText edit_titulo;
    private EditText edit_dia;
    private EditText edit_mes;
    private EditText edit_ano;
    private EditText edit_editorial;
    private EditText edit_pagina_ini;
    private EditText edit_pagina_fi;
    private EditText edit_isbn;
    private Spinner spinner_pais;
//    private Spinner spinner_ciudad;

    private int idProyecto;
    private int idLibro;

    public LibroFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_libro, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_guardar_libro) {
            guardarLibro();
            return true;
        }
        if (id == R.id.action_eliminar_libro) {
            eliminarLibro();
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        idProyecto = Utility.getPreferredIdProyecto(getActivity());
        idLibro = Utility.getPreferredIdLibro(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_libro, container, false);

        edit_titulo = (EditText) rootView.findViewById(R.id.editText_libro_titulo);
        edit_dia = (EditText) rootView.findViewById(R.id.editText_libro_dia);
        edit_mes = (EditText) rootView.findViewById(R.id.editText_libro_mes);
        edit_ano = (EditText) rootView.findViewById(R.id.editText_libro_ano);
        edit_editorial = (EditText) rootView.findViewById(R.id.editText_libro_editorial);
        edit_pagina_ini = (EditText) rootView.findViewById(R.id.editText_libro_pagina_ini);
        edit_pagina_fi = (EditText) rootView.findViewById(R.id.editText_libro_pagina_fi);
        edit_isbn = (EditText) rootView.findViewById(R.id.editText_libro_isbn);
        spinner_pais = (Spinner) rootView.findViewById(R.id.spinner_libro_pais);
//        spinner_ciudad = (Spinner) rootView.findViewById(R.id.spinner_libro_ciudad);
        ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getPaises());
        spinner_pais.setAdapter(karant_adapter);


        setOnFocusChangeListener();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LIBRO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Hacer la peticion para todo los libros del proyecto indicado.

        int idProyecto = Utility.getPreferredIdProyecto(getActivity());
        int idLibro = Utility.getPreferredIdLibro(getActivity());

        mUri = BibliografiaContract.LibroEntry.buildLibroUri(idProyecto,idLibro);

        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    LIBRO_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mLibrosAdapter.swapCursor(data);
        if (data != null && data.moveToFirst()) {
//            mTitulo.setText(data.getString(COL_PROYECTO_TITULO));

            edit_titulo.setText(data.getString(COL_LIBRO_TITULO));
            if(data.getInt(COL_LIBRO_DIA) != 0) {
                edit_dia.setText(Integer.toString(data.getInt(COL_LIBRO_DIA)));
            }

            edit_mes.setText(data.getString(COL_LIBRO_MES));
            if(data.getInt(COL_LIBRO_ANO) != 0) {
                edit_ano.setText(Integer.toString(data.getInt(COL_LIBRO_ANO)));
            }

            edit_editorial.setText(data.getString(COL_LIBRO_EDITORIAL));
            if(data.getInt(COL_LIBRO_PAGINA_INI) != 0) {
                edit_pagina_ini.setText(Integer.toString((data.getInt(COL_LIBRO_PAGINA_INI))));
            }
            if(data.getInt(COL_LIBRO_PAGINA_FI) != 0) {
                edit_pagina_fi.setText(Integer.toString(data.getInt(COL_LIBRO_PAGINA_FI)));
            }
            edit_isbn.setText(data.getString(COL_LIBRO_ISBN));

            ArrayList<String> paises = getPaises();
            int id = -1;
            for (int i = 0; i < paises.size() && id == -1; i++) {
                if(data.getString(COL_LIBRO_PAIS) == paises.get(i)) {
                    id = i;
                }
            }
            if(id == -1) id = 0;
            spinner_pais.setSelection(id);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mLibrosAdapter.swapCursor(null);
    }

    /**
     * Metodo para cojer el nombre de todos los paises.
     * @return
     */
    private ArrayList<String>  getPaises() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();

            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
//        for (String country : countries) {
//            System.out.println(country);
//        }
//        System.out.println( "# countries found: " + countries.size());
        return countries;
    }

    private ContentValues getValues() {
        ContentValues libroValues = new ContentValues();

        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_ID_PROYECTO, Integer.toString(idProyecto));
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_TITULO, edit_titulo.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_DIA, edit_dia.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_MES, edit_mes.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_ANO, edit_ano.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_EDITORIAL, edit_editorial.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_PAGINA_INI, edit_pagina_ini.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_PAGINA_FIN, edit_pagina_fi.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_ISBN, edit_isbn.getText().toString());
        libroValues.put(BibliografiaContract.LibroEntry.COLUMN_PAIS, spinner_pais.getSelectedItem().toString());

        return libroValues;
    }

    public void guardarLibro() {
        Uri newUri = BibliografiaContract.LibroEntry.buildLibroUri(idProyecto,idLibro);

        getActivity().getContentResolver().update(
                newUri,
                getValues(),
                null,
                null
        );
    }

    private void setOnFocusChangeListener() {
        edit_titulo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_dia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_mes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_ano.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_editorial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_pagina_ini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_pagina_fi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        edit_isbn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
        spinner_pais.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarLibro();
            }
        });
    }


    private void eliminarLibro() {
        Uri newUri = BibliografiaContract.LibroEntry.buildLibroUri(idProyecto,idLibro);

        getActivity().getContentResolver().delete(
                newUri,
                null,
                null
        );
    }
}
