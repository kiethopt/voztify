package nhom2.voztify.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Model.Album;
import nhom2.voztify.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albums;
    private static int backgroundColor = Color.parseColor("#040D12");
    private static int textColor = Color.BLACK; //Defailt color


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (albums == null || albums.isEmpty()) {
            return;
        }

        Album album = albums.get(position);
        Picasso.get()
                .load(album.getCover())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.albumCover);
        holder.albumTitle.setText(album.getTitle());
        holder.itemView.setBackgroundColor(backgroundColor);
        holder.albumTitle.setTextColor(textColor);
    }

    public static void savePreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("background_color", "#" + Integer.toHexString(backgroundColor & 0x00FFFFFF)); // Save as a hexadecimal string
        editor.apply();
    }

    public static void updateBackground(Context context, String colorHexCode) {
        backgroundColor = Color.parseColor(colorHexCode);
        savePreferences(context);
    }
    public static void updateTextColor(Context context, int color) {
        textColor = color;
        savePreferences(context);
    }
    @Override
    public int getItemCount() {
        return albums != null ? albums.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumCover;
        TextView albumTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumCover = itemView.findViewById(R.id.album_cover);
            albumTitle = itemView.findViewById(R.id.album_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }

}

