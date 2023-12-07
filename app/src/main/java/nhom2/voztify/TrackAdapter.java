package nhom2.voztify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Class.History;
import nhom2.voztify.Class.Track;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private Context context;
    private List<Track> tracks;
    private OnItemClickListener onItemClickListener;
    private static int backgroundColor = Color.parseColor("#040D12");
    private static int textGravity = Gravity.START;
    private static float textSize = 20;
    private static int songArtistVisibility = View.VISIBLE;

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
    public void update(List<Track> newTracks) {
        this.tracks.clear();
        this.tracks.addAll(newTracks);
        notifyDataSetChanged();
    }
    public void updatePlaylist(List<Track> newTracks) {
        this.tracks = newTracks;
        notifyDataSetChanged();
    }
    public void addTrack(Track track) {
        // Add the track to your list
        tracks.add(track);
        // Notify the adapter about the data change
        notifyDataSetChanged();
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

        holder.songTitle.setGravity(textGravity);
        holder.itemView.setBackgroundColor(backgroundColor);
        holder.songTitle.setTextSize(textSize);
        holder.songArtist.setVisibility(songArtistVisibility);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
    public static void savePreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("background_color", "#" + Integer.toHexString(backgroundColor & 0x00FFFFFF)); // Save as a hexadecimal string
        editor.putFloat("text_size", textSize);
        editor.putInt("text_gravity", textGravity);
        editor.putInt("song_artist_visibility", songArtistVisibility);
        editor.apply();
    }

    public static void updateBackground(Context context, String colorHexCode) {
        backgroundColor = Color.parseColor(colorHexCode);
        savePreferences(context);
    }
    public static void updateTextSize(Context context, float size) {
        textSize = size;
        savePreferences(context);
    }
    public static void updateSongArtistVisibility(Context context, int visibility) {
        songArtistVisibility = visibility;
        savePreferences(context);
    }
    public static void updateTextGravity(Context context, int gravity) {
        textGravity = gravity;
        savePreferences(context);
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
