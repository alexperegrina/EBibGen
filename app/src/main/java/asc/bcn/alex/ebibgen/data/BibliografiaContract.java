package asc.bcn.alex.ebibgen.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class BibliografiaContract {

    String LOG_TAG = BibliografiaContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "asc.bcn.alex.ebibgen";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Path para acceder las bd
    public static final String PATH_PROYECTO = "proyecto";
    public static final String PATH_LIBRO = "libro";
    public static final String PATH_AUTOR = "autor";

    // definimos la tabla de Proyecto
    public static final class ProyectoEntry implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROYECTO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROYECTO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROYECTO;

        // Table name
        public static final String TABLE_NAME = "proyecto";

        public static final String COLUMN_TITULO = "titulo";

        public static Uri buildProyectoUri(long id) {
            Uri ret = ContentUris.withAppendedId(CONTENT_URI, id);
//            Log.e("LOG_TAG", ret.toString());
            return ret;
        }
    }

    // definimos la tabla para libro
    public static final class LibroEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIBRO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LIBRO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LIBRO;

        // Table name
        public static final String TABLE_NAME = "libro";

        public static final String COLUMN_ID_PROYECTO= "id_proyecto";
        public static final String COLUMN_ISBN = "isbn";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DIA =  "dia";
        public static final String COLUMN_MES = "mes";
        public static final String COLUMN_ANO = "ano";
        public static final String COLUMN_PAIS = "pais";
        public static final String COLUMN_CIUDAD = "ciudad";
        public static final String COLUMN_EDITORIAL = "editorial";
        public static final String COLUMN_PAGINA_INI = "pagina_ini";
        public static final String COLUMN_PAGINA_FIN = "pagina_fin";
        public static final String COLUMN_PATH_IMG = "path_img";


        public static Uri buildLibroUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLibroWithIdProyecto(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }


        //preguntar por idProyecto
    }

    // definimos la tabla para autores
    public static final class AutorEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AUTOR).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUTOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUTOR;

        // Table name
        public static final String TABLE_NAME = "autor";

        public static final String COLUMN_ID_LIBRO= "id_libro";
        public static final String COLUMN_NOMBRE = "nombre";

        public static Uri buildAutorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //preguntar por idLibro
    }

    public static String getIdProyectoFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }
}
