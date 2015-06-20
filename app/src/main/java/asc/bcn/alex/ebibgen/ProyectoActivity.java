package asc.bcn.alex.ebibgen;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import asc.bcn.alex.ebibgen.data.BibliografiaContract;


public class ProyectoActivity extends ActionBarActivity {
    private static final String LOG_TAG = ProyectoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);



        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            int idProyecto = Utility.getPreferredIdProyecto(this);
            Uri mUri = BibliografiaContract.ProyectoEntry.buildProyectoUri(idProyecto);

            Bundle arguments = new Bundle();
//            arguments.putParcelable(ProyectoFragment.PROYECTO_URI, getIntent().getData());
            arguments.putParcelable(ProyectoFragment.PROYECTO_URI, mUri);

            ProyectoFragment fragment = new ProyectoFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_proyecto, fragment)
                    .commit();

            Bundle arguments2 = new Bundle();
//            arguments2.putParcelable(LibrosFragment.PROYECTO_URI, getIntent().getData());
            arguments2.putParcelable(LibrosFragment.PROYECTO_URI, mUri);

            LibrosFragment fragment2 = new LibrosFragment();
            fragment2.setArguments(arguments2);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_libros, fragment2)
                    .commit();

        }
    }
}
