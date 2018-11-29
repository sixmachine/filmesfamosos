package br.com.arthursena.filmesfamosos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.model.ReviewDb;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private final ReviewDb[] reviews;

    public ReviewAdapter(Context context, ReviewDb[] reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, viewGroup, false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewDb review = reviews[(position)];
        holder.tvAutor.setText(review.getAuthor());
        holder.tvTexto.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.length;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvAutor;
        protected TextView tvTexto;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.text_movie_review_content);
            tvAutor = itemView.findViewById(R.id.text_movie_review_author);
        }
    }
}
