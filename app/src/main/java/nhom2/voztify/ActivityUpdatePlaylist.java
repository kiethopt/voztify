package nhom2.voztify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nhom2.voztify.Class.Playlist;


public class ActivityUpdatePlaylist extends AppCompatActivity {
    EditText edtUpdatePlaylist;
    Button btnSave;
    Toolbar toolbar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String playlistNameToUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_playlist);
        edtUpdatePlaylist = findViewById(R.id.edt_update);
        btnSave = (Button) findViewById(R.id.btn_save);
        toolbar = findViewById(R.id.toolbar_update);

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


        // Retrieve playlistNameToUpdate from Intent
        playlistNameToUpdate = getIntent().getStringExtra("playlistNameToUpdate");
        if (playlistNameToUpdate != null && !playlistNameToUpdate.isEmpty()) {
            edtUpdatePlaylist.setText(playlistNameToUpdate);
        } else {
            Toast.makeText(ActivityUpdatePlaylist.this, "Invalid playlist name", Toast.LENGTH_SHORT).show();
            finish();
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePlaylistName();
            }
        });

    }


    private void updatePlaylistName() {
        String updatedName = edtUpdatePlaylist.getText().toString().trim();

        if (!updatedName.isEmpty()) {
            DatabaseReference playlistRef = FirebaseDatabase.getInstance()
                    .getReference("users").child(currentUser.getUid()).child("playlists");

            if (playlistNameToUpdate != null && !playlistNameToUpdate.isEmpty()) {
                playlistRef.orderByChild("playlistName").equalTo(playlistNameToUpdate)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot playlistSnapshot : dataSnapshot.getChildren()) {
                                      String playlistId = playlistSnapshot.getKey();
                                        // Update the playlist name in Firebase
                                        updatePlaylistNameInFirebase(playlistId, updatedName);
                                        break;
                                    }
                                } else {
                                    Toast.makeText(ActivityUpdatePlaylist.this, "Playlist not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ActivityUpdatePlaylist.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ActivityUpdatePlaylist.this, "Invalid playlist name", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ActivityUpdatePlaylist.this, "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePlaylistNameInFirebase(String playlistId, String updatedName) {
        DatabaseReference playlistRef = FirebaseDatabase.getInstance()
                .getReference("users").child(currentUser.getUid()).child("playlists").child(playlistId);

        playlistRef.child("playlistName").setValue(updatedName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful, you can handle additional logic here
                        Toast.makeText(ActivityUpdatePlaylist.this, "Playlist name updated successfully", Toast.LENGTH_SHORT).show();
                        // Pass the updated information back to the calling activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedPlaylistName", updatedName);
                        setResult(Activity.RESULT_OK, resultIntent);

                        // Finish the current activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(ActivityUpdatePlaylist.this, "Failed to update playlist name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}