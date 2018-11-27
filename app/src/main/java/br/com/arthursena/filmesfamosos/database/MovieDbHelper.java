package br.com.arthursena.filmesfamosos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie_db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE =
                "create table " + MovieContract.MovieEntry.TABLE_NAME + " ("
                        + MovieContract.MovieEntry._ID + " integer primary key autoincrement, "
                        + MovieContract.MovieEntry.MOVIE_ID + " integer , "
                        + MovieContract.MovieEntry.MOVIE_TITLE + " text "
                        + " ) ; ";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
