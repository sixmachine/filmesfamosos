package br.com.arthursena.filmesfamosos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.model.ReviewDb;
import br.com.arthursena.filmesfamosos.model.VideoDb;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    private Context context;
    private VideoClickListener listener;
    private VideoDb[] videos;

    public VideoAdapter(Context context, VideoClickListener listener, VideoDb[] videos) {
        this.context = context;
        this.listener = listener;
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item, viewGroup, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoDb video = videos[(position)];
        Picasso.with(context).load(String.format(YOUTUBE_THUMBNAIL, video.getKey())).into(holder.ivVideo);
    }

    @Override
    public int getItemCount() {
        return videos.length;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView ivVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVideo = itemView.findViewById(R.id.movie_video_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onVideoClickListener(videos[getAdapterPosition()]);
        }
    }

    public interface VideoClickListener {
        void onVideoClickListener(VideoDb videoDb);
    }
}
