package asc.bcn.alex.ebibgen.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import asc.bcn.alex.ebibgen.data.BibliografiaContract.LibroEntry;
import asc.bcn.alex.ebibgen.data.BibliografiaContract.ProyectoEntry;
import asc.bcn.alex.ebibgen.data.BibliografiaContract.AutorEntry;

public class BibliografiaDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "bibliografia.db";

    public BibliografiaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PROYECTO_TABLE = "CREATE TABLE " + ProyectoEntry.TABLE_NAME + " (" +
                ProyectoEntry._ID + " INTEGER PRIMARY KEY," +
                ProyectoEntry.COLUMN_TITULO + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_LIBRO_TABLE = "CREATE TABLE " + LibroEntry.TABLE_NAME + " (" +
                LibroEntry._ID + " INTEGER PRIMARY KEY," +
                LibroEntry.COLUMN_ID_PROYECTO + " INTEGER NOT NULL, " +
                LibroEntry.COLUMN_ISBN + " TEXT NOT NULL, " +
                LibroEntry.COLUMN_TITULO + " TEXT NOT NULL, " +
                LibroEntry.COLUMN_DIA + " INTEGER, " +
                LibroEntry.COLUMN_MES + " INTEGER, " +
                LibroEntry.COLUMN_ANO + " INTEGER, " +
                LibroEntry.COLUMN_PAIS + " TEXT NOT NULL, " +
                LibroEntry.COLUMN_CIUDAD + " TEXT NOT NULL, " +
                LibroEntry.COLUMN_EDITORIAL + " TEXT NOT NULL, " +
                LibroEntry.COLUMN_PAGINA_INI + " INTEGER, " +
                LibroEntry.COLUMN_PAGINA_FIN + " INTEGER, " +
                LibroEntry.COLUMN_PATH_IMG + " TEXT " +
                " );";

        final String SQL_CREATE_AUTOR_TABLE = "CREATE TABLE " + AutorEntry.TABLE_NAME + " (" +
                AutorEntry._ID + " INTEGER PRIMARY KEY," +
                AutorEntry.COLUMN_ID_LIBRO + " INTEGER NOT NULL, " +
                AutorEntry.COLUMN_NOMBRE + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_PROYECTO_TABLE);
        db.execSQL(SQL_CREATE_LIBRO_TABLE);
        db.execSQL(SQL_CREATE_AUTOR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**
         * NOTA: esto esta de forma provisional aqui hay que controlar cuando se cambia de versión
         * ya que si eliminamos las tablas tambien eliminamos su contenido y no tendremos a los
         * usuarios contentos si eliminamos sus datos.
         */

        db.execSQL("DROP TABLE IF EXISTS " + ProyectoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LibroEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AutorEntry.TABLE_NAME);
        onCreate(db);
    }
}
