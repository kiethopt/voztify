package nhom2.voztify.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nhom2.voztify.Model.Song;
import nhom2.voztify.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> songs;
    private Context context;
    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameTextView;
        private TextView artistTextView;
        private ImageButton addButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.tv_song);
            artistTextView = itemView.findViewById(R.id.tv_artist);
            addButton = itemView.findViewById(R.id.imageButton);
        }
        public void bind(Song song) {
            songNameTextView.setText(song.getName());
            artistTextView.setText(song.getArtist());

            // Handle add button click if needed
            addButton.setOnClickListener(v -> {
                // Handle adding the song to the playlist
            });
        }
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    public void updateSongs(List<Song> newSongs) {
        songs.clear();
        songs.addAll(newSongs);
        notifyDataSetChanged();
    }
}
