package nhom2.voztify.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Controller.TrackResponse;
import nhom2.voztify.Model.Track;
import nhom2.voztify.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityInsertSongPlaylist extends AppCompatActivity {
    private EditText searchSongEditText;
    private RecyclerView recyclerView;
    private SearchTrackAdapter trackAdapter;
    private DZService dzService;
    private List<Track> songDetailList; // Assuming this is your list of tracks in recycler_view_song_detail


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_song_playlist);
        searchSongEditText = findViewById(R.id.search_song);
        dzService = DeezerService.getService();
        recyclerView = findViewById(R.id.recycler_view_search_song);

        // Set up RecyclerView and adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        trackAdapter = new SearchTrackAdapter(this, new ArrayList<>());
        songDetailList = new ArrayList<>();
        recyclerView.setAdapter(trackAdapter);

        trackAdapter.setTrackClickListener(new SearchTrackAdapter.OnTrackClickListener() {
            @Override
            public void onTrackClick(Track track) {
                addTrackToFirebasePlaylist(getActualPlaylistId(), track);
                Toast.makeText(ActivityInsertSongPlaylist.this, "Song added successfully", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        // Set up the TextWatcher for real-time search
        searchSongEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
//                String partialQuery = charSequence.toString().trim();
//                if (!partialQuery.isEmpty()) {
//                    searchTrack(partialQuery);
//                } else {
//                    trackAdapter.updateTracks(new ArrayList<>());
//                    showEmptyResultImage(true);
//                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

//                    trackAdapter.updateTracks(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String trackTitle = editable.toString().trim();

                if (!trackTitle.isEmpty()) {
                    searchTrack(trackTitle);
                } else {
                    // If the EditText is empty, clear the RecyclerView and show the empty image
                    trackAdapter.updateTracks(new ArrayList<>());
                }
            }
        });
    }

    private void addTrackToRecyclerView(Track track) {
        songDetailList.add(track);
        trackAdapter.notifyDataSetChanged();

    }

    private String getActualPlaylistId() {
        // Implement your logic to retrieve the actual playlistId
        // For example, you might receive it through an intent
        return getIntent().getStringExtra("playlistId");
    }

    private void addTrackToFirebasePlaylist(String playlistId, Track track) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String trackId = track.getId();
        if (currentUser != null && !trackId.isEmpty()) {
            String userId = currentUser.getUid();

            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("playlists").child(playlistId).child("song of playlist");

            Track trackData = track;

            // Save track information to the playlist
            playlistRef.child(trackId).setValue(trackData);
        }
        else {
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        }
    }



    private void searchTrack(String trackTitle) {
        Call<TrackResponse> call = dzService.searchTrack(trackTitle);
        call.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getTracks();
                    updateRecyclerView(tracks);
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void updateRecyclerView(List<Track> tracks) {
        trackAdapter.updateTracks(tracks);
    }


}