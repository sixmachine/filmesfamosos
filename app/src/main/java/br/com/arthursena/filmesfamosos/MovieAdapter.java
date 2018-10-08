package br.com.arthursena.filmesfamosos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private final Context context;
    private final MovieDbResponse.MovieDb[] filmes;
    private FilmeOnClickListener listener;

    public MovieAdapter(Context context, MovieDbResponse.MovieDb[] filmes, FilmeOnClickListener listener) {
        this.context = context;
        this.filmes = filmes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, viewGroup, false);
        MovieViewHolder holder = new MovieViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieDbResponse.MovieDb filme = filmes[(position)];
        holder.tvTitulo.setText(filme.getTitle());
        Picasso.with(context).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return filmes.length;
    }

    public interface FilmeOnClickListener {
        void onClickFilme(View view, int indice);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPoster;
        private TextView tvTitulo;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
