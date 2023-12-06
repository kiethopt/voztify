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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePlaylistName();
            }
        });
        // Xử lý sự kiện khi nút back được nhấn
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            String playlistNameToUpdate = intent.getStringExtra("playlistNameToUpdate");

            // Set the playlist name to the EditText
            if (playlistNameToUpdate != null) {
                edtUpdatePlaylist.setText(playlistNameToUpdate);
            }
        }
    }
    private void updatePlaylistName() {
        String updatedName = edtUpdatePlaylist.getText().toString().trim();
        if (!updatedName.isEmpty()) {
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("playlists");

            playlistRef.child(playlistNameToUpdate).child("playlistName").setValue(updatedName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Cập nhật thành công
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updatedPlaylistName", updatedName);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý khi cập nhật thất bại
                            Toast.makeText(ActivityUpdatePlaylist.this, "Không thể cập nhật tên danh sách phát: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(ActivityUpdatePlaylist.this, "Tên danh sách phát không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }



}