package br.com.arthursena.filmesfamosos.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.adapter.AbstractMovieAdapter;
import br.com.arthursena.filmesfamosos.adapter.MovieAdapter;
import br.com.arthursena.filmesfamosos.model.MovieDb;
import br.com.arthursena.filmesfamosos.model.MovieDbResponse;
import br.com.arthursena.filmesfamosos.model.ReviewDbResponse;
import br.com.arthursena.filmesfamosos.util.DateUtil;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView tvSinopse;
    private TextView tvAvaliacaoUsuarios;
    private TextView tvDataLancamento;
    private TextView tvTitulo;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MovieDb filme = getIntent().getExtras().getParcelable("filme");

        tvSinopse = findViewById(R.id.tv_sinopse);
        tvSinopse.setText(String.format("%s: %s", getString(R.string.sinopse), filme.getOverview()));

        tvTitulo = findViewById(R.id.tv_titulo_detalhe);
        tvTitulo.setText(String.format("%s: %s", getString(R.string.titulo), filme.getTitle()));

        tvDataLancamento = findViewById(R.id.tv_data_lancamento);
        tvDataLancamento.setText(String.format("%s: %s", getString(R.string.data_lancamento), DateUtil.formatarData(filme.getRelease_date())));

        tvAvaliacaoUsuarios = findViewById(R.id.tv_avaliacao_usuarios);
        tvAvaliacaoUsuarios.setText(String.format("%s: %s", getString(R.string.avaliacao_usuarios), Double.toString(filme.getVote_average())));

        imageView = findViewById(R.id.ivPosterDetail);
        Picasso.with(MovieDetailActivity.this).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(imageView);
    }
}
