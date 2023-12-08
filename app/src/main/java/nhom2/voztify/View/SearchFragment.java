package nhom2.voztify.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Controller.AlbumSearchResponse;
import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Controller.ArtistSearchResponse;
import nhom2.voztify.Model.Album;
import nhom2.voztify.Model.Artist;
import nhom2.voztify.Model.Track;
import nhom2.voztify.R;
import nhom2.voztify.Controller.SearchResult;
import nhom2.voztify.Controller.TrackResponse;
import nhom2.voztify.Controller.TrendingAlbumResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private EditText searchField;
    private GridView searchResultsGrid;
    private DZService service;
    private ArtistGridViewAdapter adapter;
    private AlbumGridViewAdapter albumAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        service = DeezerService.getService();
        searchField = view.findViewById(R.id.search_field);
        searchResultsGrid = view.findViewById(R.id.tags_grid);

        adapter = new ArtistGridViewAdapter(getContext(), new ArrayList<>());
        searchResultsGrid.setAdapter(adapter);

        // Initialize the albumAdapter
        albumAdapter = new AlbumGridViewAdapter(getContext(), new ArrayList<>());
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = searchField.getText().toString();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    loadTrendingAlbums();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadTrendingAlbums();
        return view;
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            searchArtistsAndAlbums(query);
        } else {
            loadTrendingAlbums();
        }
    }

    private void searchArtistsAndAlbums(String query) {
        List<SearchResult> combinedResults = new ArrayList<>();

        // Fetch artists
        Call<ArtistSearchResponse> artistCall = service.searchArtist(query);
        artistCall.enqueue(new Callback<ArtistSearchResponse>() {
            @Override
            public void onResponse(Call<ArtistSearchResponse> call, Response<ArtistSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Artist artist : response.body().getData()) {
                        combinedResults.add(new SearchResult(artist));
                    }

                    updateResultsGrid(combinedResults);
                }
            }

            @Override
            public void onFailure(Call<ArtistSearchResponse> call, Throwable t) {
            }
        });

        // Fetch albums
        Call<AlbumSearchResponse> albumCall = service.searchAlbum(query);
        albumCall.enqueue(new Callback<AlbumSearchResponse>() {
            @Override
            public void onResponse(Call<AlbumSearchResponse> call, Response<AlbumSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Album album : response.body().getData()) {
                        combinedResults.add(new SearchResult(album));
                    }

                    updateResultsGrid(combinedResults);
                }
            }

            @Override
            public void onFailure(Call<AlbumSearchResponse> call, Throwable t) {
            }
        });

        // Fetch tracks
        Call<TrackResponse> trackCall = service.searchTrack(query);
        trackCall.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getTracks();
                    for (Track track : tracks) {
                        combinedResults.add(new SearchResult(track));
                    }
                    updateResultsGrid(combinedResults);
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void updateResultsGrid(List<SearchResult> combinedResults) {
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getContext(), combinedResults);
        searchResultsGrid.setAdapter(searchResultAdapter);
    }

    private void loadTrendingAlbums() {
        Call<TrendingAlbumResponse> call = service.getTrendingAlbums();
        call.enqueue(new Callback<TrendingAlbumResponse>() {
            @Override
            public void onResponse(Call<TrendingAlbumResponse> call, Response<TrendingAlbumResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Album> albums = response.body().getData();
                    albumAdapter.updateData(albums);
                    searchResultsGrid.setAdapter(albumAdapter);

                    // Thiết lập sự kiện click cho mỗi album
                    albumAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Album album) {
                            Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
                            intent.putExtra("album", album);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error loading trending albums", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TrendingAlbumResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}