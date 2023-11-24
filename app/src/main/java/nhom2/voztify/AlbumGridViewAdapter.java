package nhom2.voztify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Class.Album;

public class AlbumGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Album> albums;

    public AlbumGridViewAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums != null ? albums.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.album_grid_item, parent, false);
            holder = new ViewHolder();
            holder.albumCover = convertView.findViewById(R.id.album_cover);
            holder.albumTitle = convertView.findViewById(R.id.album_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Album album = albums.get(position);
        Picasso.get()
                .load(album.getCover())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.albumCover);
        holder.albumTitle.setText(album.getTitle());

        return convertView;
    }

    public void updateData(List<Album> newAlbums) {
        this.albums = newAlbums;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView albumCover;
        TextView albumTitle;
    }
}
