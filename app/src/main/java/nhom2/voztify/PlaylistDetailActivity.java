package nhom2.voztify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaylistDetailActivity extends AppCompatActivity {
    ImageView imgPlaylistDetail;
    TextView tvPlaylistDetailName;
    TextView tvYourNameDetail;
    ImageButton imgButtonShowDialog;
    Toolbar toolbar;
    String playlistId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        LinearLayout addSongLayout = findViewById(R.id.layout_add_song);
        ImageButton imgBtnAddSong = findViewById(R.id.img_btn_add_song_playlist);
        toolbar = findViewById(R.id.toolbar4sd);


        imgPlaylistDetail = findViewById(R.id.img_playlist_detail);
        tvPlaylistDetailName = findViewById(R.id.tv_playlist_detail_name);
        tvYourNameDetail = findViewById(R.id.tv_your_name_detail);
        imgButtonShowDialog = findViewById(R.id.image_btn_show_dialog);

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

            // Set the received data to the corresponding views
            tvPlaylistDetailName.setText(playlistName);
            tvYourNameDetail.setText(yourName);
            imgPlaylistDetail.setImageResource(imageResourceId);  // Set the image resource


        }
    }

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
                    startActivity(updateIntent);
                } else {
                    Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist name", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();    }
        });
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the playlist ID from the intent
                playlistId = getIntent().getStringExtra("playlistId");

                if (playlistId != null && !playlistId.isEmpty()) {
                    // Pass the playlist ID to the delete function
                    deletePlaylistFromFirebase(playlistId);
                } else {
                    // Handle the case when the playlist ID is null or empty
                    Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist ID", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
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

//    private void deletePlaylistFromFirebase(String playlistId) {
//        DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance().getReference("users")
//                .child(currentUser.getUid()).child("playlists");
//
//        // Kiểm tra xem playlistId có tồn tại không trước khi xóa
//        userPlaylistRef.child(playlistId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Nút con tồn tại, tiến hành xóa
//                    userPlaylistRef.child(playlistId).removeValue()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    // Xóa thành công
//                                    Intent resultIntent = new Intent();
//                                    resultIntent.putExtra("deletedPlaylistId", playlistId);
//                                    setResult(Activity.RESULT_OK, resultIntent);
//                                    finish();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // Xóa thất bại, xử lý theo ý của bạn
//                                    Toast.makeText(PlaylistDetailActivity.this, "Failed to delete playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                } else {
//                    // Nút con không tồn tại, có thể đã bị xóa bởi người dùng khác
//                    Toast.makeText(PlaylistDetailActivity.this, "Playlist not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý lỗi nếu có
//                Toast.makeText(PlaylistDetailActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void deletePlaylistFromFirebase(String playlistId) {
        DatabaseReference userPlaylistRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid()).child("playlists");

        // Kiểm tra xem playlistId có tồn tại không trước khi xóa
        if (playlistId != null && !playlistId.isEmpty()) {
            userPlaylistRef.child(playlistId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // ...
                    // Xóa playlist từ Firebase
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // ...
                }
            });
        } else {
            Toast.makeText(PlaylistDetailActivity.this, "Invalid playlist ID", Toast.LENGTH_SHORT).show();

        }
    }

}