package br.com.arthursena.filmesfamosos.adapter;

import android.content.Context;
import android.database.Cursor;

import com.squareup.picasso.Picasso;

import br.com.arthursena.filmesfamosos.database.MovieContract;
import br.com.arthursena.filmesfamosos.model.MovieDb;
import br.com.arthursena.filmesfamosos.util.DateUtil;

public class MovieCursorAdapter extends AbstractMovieAdapter {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MovieCursorAdapter(Context mContext, MovieClickListener listener) {
        super(mContext, listener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieDb filmeDoCursor = getFilmeDoCursor(position);
        holder.tvTitulo.setText(filmeDoCursor.getTitle());
        Picasso.with(context).load(String.format("https://image.tmdb.org/t/p/w500%s", filmeDoCursor.getPoster_path())).into(holder.ivPoster);
    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    @Override
    MovieDb getFilme(int position) {
        return getFilmeDoCursor(position);
    }

    private MovieDb getFilmeDoCursor(int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        int movieId = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID));
        String moviePoster = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_POSTER_PATH));
        String movieTitle = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE));
        String releaseDate = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RELEASE_DATE));
        String overview = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_OVERVIEW));
        double voteAverage = mCursor.getDouble( mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE));

        MovieDb movieDb = new MovieDb();
        movieDb.setPoster_path(moviePoster);
        movieDb.setTitle(movieTitle);
        movieDb.setId(movieId);
        movieDb.setOverview(overview);
        movieDb.setVote_average(voteAverage);
        movieDb.setRelease_date(DateUtil.formatarDataString(releaseDate));
        return movieDb;
    }
}

