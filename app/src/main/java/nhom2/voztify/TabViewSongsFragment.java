package nhom2.voztify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabViewSongsFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private TrackAdapter songAdapter;
    private List<Track> trackList;

    public TabViewSongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_view_tracks, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        context = getContext();

        fetchTracks();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void fetchTracks() {
        DZService service = DeezerService.getService();
        Call<JsonObject> call = service.getTracks(); // Assume JsonObject as the response type

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    if (jsonResponse.has("tracks")) {
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("tracks");

                        // Now you can use Gson to deserialize the "tracks" object
                        TrackData trackData = new Gson().fromJson(tracksObject, TrackData.class);
                        List<Track> tracks = trackData.getData();
                        updateListView(tracks);
                    } else {
                        Log.e("Bai2", "Response does not contain 'tracks' field");
                    }
                } else {
                    try {
                        Log.e("Bai2", "Error fetching tracks: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Bai2", "Error fetching tracks: " + t.getMessage());
            }
        });
    }



    private void updateListView(List<Track> tracks) {
        TrackAdapter adapter = new TrackAdapter(context, tracks,new TrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Track track = trackList.get(position);

                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("SONG_TITLE", track.getTitle());
                intent.putExtra("SONG_ARTIST", track.getArtist().getName());
                intent.putExtra("SONG_IMAGE", track.getAlbum().getCover_medium());

                context.startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}