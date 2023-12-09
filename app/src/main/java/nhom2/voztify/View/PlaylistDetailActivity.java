package nhom2.voztify.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Model.Track;
import nhom2.voztify.R;

public class PlaylistDetailActivity extends AppCompatActivity {
    private ImageView imgPlaylistDetail;
    private TextView tvPlaylistDetailName;
    private TextView tvYourNameDetail;
    private ImageButton imgButtonShowDialog, imgButtonPlay;

    LinearLayout layoutAddSong;
    private Toolbar toolbar;
    private String playlistId;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();


    private RecyclerView recyclerView;
    private SongOfPlaylistAdapter songOfPlaylistAdapter;
    private List<Track> songForUList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);



        LinearLayout addSongLayout = findViewById(R.id.layout_add_song);

        ImageButton imgBtnAddSong = findViewById(R.id.img_btn_add_song_playlist);
        imgButtonPlay = findViewById(R.id.imageButton3);
        imgPlaylistDetail = findViewById(R.id.img_playlist_detail);
        tvPlaylistDetailName = findViewById(R.id.tv_playlist_detail_name);
        tvYourNameDetail = findViewById(R.id.tv_your_name_detail);
        imgButtonShowDialog = findViewById(R.id.image_btn_show_dialog);
        layoutAddSong = findViewById(R.id.layout_add_song);



        toolbar = findViewById(R.id.toolbar4sd);
        setSupportActionBar(toolbar);
        // Tắt tiêu đề mặc định của ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set custom drawable as the navigation icon
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_ios_24);
        // Xử lý sự kiện khi nút back được nhấn
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imgBtnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistDetailActivity.this, ActivityInsertSongPlaylist.class);
                intent.putExtra("playlistId", playlistId);  // Pass the playlistId to the new activity
                startActivity(intent);
            }
        });
        layoutAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistDetailActivity.this, ActivityInsertSongPlaylist.class);
                intent.putExtra("playlistId", playlistId);  // Pass the playlistId to the new activity
                startActivity(intent);
            }
        });

        imgButtonShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String playlistName = intent.getStringExtra("playlistName");
            String yourName = intent.getStringExtra("yourName");
            int imageResourceId = intent.getIntExtra("imageResourceId", 0);  // Default value is 0

            // Set the current playlist ID

            playlistId = intent.getStringExtra("playlistId");

            // Set the received data to the corresponding views
            tvPlaylistDetailName.setText(playlistName);
            tvYourNameDetail.setText(yourName);
            imgPlaylistDetail.setImageResource(imageResourceId);  // Set the image resource


        }

        imgButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistDetailActivity.this, PlayMusicActivity.class);
                intent.putExtra("Track", songForUList.get(0));
                intent.putExtra("TracksList", (Serializable) songForUList);
                startActivity(intent);
            }
        });



        recyclerView = findViewById(R.id.recycler_view_song_detail);
        LinearLayoutManager layoutManagerRV = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManagerRV);

        songForUList = new ArrayList<>();
        songOfPlaylistAdapter = new SongOfPlaylistAdapter(getApplicationContext(),songForUList);
        recyclerView.setAdapter(songOfPlaylistAdapter);

        getSongFromFirebase();
    }



    //
    private void getSongFromFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference songOfPl = FirebaseDatabase.getInstance()
                    .getReference("users").child(userId)
                    .child("playlists").child(playlistId)
                    .child("song of playlist");

            songOfPl.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    songForUList.clear();

                    // Iterate through the playlists
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Track songForU = snapshot.getValue(Track.class);
                        songForUList.add(songForU);

                    }
                    songOfPlaylistAdapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
    //
    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_playlist);

        LinearLayout createLayout = dialog.findViewById(R.id.layout_create);
        LinearLayout editLayout = dialog.findViewById(R.id.layout_edit);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layout_delete);
        createLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlaylistDetailActivity.this, "Create is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ActivityUpdatePlaylist with the playlist name
                String playlistName = tvPlaylistDetailName.getText().toString();
                if (!playlistName.isEmpty()) {
                    Intent updateIntent = new Intent(PlaylistDetailActivity.this, ActivityUpdatePlaylist.class);
                    updateIntent.putExtra("playlistNameToUpdate", playlistName);
                    startActivityForResult(updateIntent, 1);  // Start the activity for result
                } else {
                    Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist name", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();    }
        });
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // Get the playlist ID from the intent
//                playlistId = getIntent().getStringExtra("playlistId");
//
//                if (playlistId != null && !playlistId.isEmpty()) {
//                    // Pass the playlist ID to the delete function
//                    deletePlaylistFromFirebase(playlistId);
//                } else {
//                    // Handle the case when the playlist ID is null or empty
//                    Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist ID", Toast.LENGTH_SHORT).show();
//                }
                showDeleteConfirmationDialog();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    //
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this playlist?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, proceed with deletion
                deletePlaylistFromFirebase(playlistId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    //
    private void getPlaylistIdAndDelete() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("playlists");

            // Check if userPlaylistRef is not null
            if (userPlaylistRef != null) {
                // Get the playlist ID from the reference
                String playlistId = getIntent().getStringExtra("playlistId");

                if (playlistId != null && !playlistId.isEmpty()) {
                    // Pass the playlist ID to the delete function
                    deletePlaylistFromFirebase(playlistId);
                } else {
                    // Handle the case when the playlist ID is null or empty
                    Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist ID", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case when userPlaylistRef is null
                Toast.makeText(PlaylistDetailActivity.this, "User playlist reference is null", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //
    private void deletePlaylistFromFirebase(String playlistId) {
        DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid()).child("playlists");

        // Check if playlistId exists before deleting
        if (playlistId != null && !playlistId.isEmpty()) {
            // Remove the playlist from the user's playlists
            userPlaylistRef.child(playlistId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Deletion successful
                            Toast.makeText(PlaylistDetailActivity.this, "Playlist deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();  // You might want to close the activity after deletion
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the failure
                            Toast.makeText(PlaylistDetailActivity.this, "Failed to delete playlist", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist ID", Toast.LENGTH_SHORT).show();
        }
    }
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                String updatedPlaylistName = data.getStringExtra("updatedPlaylistName");
                String yourName = data.getStringExtra("yourName");


                // Update your UI or perform any additional logic with the updated information
                // For example, you can update the playlist name TextView
                tvPlaylistDetailName.setText(updatedPlaylistName, TextView.BufferType.valueOf(yourName));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Fetch and display the user's recently played tracks
        getSongFromFirebase();

    }

}