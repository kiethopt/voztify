package nhom2.voztify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Song;
import nhom2.voztify.Class.Track;
import nhom2.voztify.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityInsertSongPlaylist extends AppCompatActivity {
    private EditText searchSongEditText;
    private RecyclerView recyclerView;
    private SearchTrackAdapter trackAdapter;

    private DZService dzService;

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
        recyclerView.setAdapter(trackAdapter);

        // Set up the TextWatcher for real-time search
        searchSongEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchTrack(editable.toString());
            }
        });
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