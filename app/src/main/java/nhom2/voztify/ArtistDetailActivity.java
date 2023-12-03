package nhom2.voztify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Artist;
import nhom2.voztify.Class.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistDetailActivity extends AppCompatActivity {
    private int REQUEST_CODE_PLAY_MUSIC = 1;
    ImageView artistImage;
    TextView artistName, txtViewRelatedArtist;
    RecyclerView artistAlbumsRecyclerView,artistTracksRecyclerView, artistRelatedRecyclerView;
    ArtistAdapter artistAdapter;
    Artist artist;
    Track track;
    AlbumAdapter albumAdapter;
    TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_detail_layout);

        artistImage = findViewById(R.id.artistImage);
        artistName = findViewById(R.id.artistName);
        artistAlbumsRecyclerView = findViewById(R.id.artistAlbumsRecyclerView);
        artistTracksRecyclerView = findViewById(R.id.artistTrackRecyclerView);
        artistRelatedRecyclerView = findViewById(R.id.artistRelatedRecyclerView);

        artist = (Artist) getIntent().getSerializableExtra("artist");

        Picasso.get().load(artist.getPicture()).into(artistImage);
        artistName.setText(artist.getName());

        // Set up RecyclerViews
        artistAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistTracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistRelatedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display artist's albums
        fetchArtistAlbums(artist.getId());
        fetchArtistTracks(artist.getId());
        fetchRelatedArtist(artist.getId());
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
                    AlbumAdapter.updateBackground(getApplicationContext(), "#040D12");
                    AlbumAdapter.updateTextColor(getApplicationContext(), Color.WHITE);
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

    private void fetchArtistTracks(String artistId) {
        DZService service = DeezerService.getService();
        Call<ArtistTracksResponse> call = service.getArtistTracks(artistId);

        call.enqueue(new Callback<ArtistTracksResponse>() {
            @Override
            public void onResponse(Call<ArtistTracksResponse> call, Response<ArtistTracksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getData();

                    // Set up RecyclerView for tracks
                    artistTracksRecyclerView.setLayoutManager(new LinearLayoutManager(ArtistDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    trackAdapter = new TrackAdapter(getApplicationContext(), tracks, new TrackAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Track track = tracks.get(position);

                            Intent intent = new Intent(ArtistDetailActivity.this, PlayMusicActivity.class);
                            intent.putExtra("Track", track);
                            intent.putExtra("TracksList", (Serializable) tracks);
                            startActivityForResult(intent, REQUEST_CODE_PLAY_MUSIC);
                        }
                    });
                    trackAdapter.notifyDataSetChanged();

                    // Change background color, text size, and song artist visibility
                    TrackAdapter.updateBackground(getApplicationContext(), "#040D12");
                    TrackAdapter.updateTextSize(getApplicationContext(), 14);
                    TrackAdapter.updateSongArtistVisibility(getApplicationContext(), View.GONE);
                    TrackAdapter.updateTextGravity(getApplicationContext(), Gravity.CENTER);
                    trackAdapter.notifyDataSetChanged();

                    artistTracksRecyclerView.setAdapter(trackAdapter);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<ArtistTracksResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void fetchRelatedArtist(String artistId) {
        DZService service = DeezerService.getService();
        Call<RelatedArtistsResponse> call = service.getRelatedArtists(artistId);

        call.enqueue(new Callback<RelatedArtistsResponse>() {
            @Override
            public void onResponse(Call<RelatedArtistsResponse> call, Response<RelatedArtistsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Artist> artists = response.body().getData();

                    // Check if the list is empty
                    if (artists.isEmpty()) {
                        txtViewRelatedArtist = findViewById(R.id.txtViewRelatedArtists);
                        txtViewRelatedArtist.setVisibility(View.GONE);
                        Log.d("ArtistDetailActivity", "No related artists found.");
                        // Handle the case when there are no related artists (show a message, hide the related artists section, etc.)
                    } else {
                        // Set up RecyclerView for relatedArtist
                        artistRelatedRecyclerView.setLayoutManager(new LinearLayoutManager(ArtistDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        artistAdapter = new ArtistAdapter(getApplicationContext(), artists, new ArtistAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Artist artist = artists.get(position);
                                Intent intent = new Intent(getApplicationContext(), ArtistDetailActivity.class);
                                intent.putExtra("artist", artist);
                                startActivity(intent);
                            }
                        });
                        ArtistAdapter.updateTextSize(getApplicationContext(), 14);


                        artistRelatedRecyclerView.setAdapter(artistAdapter);
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<RelatedArtistsResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }


}
