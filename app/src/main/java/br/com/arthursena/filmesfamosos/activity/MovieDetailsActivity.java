package br.com.arthursena.filmesfamosos.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.arthursena.filmesfamosos.BuildConfig;
import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.adapter.ReviewAdapter;
import br.com.arthursena.filmesfamosos.adapter.VideoAdapter;
import br.com.arthursena.filmesfamosos.database.MovieContract;
import br.com.arthursena.filmesfamosos.model.MovieDb;
import br.com.arthursena.filmesfamosos.model.ReviewDb;
import br.com.arthursena.filmesfamosos.model.ReviewDbResponse;
import br.com.arthursena.filmesfamosos.model.VideoDb;
import br.com.arthursena.filmesfamosos.model.VideoDbResponse;
import br.com.arthursena.filmesfamosos.util.DateUtil;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewAdapter.ReviewClickListener, VideoAdapter.VideoClickListener {

    private static final String YOUTUBE_LINK = "http://youtube.com/watch?v=%s";

    private ImageView imageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private RecyclerView rvReviews;
    private RecyclerView rvVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MovieDb filme = getIntent().getExtras().getParcelable("filme");

        imageView = findViewById(R.id.movieDetailCover);
        rvReviews = findViewById(R.id.reviews);
        rvVideos = findViewById(R.id.videos);

        ImageView ivPosterMovie = findViewById(R.id.ivPosterMovie);
        Picasso.with(MovieDetailsActivity.this).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(ivPosterMovie);

        TextView tvVoteAverage = findViewById(R.id.vote_average);
        tvVoteAverage.setText(Double.toString(filme.getVote_average()));

        TextView tvReleaseDate = findViewById(R.id.release_date);
        tvReleaseDate.setText(String.format("Release Date: %s",DateUtil.formatarData(filme.getRelease_date())));

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        Picasso.with(MovieDetailsActivity.this).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getBackdrop_path())).into(imageView);

        collapsingToolbarLayout.setTitle(filme.getTitle());
        fab = findViewById(R.id.fab);

        TextView tvOverview = findViewById(R.id.tvOverview);
        tvOverview.setText(filme.getOverview());

        mudarImagemFavorito(isFilmeFavoritado(Integer.toString(filme.getId())));

        String linkReview = String.format("http://api.themoviedb.org/3/movie/%s/reviews?api_key=%s", filme.getId(), BuildConfig.API_KEY);
        String linkVideo = String.format("http://api.themoviedb.org/3/movie/%s/videos?api_key=%s", filme.getId(), BuildConfig.API_KEY);

        try {
            new ReviewDbAsyncTask().execute(new URL(linkReview));
            new VideoDbAsyncTask().execute(new URL(linkVideo));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensagem;

                boolean filmeFavoritado = isFilmeFavoritado(Integer.toString(filme.getId()));

                if (!filmeFavoritado) {
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, filme.toContentValues());
                    mensagem = "Adicionado aos favoritos";
                    mudarImagemFavorito(true);
                } else {
                    getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(filme.getId())).build(), null, null);
                    mensagem = "Removido dos favoritos";
                    mudarImagemFavorito(false);
                }
                Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private boolean isFilmeFavoritado(String id) {
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "movies_id=?", new String[]{id}, null);
        return cursor != null && cursor.getCount() > 0;
    }

    private void mudarImagemFavorito(boolean filmeFavoritado) {
        if (filmeFavoritado) {
            fab.setImageResource(R.drawable.ic_baseline_favorite_24);
            return;
        }
        fab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    @Override
    public void onReviewClickListener(ReviewDb review) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onVideoClickListener(VideoDb videoDb) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(YOUTUBE_LINK,videoDb.getKey())));
        startActivity(browserIntent);
    }

    public class ReviewDbAsyncTask extends AsyncTask<URL, Void, ReviewDbResponse> {

        @Override
        protected ReviewDbResponse doInBackground(URL... urls) {

            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(urls[0].openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Gson().fromJson(reader, ReviewDbResponse.class);
        }

        @Override
        protected void onPostExecute(ReviewDbResponse response) {
            ReviewAdapter adapter = new ReviewAdapter(MovieDetailsActivity.this, response.getResults(), MovieDetailsActivity.this);
            rvReviews.setAdapter(adapter);
            super.onPostExecute(response);
        }
    }

    public class VideoDbAsyncTask extends AsyncTask<URL, Void, VideoDbResponse> {

        @Override
        protected VideoDbResponse doInBackground(URL... urls) {

            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(urls[0].openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Gson().fromJson(reader, VideoDbResponse.class);
        }

        @Override
        protected void onPostExecute(VideoDbResponse response) {
            VideoAdapter adapter = new VideoAdapter(MovieDetailsActivity.this,MovieDetailsActivity.this, response.getResults());
            rvVideos.setAdapter(adapter);
            super.onPostExecute(response);
        }
    }
}
