package nhom2.voztify;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Track;
import nhom2.voztify.TrackData;
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

        fetchAlbumTracks(album.getId());
    }

    private void fetchAlbumTracks(String albumId) {
        DZService service = DeezerService.getService();
        Call<TrackData> call = service.getAlbumTracks(albumId);

        call.enqueue(new Callback<TrackData>() {
            @Override
            public void onResponse(Call<TrackData> call, Response<TrackData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getTracks();
                    searchtrackAdapter.updateTracks(tracks); // Update the adapter with the fetched tracks
                } else {
                    // Handle the case where the response is not successful
                }
            }

            @Override
            public void onFailure(Call<TrackData> call, Throwable t) {
                // Handle the failure case
            }
        });
    }
}
