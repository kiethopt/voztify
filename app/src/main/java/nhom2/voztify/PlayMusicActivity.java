package nhom2.voztify;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class PlayMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        // Retrieve data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            String songTitle = intent.getStringExtra("SONG_TITLE");
            String songArtist = intent.getStringExtra("SONG_ARTIST");
            String songImage = intent.getStringExtra("SONG_IMAGE");

        }
    }
}