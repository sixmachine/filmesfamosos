package br.com.arthursena.filmesfamosos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.model.MovieDb;
import br.com.arthursena.filmesfamosos.model.MovieDbResponse;

public abstract class AbstractMovieAdapter extends RecyclerView.Adapter<AbstractMovieAdapter.MovieViewHolder> {


    Context context;
    MovieClickListener listener;

    public AbstractMovieAdapter(Context context, MovieClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, viewGroup, false);
        MovieViewHolder holder = new MovieViewHolder(view);
        return holder;
    }


    public interface MovieClickListener {
        void onClickFilme(MovieDb filme);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView ivPoster;
        protected TextView tvTitulo;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClickFilme(getFilme(getAdapterPosition()));
        }
    }

    abstract MovieDb getFilme(int position);

}
