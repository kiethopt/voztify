package nhom2.voztify;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Class.Artist;

public class TabViewArtistsFragment extends Fragment {
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

//        artistList = new ArrayList<>();
//        artistList.add(new Artist("Wren Evans", "https://i.scdn.co/image/ab6761610000f178cbc6b072aec429273fdbd53b"));
//        artistList.add(new Artist("Wren Evans1", "https://i.scdn.co/image/ab6761610000f178cbc6b072aec429273fdbd53b"));
//        artistList.add(new Artist("Wren Evans2", "https://i.scdn.co/image/ab6761610000f178cbc6b072aec429273fdbd53b"));
//        artistList.add(new Artist("Wren Evans3", "https://i.scdn.co/image/ab6761610000f17837b110af3f232710b8f14a97"));
//        artistList.add(new Artist("Wren Evans4", "https://i.scdn.co/image/ab6761610000f178cbc6b072aec429273fdbd53b"));
//        artistList.add(new Artist("Wren Evans5", "https://i.scdn.co/image/ab6761610000f17837b110af3f232710b8f14a97"));
//        artistList.add(new Artist("Wren Evans6", "https://i.scdn.co/image/ab6761610000f178cbc6b072aec429273fdbd53b"));

        // Add more songs here
        artistAdapter = new ArtistAdapter(requireContext(), artistList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(artistAdapter);

        return view;
    }
}