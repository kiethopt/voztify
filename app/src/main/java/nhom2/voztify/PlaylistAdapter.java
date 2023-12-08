package nhom2.voztify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistImageButton = itemView.findViewById(R.id.imageButton_playlist);
            playlistNameTextView = itemView.findViewById(R.id.tv_playlist_name);
            yourNameTextView = itemView.findViewById(R.id.tv_your_name);

            playlistImageButton.setOnClickListener(v -> {
                // Get the clicked playlist
                int position = getAdapterPosition();
                Playlist clickedPlaylist = playlists.get(position);

                // Start PlaylistDetailActivity and pass the playlist data
                Intent intent = new Intent(context, PlaylistDetailActivity.class);
                intent.putExtra("playlistName", clickedPlaylist.getPlaylistName());
                intent.putExtra("yourName", clickedPlaylist.getYourName());
                intent.putExtra("imageResourceId", clickedPlaylist.getImageResource());
                context.startActivity(intent);
            });
//            deleteButton.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    String playlistId = playlists.get(position).getId();
//                    // Hiển thị dialog xác nhận xóa
//                    showDeleteDialog(playlistId);
//                }
//            });

        }
//        public void removePlaylist(int position) {
//            Playlist removedPlaylist = playlists.remove(position);
//            notifyItemRemoved(position);
//            if (removedPlaylist != null && removedPlaylist.getId() != null && !removedPlaylist.getId().isEmpty()) {
//                removeFromFirebase(removedPlaylist.getId());
//            } else {
//                // Handle the case where the playlistId is null or empty
//                // You can log an error, show a message, or take appropriate action
//            }
//
//            // Remove the playlist from Firebase
//            removeFromFirebase(removedPlaylist.getId());
//        }

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
    private void removeFromFirebase(String playlistId) {
        // Implement logic to remove the playlist from Firebase
        // Use the provided playlistId to delete the corresponding data
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("playlists").child(playlistId);

            userPlaylistRef.removeValue();
        }
    }
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        notifyDataSetChanged();
    }
    private void showDeleteDialog(String playlistId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa playlist này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeFromFirebase(playlistId);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
