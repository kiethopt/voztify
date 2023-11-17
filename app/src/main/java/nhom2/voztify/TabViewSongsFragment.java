package nhom2.voztify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Class.Song;

public class TabViewSongsFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    public TabViewSongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_view_songs, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        songList = new ArrayList<>();
        songList.add(new Song("Song 1", "Artist 1", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 2", "Artist 2", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 3", "Artist 3", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 4", "Artist 4", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 5", "Artist 5", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 6", "Artist 6", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 7", "Artist 7", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 8", "Artist 8", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 9", "Artist 9", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        songList.add(new Song("Song 10", "Artist 10", "https://i.scdn.co/image/ab67616d00001e0207572fc88208da4e900a2fe4"));
        // Add more songs here

        context = getContext();

        SongAdapter songAdapter = new SongAdapter(context, songList, new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Song song = songList.get(position);

                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("SONG_TITLE", song.getTitle());
                intent.putExtra("SONG_ARTIST", song.getArtist());
                intent.putExtra("SONG_IMAGE", song.getImage());

                context.startActivity(intent);
            }
        });

        recyclerView.setAdapter(songAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}