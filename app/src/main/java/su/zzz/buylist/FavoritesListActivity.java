package su.zzz.buylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

public class FavoritesListActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView listFavorites = findViewById(R.id.favorites_list);
        SQLiteOpenHelper buyDatabaseHelper = new BuyDatabaseHelper(this);
        try {
            db = buyDatabaseHelper.getReadableDatabase();
            cursor = db.query("FAVORITES",
                    new String[]{"_id", "NAME", "NEED"},
                    null,
                    null,
                    null,
                    null,
                    null);
            FavoriteAdapter listAdapter = new FavoriteAdapter(this,
                    R.layout.card_favorite,
                    cursor,
                    new String[]{"_id", "NEED"},
                    new int[]{R.id.name, R.id.need},
                    0);
            SimpleCursorAdapter listAdapter2 = new SimpleCursorAdapter(this,
                    R.layout.card_favorite,
                    cursor,
                    new String[]{"NAME", "NEED"},
                    new int[]{R.id.name, R.id.need},
                    0);

            listAdapter2.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    Log.i("setViewValue","_id: "+Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")))+" NAME: "+cursor.getString(cursor.getColumnIndex("NAME"))+" NEED: "+cursor.getString(cursor.getColumnIndex("NEED")));
                    final long c_id = cursor.getLong(cursor.getColumnIndex("_id"));
                    switch (view.getId()) {
                        case R.id.name:
                            ((TextView)view).setText(cursor.getString(columnIndex));
                            return true;
                        case R.id.need:
                            ((CheckBox)view).setChecked(cursor.getInt(columnIndex)>0);
                            ((CheckBox)view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    Log.i("setOnCheckedChange","_id: "+Long.toString(c_id));
                                    if (isChecked){
                                        new UpdateFavoriteTask().execute(c_id, (long) 1);
                                    } else {
                                        new UpdateFavoriteTask().execute(c_id, (long) 0);
                                    }
                                }
                            });
                            return true;
                    }
                    return false;
                        /*
                    if(view.getId() == R.id.need){
                        ((CheckBox)view).setChecked(cursor.getInt(cursor.getColumnIndex("NEED"))>0);
                        CheckBox needCheckBox = (CheckBox)view;

                        needCheckBox.setChecked(cursor.getInt(cursor.getColumnIndex("NEED"))>0);

                        needCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Log.i("setOnCheckedChange","onCheckedChanged "+Long.toString(cursor.getLong(cursor.getColumnIndex("_id"))));
                                //int getPosition = (Integer) buttonView.getTag();
                                //Toast toast = Toast.makeText(view.getContext(), Long.toString(cursor.getLong(cursor.getColumnIndex("NEED"))), Toast.LENGTH_SHORT);
                                //Toast toast = Toast.makeText(view.getContext(), Integer.toString(getPosition), Toast.LENGTH_SHORT);
                                //toast.show();
                            }
                        });
                    }
                    */
                }
            });
            listFavorites.setAdapter(listAdapter2);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Cursor newCursor = db.query("FAVORITES",
                new String[]{"_id", "NAME", "NEED"},
                null,
                null,
                null,
                null,
                null);
        ListView listFavorites = findViewById(R.id.favorites_list);
        CursorAdapter adapter = (CursorAdapter)listFavorites.getAdapter();
        adapter.changeCursor(newCursor);
        cursor = newCursor;
    }

    private class UpdateFavoriteTask extends AsyncTask<Long, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... favorite) {
            Long favoriteId = favorite[0];
            Long favoriteNeed = favorite[1];

            ContentValues favoriteValue = new ContentValues();
            favoriteValue.put("NEED", favoriteNeed);

            Log.i("UpdateFavoriteTask","_id: "+Long.toString(favoriteId));
            SQLiteOpenHelper buyDatabaseHelper = new BuyDatabaseHelper(FavoritesListActivity.this);
            try {
                SQLiteDatabase db = buyDatabaseHelper.getWritableDatabase();
                db.update(BuyDatabaseHelper.TABLE_FAVORITES,
                        favoriteValue,
                        "_id = ?",
                        new String[] {Long.toString(favoriteId)});
                db.close();
                //onRestart();
                return true;
            } catch (Exception e){
                Toast toast = Toast.makeText(FavoritesListActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

    }
}
