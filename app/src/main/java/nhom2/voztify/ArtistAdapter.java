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

import nhom2.voztify.Class.Artist;
import nhom2.voztify.Class.Song;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private List<Artist> artists;

    public ArtistAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_recycler_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (artists == null || artists.isEmpty()) {
            return;
        }

        Artist artist = artists.get(position);
        Picasso.get()
                .load(artist.getImage())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.artistImage);
        holder.artistName.setText(artist.getName());
    }

    @Override
    public int getItemCount() {
        return artists != null ? artists.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistName = itemView.findViewById(R.id.artist_name);
        }
    }
}
