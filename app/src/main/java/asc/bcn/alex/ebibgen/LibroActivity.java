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


                // Now that the content provider is set up, inserting rows of data is pretty simple.
                // First create a ContentValues object to hold the data you want to insert.
                ContentValues libroValues = new ContentValues();

                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.
//                locationValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, cityName);
//                locationValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
//                locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, lat);
//                locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, lon);

//                libroValues.put(BibliografiaContract.LibroEntry.COLUMN_TITULO,"tituloooo");
                libroValues.put(BibliografiaContract.LibroEntry.COLUMN_ID_PROYECTO,idProyecto);

                Uri newUri = BibliografiaContract.LibroEntry.buildLibroWithIdProyecto(idProyecto);

                // Finally, insert location data into the database.
                mUri = this.getContentResolver().insert(
                        newUri,
                        libroValues
                );
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


        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_libro2, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_eliminar) {
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
