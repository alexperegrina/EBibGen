package asc.bcn.alex.ebibgen;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AutoresAdapter extends CursorAdapter {
    String LOG_TAG = AutoresAdapter.class.getSimpleName();

    public AutoresAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_autor, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.list_item_autor_textView);
        String titulo = cursor.getString(LibrosFragment.COL_LIBRO_TITULO);
        textView.setText(titulo);
    }
}
