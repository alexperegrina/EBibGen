package asc.bcn.alex.ebibgen;

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
        return rootView;
//        return inflater.inflate(R.layout.fragment_proyecto, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_proyecto, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PROYECTO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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

        if (data != null && data.moveToFirst()) {
            mTitulo.setText(data.getString(COL_PROYECTO_TITULO));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
