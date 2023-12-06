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
import nhom2.voztify.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityInsertSongPlaylist extends AppCompatActivity {
    private EditText searchSongEditText;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
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
        songAdapter = new SongAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(songAdapter);
        // Set up the TextWatcher for real-time search
        searchSongEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void performSearch(String query) {
        // Use Retrofit to make a request to the Deezer API for song search
        Call<JsonObject> call = dzService.searchSongs(query, "track");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();

                    // Parse the JSON response to get a list of songs
                    List<Song> songs = parseJsonResponse(jsonObject);

                    // Update the RecyclerView with the list of songs
                    updateRecyclerView(songs);
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle failure
            }
        });
    }
    private List<Song> parseJsonResponse(JsonObject jsonObject){

        return null;
    }
    private void updateRecyclerView(List<Song> songs) {
        // Implement logic to update the RecyclerView with the list of songs
        // Use the songs list to set up your RecyclerView adapter
        songAdapter.updateSongs(songs);
    }

}