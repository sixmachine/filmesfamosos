package br.com.arthursena.filmesfamosos.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.database.MovieContract;
import br.com.arthursena.filmesfamosos.model.MovieDb;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MovieDb filme = getIntent().getExtras().getParcelable("filme");

        imageView = findViewById(R.id.movieDetailCover);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        Picasso.with(MovieDetailsActivity.this).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(imageView);

        collapsingToolbarLayout.setTitle(filme.getTitle());
        fab = findViewById(R.id.fab);
        mudarImagemFavorito(isFilmeFavoritado(Integer.toString(filme.getId())));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensagem;

                boolean filmeFavoritado = isFilmeFavoritado(Integer.toString(filme.getId()));

                if(!filmeFavoritado){
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,filme.toContentValues());
                    mensagem = "Adicionado aos favoritos";
                    mudarImagemFavorito(true);
                }
                else {
                    getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(filme.getId())).build(),null,null);
                    mensagem = "Removido dos favoritos";
                    mudarImagemFavorito(false);
                }
                Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private boolean isFilmeFavoritado(String id){
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "movies_id=?", new String[]{id}, null);
        return cursor != null && cursor.getCount() > 0;
    }

    private void mudarImagemFavorito(boolean filmeFavoritado){
        if(filmeFavoritado){
            fab.setImageResource(R.drawable.ic_baseline_favorite_24);
            return;
        }
        fab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }
}
