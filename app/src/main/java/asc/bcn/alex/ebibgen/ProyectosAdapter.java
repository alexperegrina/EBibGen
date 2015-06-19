package asc.bcn.alex.ebibgen;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProyectosAdapter extends CursorAdapter {
    String LOG_TAG = ProyectosAdapter.class.getSimpleName();
    public ProyectosAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_proyecto, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.list_item_proyecto_textView);
//        Log.e(LOG_TAG, cursor.toString());
//        textView.setText(cursor.);
        String titulo = cursor.getString(ProyectosFragment.COL_PROYECTO_TITULO);
        textView.setText(titulo);
    }
}
