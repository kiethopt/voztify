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
import java.io.Serializable;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Class.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabViewTracksFragment extends Fragment {
    private static final int REQUEST_CODE_PLAY_MUSIC = 1;
    private Context context;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private List<Track> trackList;

    public TabViewTracksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_view_tracks, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        context = getContext();

        fetchTracks();

        return view;
    }

    private void fetchTracks() {
        DZService service = DeezerService.getService();
        Call<JsonObject> call = service.getTopTracks(); // Assume JsonObject as the response type

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    if (jsonResponse.has("tracks")) {
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("tracks");

                        TrackResponse trackData = new Gson().fromJson(tracksObject, TrackResponse.class);
                        trackList = trackData.getTracks();
                        updateListView(trackList);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        recyclerView.setLayoutManager(layoutManager);
                    } else {
                        Log.e("TrackFragment", "Response does not contain 'tracks' field");
                    }
                } else {
                    try {
                        Log.e("TrackFragment", "Error fetching tracks: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TrackFragment", "Error fetching tracks: " + t.getMessage());
            }
        });
    }



    private void updateListView(List<Track> tracks) {
        trackAdapter = new TrackAdapter(context, tracks, new TrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Track track = tracks.get(position);

                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                intent.putExtra("Track", track);
                intent.putExtra("TracksList", (Serializable) trackList);
                startActivityForResult(intent, REQUEST_CODE_PLAY_MUSIC);
            }
        });

        recyclerView.setAdapter(trackAdapter);
    }
}