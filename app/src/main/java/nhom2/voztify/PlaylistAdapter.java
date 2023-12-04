package nhom2.voztify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nhom2.voztify.Class.Playlist;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    ImageButton playlistImageButton;
    TextView playlistNameTextView;
    TextView yourNameTextView;
    int imageResourceId;
    private List<Playlist> playlists;
    private Context context;

    public PlaylistAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_recycle_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.playlistImageButton.setImageResource(playlist.getImageResource());
        holder.playlistNameTextView.setText(playlist.getPlaylistName());
        holder.yourNameTextView.setText(playlist.getYourName());

        holder.playlistImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clicked playlist
                int position = holder.getAdapterPosition();
                Playlist clickedPlaylist = playlists.get(position);

                // Start PlaylistDetailActivity and pass the playlist data
                Intent intent = new Intent(context, PlaylistDetailActivity.class);
                intent.putExtra("playlistId", clickedPlaylist.getId());
                intent.putExtra("playlistName", clickedPlaylist.getPlaylistName());
                intent.putExtra("yourName", clickedPlaylist.getYourName());
                intent.putExtra("imageResourceId", clickedPlaylist.getImageResource());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageButton playlistImageButton;
        TextView playlistNameTextView;
        TextView yourNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistImageButton = itemView.findViewById(R.id.imageButton_playlist);
            playlistNameTextView = itemView.findViewById(R.id.tv_playlist_name);
            yourNameTextView = itemView.findViewById(R.id.tv_your_name);
            // Set click listener for the ImageButton
            playlistImageButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageButton_playlist) {
                // Get the clicked playlist
                int position = getAdapterPosition();
                Playlist clickedPlaylist = playlists.get(position);

                // Start PlaylistDetailActivity and pass the playlist data
                Intent intent = new Intent(context, PlaylistDetailActivity.class);

//                intent.putExtra("playlistId", clickedPlaylist.getId()); // Pass playlist ID or other data
                intent.putExtra("playlistName", clickedPlaylist.getPlaylistName());
                intent.putExtra("yourName", clickedPlaylist.getYourName());
                intent.putExtra("imageResourceId", clickedPlaylist.getImageResource());  // Pass the image resource ID

                // Add more data as needed
                context.startActivity(intent);
            }
        }
    }
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        notifyDataSetChanged();
    }

}
