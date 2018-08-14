package su.zzz.buylist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Favorite> fArrayList = new ArrayList<>();
    public static ArrayList<Long> checkedArrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoadFavoriteArray();
        ListView favoriteList = findViewById(R.id.favorite_list);
        /*
        ArrayAdapter<Favorite> favoriteArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice,
                favoriteArray);
        */
        FavoriteArrayAdapter fArrayAdapter = new FavoriteArrayAdapter(this, fArrayList);
        /*
        ArrayAdapter<Favorite> favoriteArrayAdapter = new ArrayAdapter<>(this,
                R.layout.card_favorite,
                R.id.name,
                favoriteArray);
        */
        favoriteList.setAdapter(fArrayAdapter);
        favoriteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    public void showProductList(View view) {
        Intent intent = new Intent(this, FavoriteListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void LoadFavoriteArray(){
        SQLiteOpenHelper buyDatabaseHelper = new BuyDatabaseHelper(this);
        try {
            SQLiteDatabase db = buyDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(BuyDatabaseHelper.TABLE_FAVORITES,
                    new String[]{"_id", "NAME", "NEED"},
                    "NEED>0",
                    null,
                    null,
                    null,
                    null);
            fArrayList.clear();

            ArrayList<Long> _checkedArrayList = new ArrayList();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Favorite f = new Favorite(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("NAME")), (cursor.getLong(cursor.getColumnIndex("NEED"))>0));
                fArrayList.add(f);
                _checkedArrayList.add(f.getId());
                Log.i("LoadFavoriteArray","_id: "+f.getId()+" NAME: "+f.getName()+" NEED: "+f.getNeed());
            }
            Log.i("checked SIZE 1: ",Integer.toString(checkedArrayList.size()));
            for (Long id : new ArrayList<>(checkedArrayList)){
                if (!_checkedArrayList.contains(id)){
                    checkedArrayList.remove(id);
                }
            }
            Log.i("checked SIZE 2: ",Integer.toString(checkedArrayList.size()));

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "LoadFavoriteArray exception: "+e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoadFavoriteArray();
        ListView favoriteList = findViewById(R.id.favorite_list);
        ArrayAdapter<Favorite> favoriteArrayAdapter = (ArrayAdapter)favoriteList.getAdapter();
        favoriteArrayAdapter.notifyDataSetChanged();
    }
    private void EditMode(Boolean On){
        FloatingActionButton addFAB = findViewById(R.id.addFAB);
        FloatingActionButton okFAB = findViewById(R.id.okFAB);
        if (On){
            addFAB.show();
            okFAB.hide();
        } else {
            okFAB.show();
            addFAB.hide();
        }
    }

    private class FavoriteArrayAdapter extends ArrayAdapter<Favorite> {
        private Context mContext;
        private ArrayList<Favorite> fArrayList;

        public FavoriteArrayAdapter(Context context, ArrayList<Favorite> favoriteArrayList){
            super(context, 0, favoriteArrayList);
            this.mContext = context;
            this.fArrayList = favoriteArrayList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            Favorite f =  fArrayList.get(position);
            Log.i("getView", "pos: "+Integer.toString(position) + ", id:"+f.getId()+", name: "+f.getName());
            if (itemView == null) {
                itemView = LayoutInflater.from(mContext).inflate(R.layout.card_favorite, parent, false);
            }
            TextView nameView = itemView.findViewById(R.id.name);
            nameView.setText(f.getName());
            CheckBox needView = itemView.findViewById(R.id.need);
            final String fName = f.getName();
            final Long fId = f.getId();
            if(checkedArrayList.contains(fId)){
                needView.setOnCheckedChangeListener(null);
                needView.setChecked(true);
            }
            needView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Log.i("Checked + ", Long.toString(fId));
                        checkedArrayList.add(fId);
                    } else {
                        Log.i("Checked - ", Long.toString(fId));
                        checkedArrayList.remove(fId);
                    }
                    EditMode(checkedArrayList.size()==0);
                    Log.i("SIZE: ", Integer.toString(checkedArrayList.size()));
                }
            });

            return itemView;
        }
    }
}
