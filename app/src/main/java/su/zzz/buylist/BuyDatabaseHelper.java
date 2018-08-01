package su.zzz.buylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "buylist";
    private static final int DB_VERSION = 1;

    BuyDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FAVORITES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "NEED NUMERIC, "
                + "IMAGE_RESOURCE_ID INTEGER); ");
        insertFavorite(db, "Молоко");
        insertFavorite(db, "Хлеб");
        insertFavorite(db, "Яйца");
        insertFavorite(db, "Мороженое");
        insertFavorite(db, "Творог");
        insertFavorite(db, "Пакеты для мусора");
        insertFavorite(db, "Соль");
        insertFavorite(db, "Сахар");
        insertFavorite(db, "Чай зеленый");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void insertFavorite(SQLiteDatabase db, String name) {
        ContentValues favoriteValue = new ContentValues();
        favoriteValue.put("NAME", name);
        db.insert("FAVORITES", null, favoriteValue);

    }
}
