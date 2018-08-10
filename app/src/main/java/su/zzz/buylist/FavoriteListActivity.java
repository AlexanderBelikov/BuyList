package su.zzz.buylist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FavoriteListActivity extends AppCompatActivity {
    private ArrayList<Favorite> favoriteArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoadFavoriteArray();
        FavoriteArrayAdapter favoriteListAdapter = new FavoriteArrayAdapter(this, favoriteArray);
        ListView favoriteList = findViewById(R.id.favorite_list);
        favoriteList.setAdapter(favoriteListAdapter);
        favoriteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    private void LoadFavoriteArray(){
        SQLiteOpenHelper buyDatabaseHelper = new BuyDatabaseHelper(this);
        try {
            SQLiteDatabase db = buyDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(BuyDatabaseHelper.TABLE_FAVORITES,
                    new String[]{"_id", "NAME", "NEED"},
                    null,
                    null,
                    null,
                    null,
                    null);
            favoriteArray.clear();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Favorite f = new Favorite(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("NAME")), (cursor.getLong(cursor.getColumnIndex("NEED"))>0));
                favoriteArray.add(f);
                Log.i("LoadFavoriteArray","_id: "+f.getId()+" NAME: "+f.getName()+" NEED: "+f.getNeed());

            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "LoadFavoriteArray exception: "+e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
