package br.com.arthursena.filmesfamosos.activity;

import android.content.ContentValues;
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
import br.com.arthursena.filmesfamosos.adapter.AbstractMovieAdapter;
import br.com.arthursena.filmesfamosos.adapter.MovieAdapter;
import br.com.arthursena.filmesfamosos.adapter.ReviewAdapter;
import br.com.arthursena.filmesfamosos.database.MovieContract;
import br.com.arthursena.filmesfamosos.model.MovieDb;
import br.com.arthursena.filmesfamosos.model.MovieDbResponse;
import br.com.arthursena.filmesfamosos.model.ReviewDb;
import br.com.arthursena.filmesfamosos.model.ReviewDbResponse;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewAdapter.ReviewClickListener {

    private ImageView imageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private RecyclerView rvReviews;

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

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        Picasso.with(MovieDetailsActivity.this).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(imageView);

        collapsingToolbarLayout.setTitle(filme.getTitle());
        fab = findViewById(R.id.fab);

        TextView tvOverview = findViewById(R.id.tvOverview);
        tvOverview.setText(filme.getOverview());

        mudarImagemFavorito(isFilmeFavoritado(Integer.toString(filme.getId())));

        String link = String.format("http://api.themoviedb.org/3/movie/%s/reviews?api_key=%s", filme.getId(), BuildConfig.API_KEY);

        try {
            new ReviewDbAsyncTask().execute(new URL(link));
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
}
