package nhom2.voztify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Class.Artist;

public class ArtistGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Artist> artists;

    public ArtistGridViewAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @Override
    public int getCount() {
        return artists != null ? artists.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return artists != null ? artists.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.artist_grid_item, parent, false);
        }

        ImageView artistImage = convertView.findViewById(R.id.artist_image);
        TextView artistName = convertView.findViewById(R.id.artist_name);

        Artist artist = artists.get(position);
        if (artist != null) {
            Picasso.get()
                    .load(artist.getPicture())
                    .placeholder(R.drawable.placeholder_img)
                    .error(R.drawable.error_img)
                    .into(artistImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Action on successful image loading
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("ArtistImageError", "Error loading image: " + e.getMessage());
                        }
                    });

            artistName.setText(artist.getName());
        }

        return convertView;
    }

    public void updateData(List<Artist> newArtists) {
        artists.clear();
        artists.addAll(newArtists);
        notifyDataSetChanged();
    }
}
