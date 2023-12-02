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
import nhom2.voztify.Class.Artist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabViewArtistsFragment extends Fragment {
    private static final int REQUEST_CODE = 1;
    private Context context;
    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private List<Artist> artistList;

    public TabViewArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_view_artists, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        context = getContext();

        fetchArtists();

        return view;
    }

    private void fetchArtists() {
        DZService service = DeezerService.getService();
        Call<JsonObject> call = service.getTopArtists();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    if (jsonResponse.has("artists")) {
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("artists");

                        ArtistData artistData = new Gson().fromJson(tracksObject, ArtistData.class);
                        artistList = artistData.getArtists();
                        updateListView(artistList);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        recyclerView.setLayoutManager(layoutManager);
                    } else {
                        Log.e("ArtistFragment", "Response does not contain 'artists' field");
                    }
                } else {
                    try {
                        Log.e("ArtistFragment", "Error fetching artists: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ArtistFragment", "Error fetching artists: " + t.getMessage());
            }
        });
    }



    private void updateListView(List<Artist> artists) {
        artistAdapter = new ArtistAdapter(context, artists, new ArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Artist artist = artists.get(position);

                Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
                intent.putExtra("artist", artist);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        recyclerView.setAdapter(artistAdapter);
    }
}