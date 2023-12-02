package nhom2.voztify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Artist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistDetailActivity extends AppCompatActivity {
    ImageView artistImage;
    TextView artistName;
    RecyclerView artistAlbumsRecyclerView;
    ArtistAdapter artistAdapter;
    Artist artist;
    AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_detail_layout);

        artistImage = findViewById(R.id.artistImage);
        artistName = findViewById(R.id.artistName);
        artistAlbumsRecyclerView = findViewById(R.id.artistAlbumsRecyclerView);

        artist = (Artist) getIntent().getSerializableExtra("artist");

        Picasso.get().load(artist.getPicture()).into(artistImage);
        artistName.setText(artist.getName());

        // Set up RecyclerView for albums
        artistAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display artist's albums
        fetchArtistAlbums(artist.getId());
    }

    private void fetchArtistAlbums(String artistId) {
        DZService service = DeezerService.getService();
        Call<ArtistAlbumsResponse> call = service.getArtistAlbums(artistId);

        call.enqueue(new Callback<ArtistAlbumsResponse>() {
            @Override
            public void onResponse(Call<ArtistAlbumsResponse> call, Response<ArtistAlbumsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Album> albums = response.body().getData();

                    // Set up RecyclerView for albums
                    artistAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(ArtistDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    albumAdapter = new AlbumAdapter(getApplicationContext(), albums); // Assuming you have an ArtistAdapter
                    albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Album album = albums.get(position);
                            Intent intent = new Intent(getApplicationContext(), AlbumDetailActivity.class);
                            album.setArtist(artist);
                            intent.putExtra("album", album);
                            startActivity(intent);
                        }
                    });
                    artistAlbumsRecyclerView.setAdapter(albumAdapter);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<ArtistAlbumsResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
