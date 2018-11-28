package br.com.arthursena.filmesfamosos.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "br.com.arthursena.filmesfamosos";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "movies_id";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String MOVIE_OVERVIEW = "movie_overview";
        public static final String MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String MOVIE_VOTE_AVERAGE = "movie_vote_average";

    }

}
