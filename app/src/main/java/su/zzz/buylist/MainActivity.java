package su.zzz.buylist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listFavorites = findViewById(R.id.favorites_list);
        /*
        SQLiteOpenHelper buyDatabaseHelper = new BuyDatabaseHelper(this);
        try {
            db = buyDatabaseHelper.getReadableDatabase();
            cursor = db.query("FAVORITES",
                    new String[]{"_id", "NAME"},
                    "NEED=TRUE",
                    null,
                    null,
                    null,
                    null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.card_favorite,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{R.id.name},
                    0);
            listFavorites.setAdapter(listAdapter);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
        */
    }

    public void showProductList(View view) {
        Intent intent = new Intent(this, FavoritesListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor.close();
        //db.close();
    }
}
