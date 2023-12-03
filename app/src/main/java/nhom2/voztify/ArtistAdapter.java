package nhom2.voztify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private List<Artist> artists;
    private ArtistAdapter.OnItemClickListener onItemClickListener;
    private static float textSize = 20;

    public ArtistAdapter(Context context, List<Artist> artists, ArtistAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.artists = artists;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ArtistAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_recycler_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (artists == null || artists.isEmpty()) {
            return;
        }

        Log.e("ARTIST", artists.toString());

        Artist artist = artists.get(position);
        Picasso.get()
                .load(artist.getPicture())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.artistImage);
        holder.artistName.setText(artist.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);

                }
            }
        });
        holder.artistName.setTextSize(textSize);
    }

    public static void savePreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("text_size", textSize);
        editor.apply();
    }
    public static void updateTextSize(Context context, float size) {
        textSize = size;
        savePreferences(context);
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
