package br.com.arthursena.filmesfamosos.database;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "movies_id";
        public static final String MOVIE_ADULT = "movie_adult";
        public static final String MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String MOVIE_OVERVIEW = "movie_overview";
        public static final String MOVIE_GENRES = "movie_genres";
        public static final String MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_ORIGINAL_TITLE = "movie_original_title";
        public static final String MOVIE_LANGUAGE = "movie_language";
        public static final String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
        public static final String MOVIE_POPULARITY = "movie_popularity";
        public static final String MOVIE_VIDEO = "movie_video";
        public static final String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String MOVIE_VOTE_COUNT = "movie_vote_count";

    }

}
