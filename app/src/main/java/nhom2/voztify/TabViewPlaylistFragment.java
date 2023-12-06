package nhom2.voztify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Class.Playlist;

public class TabViewPlaylistFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private ImageButton imageButtonAddPlaylist;
    private List<Playlist> playlistItems;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TabViewPlaylistFragment() {
        // Required empty public constructor
    }

    public static TabViewPlaylistFragment newInstance(String param1, String param2) {
        TabViewPlaylistFragment fragment = new TabViewPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_view_playlist2, container, false);
        recyclerView = view.findViewById(R.id.recycle_playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        playlistItems = new ArrayList<>();

        adapter = new PlaylistAdapter(getActivity(), playlistItems);
        recyclerView.setAdapter(adapter);
        imageButtonAddPlaylist = view.findViewById(R.id.img_btn_add_playlist);
        imageButtonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePlaylistActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Lấy danh sách playlist từ Firebase và cập nhật RecyclerView
        updatePlaylistFromFirebase();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String playlistName = data.getStringExtra("playlistName");
                String userId = data.getStringExtra("userId");
                String yourName = data.getStringExtra("yourName");

                Playlist newPlaylist = new Playlist(R.drawable.ai, playlistName, yourName, userId);

                // Lưu playlist mới vào Firebase
                savePlaylistToFirebase(newPlaylist);

                playlistItems.add(newPlaylist);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void savePlaylistToFirebase(Playlist playlist) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("playlists");

            String playlistId = userPlaylistRef.push().getKey();
            playlist.setId(playlistId);
            userPlaylistRef.child(playlistId).setValue(playlist);
        }
    }

    private void updatePlaylistFromFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance()
                    .getReference("users").child(userId).child("playlists");
            userPlaylistRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    playlistItems.clear();

                    for (DataSnapshot playlistSnapshot : snapshot.getChildren()) {
                        Playlist playlist = playlistSnapshot.getValue(Playlist.class);
                        playlistItems.add(playlist);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Failed to load playlists: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}