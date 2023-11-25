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

import nhom2.voztify.Class.Track;


public class SearchTrackAdapter extends RecyclerView.Adapter<SearchTrackAdapter.ViewHolder> {

    private Context context;
    private List<Track> tracks;

    public SearchTrackAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.track_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.trackTitle.setText(track.getTitle());
        holder.trackArtistName.setText(track.getArtist().getName());

        if (track != null && track.getMd5_image() != null) {
            String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/" + track.getMd5_image() + "/250x250-000000-80-0-0.jpg";

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_img)
                    .into(holder.trackImage);


        } else {
            holder.trackImage.setImageResource(R.drawable.silver);
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void updateTracks(List<Track> newTracks) {
        this.tracks = newTracks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView trackImage;
        TextView trackTitle;
        TextView trackArtistName;

        public ViewHolder(View itemView) {
            super(itemView);
            trackImage = itemView.findViewById(R.id.track_image);
            trackTitle = itemView.findViewById(R.id.track_title);
            trackArtistName = itemView.findViewById(R.id.track_artist_name);
        }
    }
}
