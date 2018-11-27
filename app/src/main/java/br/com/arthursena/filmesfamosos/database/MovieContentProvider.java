package br.com.arthursena.filmesfamosos.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static br.com.arthursena.filmesfamosos.database.MovieContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;

    private MovieDbHelper movieDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case MOVIE:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Não implementado");
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int movieDeleted;

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                movieDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Não implementado");
    }
}
