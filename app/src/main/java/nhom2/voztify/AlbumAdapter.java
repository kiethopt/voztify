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

import nhom2.voztify.Class.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albums;

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

