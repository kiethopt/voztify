package nhom2.voztify.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Model.Album;
import nhom2.voztify.Model.Artist;
import nhom2.voztify.Model.Track;
import nhom2.voztify.R;
import nhom2.voztify.Controller.SearchResult;

public class SearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<SearchResult> results;

    public SearchResultAdapter(Context context, List<SearchResult> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult result = results.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        if (result.isArtist()) {
            // Xử lý nếu là nghệ sĩ
            Artist artist = result.getArtist();
            Picasso.get().load(artist.getPicture()).into(imageView);
            textView.setText(artist.getName());
        } else if (result.isAlbum()) {
            // Xử lý nếu là album
            Album album = result.getAlbum();
            if (album != null && album.getCover() != null) {
                Picasso.get().load(album.getCover()).into(imageView);
                textView.setText(album.getTitle());
            }
        } else if (result.isTrack()) {
            // Xử lý nếu là track
            Track track = result.getTrack();
            if (track != null) {
                // Sử dụng ảnh đại diện của album cho track hoặc ảnh mặc định nếu album là null
                String imageUrl = (track.getAlbum() != null && track.getAlbum().getCover() != null)
                        ? track.getAlbum().getCover()
                        : "url_to_default_image"; // Thay "url_to_default_image" bằng URL hợp lý
                Picasso.get().load(imageUrl).into(imageView);
                // Đặt tiêu đề track và tên nghệ sĩ (nếu có)
                String trackInfo = track.getTitle() + (track.getArtist() != null ? " - " + track.getArtist().getName() : "");
                textView.setText(trackInfo);
            }
        }

        convertView.setOnClickListener(v -> {
            if (result.isArtist()) {
                Intent intent = new Intent(context, ArtistDetailActivity.class);
                intent.putExtra("artist", result.getArtist());
                context.startActivity(intent);
            } else if (result.isAlbum()) {
                Intent intent = new Intent(context, AlbumDetailActivity.class);
                intent.putExtra("album", result.getAlbum());
                context.startActivity(intent);
            } else if (result.isTrack()) {
                // Xử lý khi track được click
                Track selectedTrack = result.getTrack();
                Intent intent = new Intent(context, TrackDetailActivity.class);
                intent.putExtra("track", selectedTrack);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void updateData(List<SearchResult> newResults) {
        this.results = newResults;
        notifyDataSetChanged();
    }
}
