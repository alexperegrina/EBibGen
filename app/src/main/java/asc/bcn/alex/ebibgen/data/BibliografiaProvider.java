package asc.bcn.alex.ebibgen.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BibliografiaProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BibliografiaDbHelper mOpenHelper;

    static final int PROYECTO = 100;
    static final int PROYECTO_WITH_ID = 101;
    static final int LIBRO = 200;
    static final int LIBRO_WITH_PROYECTO = 201;
    static final int AUTOR = 300;
    static final int AUTOR_WITH_LIBRO = 301;

    //proyecto._id = ?
    private static final String sProyectoId = BibliografiaContract.ProyectoEntry.TABLE_NAME +
            "." + BibliografiaContract.ProyectoEntry._ID + " = ? ";
    //libro.id_proyecto = ?
    private static final String sLibroIdProyecto = BibliografiaContract.LibroEntry.TABLE_NAME +
            "." + BibliografiaContract.LibroEntry.COLUMN_ID_PROYECTO + " = ? ";
    //libro._id = ?
    private static final String sLibroId = BibliografiaContract.LibroEntry.TABLE_NAME +
            "." + BibliografiaContract.LibroEntry._ID + " = ? ";
    //autor.idLibro = ?
    private static final String sAutorIdLibro = BibliografiaContract.AutorEntry.TABLE_NAME +
            "." + BibliografiaContract.AutorEntry.COLUMN_ID_LIBRO + " = ? ";


    // Metodo para añadir al uriMatcher las uris que utilizaremos
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BibliografiaContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BibliografiaContract.PATH_PROYECTO + "/*", PROYECTO_WITH_ID);
        matcher.addURI(authority, BibliografiaContract.PATH_PROYECTO, PROYECTO);
        // libro/idProyecto/idLibro
        matcher.addURI(authority, BibliografiaContract.PATH_LIBRO + "/*/*", LIBRO);
        matcher.addURI(authority, BibliografiaContract.PATH_LIBRO + "/*", LIBRO_WITH_PROYECTO);
        matcher.addURI(authority, BibliografiaContract.PATH_AUTOR, AUTOR);
        matcher.addURI(authority, BibliografiaContract.PATH_AUTOR + "/*", AUTOR_WITH_LIBRO);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        // inicializamos la BD
        mOpenHelper = new BibliografiaDbHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    private Cursor getProyectoWithId(Uri uri, String[] projection, String sortOrder) {
        String idProyecto = BibliografiaContract.getIdProyectoFromUri(uri);
        String[] selectionArgs;
        String selection;
        selection = sProyectoId;
        selectionArgs = new String[]{idProyecto};

        return mOpenHelper.getReadableDatabase().query(
                BibliografiaContract.ProyectoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getLibrosWithIdProyecto(Uri uri, String[] projection, String sortOrder) {
        String idProyecto = BibliografiaContract.getIdProyectoFromUri(uri);
        String[] selectionArgs;
        String selection;
        selection = sLibroIdProyecto;
        selectionArgs = new String[]{idProyecto};

        return mOpenHelper.getReadableDatabase().query(
                BibliografiaContract.LibroEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getLibroWithId(Uri uri, String[] projection, String sortOrder) {
//        String idProyecto = BibliografiaContract.getIdProyectoFromUri(uri);
        String idLibro = BibliografiaContract.getIdLibroFromUri(uri);
//        Log.e("su puta madreeeeee", idLibro);
        String[] selectionArgs;
        String selection;
        selection = sLibroId;
        selectionArgs = new String[]{idLibro};

        return mOpenHelper.getReadableDatabase().query(
                BibliografiaContract.LibroEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case PROYECTO_WITH_ID:
                retCursor = getProyectoWithId(uri,projection,sortOrder);
                break;
            // "proyecto"
            case PROYECTO:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BibliografiaContract.ProyectoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "libro"
            case LIBRO_WITH_PROYECTO:
                Log.e("DEBUGGGGG", "LIBRO_WITH_PRO");
                retCursor = getLibrosWithIdProyecto(uri,projection,sortOrder);
                Log.e("DEBUGGGGG", "LIBRO_WITH_PRO");
                break;
            // "libro/*/*"
            case LIBRO:
                Log.e("DEBUGGGGG", "LIBRO");

                retCursor = getLibroWithId(uri,projection,sortOrder);
                break;
//            // "autor"
//            case AUTOR:
//                break;
//            // "autor/*"
//            case AUTOR_WITH_LIBRO:
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;

        switch (match) {
            // "proyecto"
            case PROYECTO:
                _id = db.insert(BibliografiaContract.ProyectoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BibliografiaContract.ProyectoEntry.buildProyectoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            // "libro"
            case LIBRO_WITH_PROYECTO:
                Log.e("++++++++++++++++","insert");
                _id = db.insert(BibliografiaContract.LibroEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BibliografiaContract.LibroEntry.buildLibroUri(values.getAsInteger(
                            BibliografiaContract.LibroEntry.COLUMN_ID_PROYECTO),_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
//            // "autor"
            case LIBRO:
                Log.e("++++++++++++++++","insert2222");
                _id = db.insert(BibliografiaContract.LibroEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BibliografiaContract.LibroEntry.buildLibroUri(values.getAsInteger(
                            BibliografiaContract.LibroEntry.COLUMN_ID_PROYECTO),_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case LIBRO:
                selection = BibliografiaProvider.sLibroId;
                selectionArgs = new String[]{BibliografiaContract.getIdLibroFromUri(uri)};

                rowsUpdated = db.update(
                        BibliografiaContract.LibroEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PROYECTO_WITH_ID:
                selection = BibliografiaProvider.sProyectoId;
                selectionArgs = new String[]{BibliografiaContract.getIdProyectoFromUri(uri)};
                rowsUpdated = db.update(
                        BibliografiaContract.ProyectoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
//            case LOCATION:
//                rowsUpdated = db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

//        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "-1";
        switch (match) {
            case LIBRO:
//                normalizeDate(values);
                selection = BibliografiaProvider.sLibroId;
                selectionArgs = new String[]{BibliografiaContract.getIdLibroFromUri(uri)};

                rowsDeleted = db.delete(
                        BibliografiaContract.LibroEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case PROYECTO_WITH_ID:
                selection = BibliografiaProvider.sProyectoId;
                selectionArgs = new String[]{BibliografiaContract.getIdProyectoFromUri(uri)};
                rowsDeleted = db.delete(
                        BibliografiaContract.ProyectoEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

//            case WEATHER:
//                rowsDeleted = db.delete(
//                        WeatherContract.WeatherEntry.TABLE_NAME,
//                        selection,
//                        selectionArgs);
//                break;
//            case LOCATION:
//                rowsDeleted = db.delete(
//                        WeatherContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
