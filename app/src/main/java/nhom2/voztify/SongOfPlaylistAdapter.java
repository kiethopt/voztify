package nhom2.voztify;

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

import nhom2.voztify.Class.SongForU;

public class SongOfPlaylistAdapter extends RecyclerView.Adapter<SongOfPlaylistAdapter.ViewHolder> {
    private List<SongForU> songList;
    private Context context;

    public SongOfPlaylistAdapter(Context context, List<SongForU> songList) {
        this.context = context;
        this.songList = songList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_song_playlist_item, parent, false);
        return new SongOfPlaylistAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongOfPlaylistAdapter.ViewHolder holder, int position) {
        SongForU songForU = songList.get(position);

        holder.titleTextView.setText(songForU.getTitle());
        holder.textViewArtist.setText(songForU.getArtist());

        // Load the image into the ImageView using Picasso (you need to add the Picasso library to your dependencies)
        String imgUrl =  "https://e-cdns-images.dzcdn.net/images/cover" + "/" + songForU.getImageUrl() + "/120x120-000000-80-0-0.jpg";
        Picasso.get().load(imgUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView textViewArtist;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            imageView = itemView.findViewById(R.id.imageViewCover);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
        }
    }
}
