package br.com.arthursena.filmesfamosos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView tvSinopse;
    private TextView tvAvaliacaoUsuarios;
    private TextView tvDataLancamento;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MovieDb filme = getIntent().getExtras().getParcelable("filme");

        tvSinopse = findViewById(R.id.tv_sinopse);
        tvSinopse.setText(String.format("%s: %s", getString(R.string.sinopse), filme.getOverview()));

        tvDataLancamento = findViewById(R.id.tv_data_lancamento);
        tvDataLancamento.setText(String.format("%s: %s", getString(R.string.data_lancamento), DateUtil.formatarData(filme.getRelease_date())));

        tvAvaliacaoUsuarios = findViewById(R.id.tv_avaliacao_usuarios);
        tvAvaliacaoUsuarios.setText(String.format("%s: %s", getString(R.string.avaliacao_usuarios), Double.toString(filme.getVote_average())));

        imageView = findViewById(R.id.ivPosterDetail);
        Picasso.with(MovieDetailActivity.this).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(imageView);
    }
}