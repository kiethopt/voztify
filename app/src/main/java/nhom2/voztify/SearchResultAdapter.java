package nhom2.voztify;

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

import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Artist;

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
            Artist artist = result.getArtist();
            Picasso.get().load(artist.getPicture()).into(imageView);
            textView.setText(artist.getName());
        } else {
            Album album = result.getAlbum();
            Picasso.get().load(album.getCover()).into(imageView);
            textView.setText(album.getTitle());
        }

        convertView.setOnClickListener(v -> {
            SearchResult searchResult = results.get(position);
            if (searchResult.isArtist()) {
                // Existing code for Artist
            } else {
                Intent intent = new Intent(context, AlbumDetailActivity.class);
                intent.putExtra("album", searchResult.getAlbum());
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
