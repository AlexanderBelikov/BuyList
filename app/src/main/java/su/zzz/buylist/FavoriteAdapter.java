package su.zzz.buylist;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


public class FavoriteAdapter extends SimpleCursorAdapter {
    public FavoriteAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(final View view, Context context, Cursor cursor) {
        Log.i("FavoriteAdapter","bindView");
        super.bindView(view, context, cursor);
        /*
        final long tempId = cursor.getLong(cursor.getColumnIndex("_id"));
        CheckBox needCheckBox = view.findViewById(R.id.need);
        needCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("setOnCheckedChange","onCheckedChanged");
                //int getPosition = (Integer) buttonView.getTag();
                Toast toast = Toast.makeText(view.getContext(), Long.toString(tempId), Toast.LENGTH_SHORT);
                //Toast toast = Toast.makeText(view.getContext(), Integer.toString(getPosition), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        */
    }
}
