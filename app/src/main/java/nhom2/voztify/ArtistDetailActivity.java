package nhom2.voztify;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import nhom2.voztify.Class.Artist;

public class ArtistDetailActivity extends AppCompatActivity {
    ImageView artistImage;
    TextView artistName;
    RecyclerView artistAlbumsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_detail_layout);

        artistImage = findViewById(R.id.artistImage);
        artistName = findViewById(R.id.artistName);
        artistAlbumsRecyclerView = findViewById(R.id.artistAlbumsRecyclerView);

        Artist artist = (Artist) getIntent().getSerializableExtra("artist");

        Picasso.get().load(artist.getPicture()).into(artistImage);
        artistName.setText(artist.getName());

        // Set up RecyclerView for albums
        artistAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Assume ArtistAlbumsAdapter is created and set up properly
        // artistAlbumsRecyclerView.setAdapter(new ArtistAlbumsAdapter(artist.getAlbums()));

        // Fetch and display artist's albums
        fetchArtistAlbums(artist.getId());
    }

    private void fetchArtistAlbums(String artistId) {
        // Implement API call to fetch albums and update the RecyclerView adapter
    }
}
