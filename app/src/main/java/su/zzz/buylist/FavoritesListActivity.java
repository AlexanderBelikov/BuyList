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

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView fListView = findViewById(R.id.favorite_list);
        SQLiteOpenHelper buyDatabaseHelper = new BuyDatabaseHelper(this);
        try {
            db = buyDatabaseHelper.getWritableDatabase();
            cursor = db.query(BuyDatabaseHelper.TABLE_FAVORITES,
                    new String[]{"_id", "NAME", "NEED"},
                    null,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        SimpleCursorAdapter fSimpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.card_favorite,
                cursor,
                new String[]{"NAME", "NEED"},
                new int[]{R.id.name, R.id.need},
                0);
        fSimpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.name:
                        Log.i("setViewValue","NAME: "+cursor.getString(columnIndex));
                        TextView nameTextView = (TextView)view;
                        nameTextView.setText(cursor.getString(columnIndex));
                        return true;
                    case R.id.need:
                        CheckBox needCheckBox = (CheckBox)view;
                        needCheckBox.setOnCheckedChangeListener(null);
                        needCheckBox.setChecked(cursor.getInt(columnIndex)>0);
                        final String fName = cursor.getString(cursor.getColumnIndex("NAME"));
                        final Long fId = cursor.getLong(cursor.getColumnIndex("_id"));
                        needCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Log.i("Checked","NAME: "+fName+", isChecked: "+Boolean.toString(isChecked));
                                ContentValues fValue = new ContentValues();
                                fValue.put("NEED", isChecked);
                                try {
                                    db.update(BuyDatabaseHelper.TABLE_FAVORITES,
                                            fValue,
                                            "_id = ?",
                                            new String[] {Long.toString(fId)});
                                } catch (Exception e) {
                                    Toast toast = Toast.makeText(FavoritesListActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });
                        return true;
                }
                return false;
            }
        });
        fListView.setAdapter(fSimpleCursorAdapter);
        fListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
