package nhom2.voztify.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import nhom2.voztify.Model.Track;
import nhom2.voztify.R;

public class SongOfPlaylistAdapter extends RecyclerView.Adapter<SongOfPlaylistAdapter.ViewHolder> {
    private List<Track> songList;
    private Context context;

    public SongOfPlaylistAdapter(Context context, List<Track> songList) {
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
        Track songForU = songList.get(position);

        holder.titleTextView.setText(songForU.getTitle());
        holder.textViewArtist.setText(songForU.getArtist().getName());

        // Load the image into the ImageView using Picasso (you need to add the Picasso library to your dependencies)
        String imgUrl =  "https://e-cdns-images.dzcdn.net/images/cover" + "/" + songForU.getMd5_image() + "/120x120-000000-80-0-0.jpg";
        Picasso.get().load(imgUrl).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();

                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("Track", songForU);
                intent.putExtra("TracksList", (Serializable) songList);
                context.startActivity(intent);
            }
        });
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
