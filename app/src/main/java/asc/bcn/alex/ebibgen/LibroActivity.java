package asc.bcn.alex.ebibgen;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


public class LibroActivity extends ActionBarActivity {


    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);

        if (savedInstanceState == null) {
            int idLibro = Utility.getPreferredIdLibro(this);
            int idProyecto = Utility.getPreferredIdProyecto(this);


            if(idLibro == -1) {

                //insertar un nuevo libro y recojer el id y crear la uri
                ContentValues libroValues = new ContentValues();

                libroValues.put(BibliografiaContract.LibroEntry.COLUMN_ID_PROYECTO, idProyecto);

                Uri newUri = BibliografiaContract.LibroEntry.buildLibroWithIdProyecto(idProyecto);

                mUri = this.getContentResolver().insert(
                        newUri,
                        libroValues
                );

                int idL = Integer.parseInt(BibliografiaContract.getIdLibroFromUri(mUri));
                Utility.setPreferredIdLibro(this,idL);

            }
            else {
                mUri = BibliografiaContract.LibroEntry.buildLibroUri(idProyecto,idLibro);
            }


            Bundle arguments = new Bundle();
            arguments.putParcelable(LibroFragment.PROYECTO_URI, mUri);

            LibroFragment fragment = new LibroFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_libro, fragment)
                    .commit();

            Bundle arguments2 = new Bundle();
            arguments2.putParcelable(AutoresFragment.PROYECTO_URI, mUri);

            AutoresFragment fragment2 = new AutoresFragment();
            fragment2.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_autores, fragment2)
                    .commit();

        }
    }
}
