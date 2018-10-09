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
    private final MovieDb[] filmes;
    private MovieClickListener listener;

    public MovieAdapter(Context context, MovieDb[] filmes, MovieClickListener listener) {
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
        MovieDb filme = filmes[(position)];
        holder.tvTitulo.setText(filme.getTitle());
        Picasso.with(context).load(String.format("https://image.tmdb.org/t/p/w500%s", filme.getPoster_path())).into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return filmes.length;
    }

    public interface MovieClickListener {
        void onClickFilme(MovieDb filme);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPoster;
        private TextView tvTitulo;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClickFilme(filmes[getAdapterPosition()]);
        }
    }

}
