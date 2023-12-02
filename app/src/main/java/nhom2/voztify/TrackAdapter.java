package nhom2.voztify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Class.Track;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private Context context;
    private List<Track> tracks;
    private OnItemClickListener onItemClickListener;

    public TrackAdapter(Context context, List<Track> tracks, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.tracks = tracks;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.track_recycler_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (tracks == null || tracks.isEmpty()) {
            return;
        }

        Track track = tracks.get(position);
        holder.songTitle.setText(track.getTitle());

        if (track.getArtist() != null) {
            holder.songArtist.setText(track.getArtist().getName());
        } else {
            holder.songArtist.setText("");
        }

        if (track.getAlbum() != null && track.getAlbum().getCover_medium() != null) {
            Picasso.get()
                    .load(track.getAlbum().getCover_medium())
                    .placeholder(R.drawable.placeholder_img)
                    .into(holder.songImage);
        } else {
            Picasso.get()
                    .load(R.drawable.placeholder_img)
                    .into(holder.songImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songTitle;
        TextView songArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.song_image);
            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);
        }


    }
}
