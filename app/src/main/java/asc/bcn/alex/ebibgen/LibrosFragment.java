package asc.bcn.alex.ebibgen;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


public class LibrosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = LibrosFragment.class.getSimpleName();
    private static final int LIBRO_LOADER = 0;
    static final String PROYECTO_URI = "IDPROYECTO";

    private static final String[] LIBRO_COLUMNS = {
            BibliografiaContract.LibroEntry.TABLE_NAME + "." + BibliografiaContract.LibroEntry._ID,
            BibliografiaContract.LibroEntry.COLUMN_TITULO
    };

    static final int COL_LIBRO_ID = 0;
    static final int COL_LIBRO_TITULO = 1;

    private LibrosAdapter mLibrosAdapter;
    private Uri mUri;


    public LibrosFragment() {
        // Required empty public constructor
    }

    ImageButton imageAddLibro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(ProyectoFragment.PROYECTO_URI);
        }


        mLibrosAdapter = new LibrosAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_libros, container, false);


        ListView listLibros = (ListView) rootView.findViewById(R.id.listView_libros);
        listLibros.setAdapter(mLibrosAdapter);
        listLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    // Inicialicamos el intent para editar el libro seleccionado.

//                    Intent intent = new Intent(getActivity(), ProyectoActivity.class)
//                            .setData(BibliografiaContract.ProyectoEntry.buildProyectoUri(
//                                    cursor.getInt(COL_PROYECTO_ID)));
//                    startActivity(intent);
                }
            }
        });

        imageAddLibro = (ImageButton) rootView.findViewById(R.id.imageButton_add_libro);
        imageAddLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LibroActivity.class);
                startActivity(intent);
            }
        });

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

//        String sortOrder = BibliografiaContract.LibroEntry.COLUMN_TITULO + " ASC";
//        String idProyecto = BibliografiaContract.getIdProyectoFromUri(mUri);
//
//        Uri librosUri = BibliografiaContract.LibroEntry.buildLibroWithIdProyecto(Long.parseLong(idProyecto));
//        return new CursorLoader(getActivity(),
//                librosUri,
//                LIBRO_COLUMNS,
//                null,
//                null,
//                sortOrder);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLibrosAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLibrosAdapter.swapCursor(null);
    }


}
