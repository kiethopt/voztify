package nhom2.voztify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumDetailActivity extends AppCompatActivity {
    ImageView albumImage;
    TextView albumName;
    TextView albumArtistName;
    RecyclerView albumTracksRecyclerView;
    SearchTrackAdapter searchtrackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_detail_layout);

        albumImage = findViewById(R.id.albumImage);
        albumName = findViewById(R.id.albumName);
        albumArtistName = findViewById(R.id.albumArtistName);
        albumTracksRecyclerView = findViewById(R.id.albumTracksRecyclerView);

        Album album = (Album) getIntent().getSerializableExtra("album");

        Picasso.get().load(album.getCover()).into(albumImage);
        albumName.setText(album.getTitle());
        albumArtistName.setText(album.getArtist().getName());

        albumTracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchtrackAdapter = new SearchTrackAdapter(this, new ArrayList<>());
        albumTracksRecyclerView.setAdapter(searchtrackAdapter);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Kích hoạt nút Up (Back)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        searchtrackAdapter.setTrackClickListener(new SearchTrackAdapter.OnTrackClickListener() {
            @Override
            public void onTrackClick(Track track) {
                Intent intent = new Intent(AlbumDetailActivity.this, PlayMusicActivity.class);
                intent.putExtra("Track", track);
                intent.putExtra("TracksList", (Serializable) searchtrackAdapter.getTracks()); // Gửi danh sách các track
                startActivity(intent);
            }
        });

        fetchAlbumTracks(album.getId());
    }

    private void fetchAlbumTracks(String albumId) {
        DZService service = DeezerService.getService();
        Call<TrackResponse> call = service.getAlbumTracks(albumId);

        call.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getTracks();
                    searchtrackAdapter.updateTracks(tracks);
                } else {
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
