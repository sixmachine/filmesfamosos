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
    private ReviewClickListener listener;

    public ReviewAdapter(Context context, ReviewDb[] reviews,ReviewClickListener listener) {
        this.context = context;
        this.reviews = reviews;
        this.listener = listener;
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

    public interface ReviewClickListener {
        void onReviewClickListener(ReviewDb review);
    }

    @Override
    public int getItemCount() {
        return reviews.length;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView tvAutor;
        protected TextView tvTexto;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.text_movie_review_content);
            tvAutor = itemView.findViewById(R.id.text_movie_review_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onReviewClickListener(reviews[getAdapterPosition()]);
        }
    }
}
