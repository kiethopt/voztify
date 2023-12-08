package nhom2.voztify.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nhom2.voztify.R;

public class CreatePlaylistActivity extends AppCompatActivity {
    ImageButton imgButtonBack;
    EditText edtCreatePlaylist;
    Button btnCreatePlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);
        imgButtonBack = (ImageButton) findViewById(R.id.imageButton_back);
        edtCreatePlaylist = (EditText) findViewById(R.id.edt_create_playlist);
        btnCreatePlaylist = (Button) findViewById(R.id.btn_Create);

        imgButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playlistName = edtCreatePlaylist.getText().toString();
                if (playlistName.trim().isEmpty()) {
                    Toast.makeText(CreatePlaylistActivity.this, "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        String yourName = currentUser.getDisplayName();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("playlistName", playlistName);
                        resultIntent.putExtra("userId", userId);
                        resultIntent.putExtra("yourName", yourName);

                        // Đặt kết quả và kết thúc hoạt động
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        // Người dùng chưa đăng nhập, xử lý theo ý của bạn
                    }
                }

            }
        });
    }
}