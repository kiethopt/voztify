package nhom2.voztify.View;

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

import java.util.ArrayList;

import nhom2.voztify.Model.Track;
import nhom2.voztify.R;

public class TrackDetailActivity extends AppCompatActivity {
    ImageView trackImage;
    TextView trackTitle;
    TextView trackArtist;
    RecyclerView trackTracksRecyclerView;
    SearchTrackAdapter searchtrackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);

        trackImage = findViewById(R.id.trackImage);
        trackTitle = findViewById(R.id.trackTitle);
        trackArtist = findViewById(R.id.trackArtist);
        trackTracksRecyclerView = findViewById(R.id.trackTracksRecyclerView);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Kích hoạt nút Up (Back)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        trackTracksRecyclerView = findViewById(R.id.trackTracksRecyclerView);
        trackTracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchtrackAdapter = new SearchTrackAdapter(this, new ArrayList<>());
        trackTracksRecyclerView.setAdapter(searchtrackAdapter);

        Track track = (Track) getIntent().getSerializableExtra("track");

        if (track != null) {
            // Cập nhật thông tin track...
            Picasso.get().load(track.getAlbum() != null ? track.getAlbum().getCover() : "default_image_url").into(trackImage);
            trackTitle.setText(track.getTitle());
            trackArtist.setText(track.getArtist() != null ? track.getArtist().getName() : "Unknown Artist");

            // Thêm chỉ track được chọn vào danh sách
            searchtrackAdapter.addTrack(track);
        }

        searchtrackAdapter.setTrackClickListener(new SearchTrackAdapter.OnTrackClickListener() {
            @Override
            public void onTrackClick(Track selectedTrack) {
                Intent intent = new Intent(TrackDetailActivity.this, PlayMusicActivity.class);
                intent.putExtra("Track", selectedTrack);
                startActivity(intent);
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
