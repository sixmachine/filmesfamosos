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

        toolbar.setTitle(filme.getTitle());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensagem;

                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "movies_id=?", new String[]{Integer.toString(filme.getId())}, null);
                if(cursor != null && cursor.getCount() == 0){
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.MOVIE_ID,filme.getId());
                    cv.put(MovieContract.MovieEntry.MOVIE_TITLE,filme.getTitle());
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,cv);
                    mensagem = "Adicionado aos favoritos";
                }
                else {
                    getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(filme.getId())).build(),null,null);
                    mensagem = "Removido dos favoritos";
                }
                Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
