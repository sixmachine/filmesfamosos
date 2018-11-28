package br.com.arthursena.filmesfamosos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;

import br.com.arthursena.filmesfamosos.model.MovieDb;

public class MovieAdapter extends AbstractMovieAdapter {


    private final MovieDb[] filmes;

    public MovieAdapter(Context context, MovieDb[] filmes, MovieClickListener listener) {
        super(context,listener);
        this.filmes = filmes;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieDb filme = filmes[(position)];
        holder.tvTitulo.setText(filme.getTitle());
        Picasso.with(context).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(holder.ivPoster);
    }



    @Override
    public int getItemCount() {
        return filmes.length;
    }


    @Override
    MovieDb getFilme(int position) {
        return filmes[position];
    }
}
