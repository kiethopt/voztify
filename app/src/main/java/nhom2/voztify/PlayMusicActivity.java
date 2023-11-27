package nhom2.voztify;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;

import nhom2.voztify.Class.Track;


public class PlayMusicActivity extends AppCompatActivity {
    private List<Track> trackList;
    private Timer timer;
    private Track track;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private ImageView imgViewSong;
    private TextView songTitleTextView, songArtistTextView, startTime, endTime;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        handler = new Handler();

        track = (Track) getIntent().getSerializableExtra("Track");

        ImageButton minimizeButton = findViewById(R.id.minimizeButton);
        minimizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });

        imgViewSong = findViewById(R.id.imgViewSong);
        Picasso.get()
                .load(track.getAlbum().getCover_medium())
                .placeholder(R.drawable.placeholder_img)
                .into(imgViewSong);

        songTitleTextView = findViewById(R.id.songTitleTextView);
        songTitleTextView.setText(track.getTitle());

        songArtistTextView = findViewById(R.id.songArtistTextView);
        songArtistTextView.setText(track.getArtist().getName());

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        endTime.setText(formatDuration(30));

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress * 1000); // Convert seconds to milliseconds
                    startTime.setText(formatDuration(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(track.getPreviewUrl()));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    handler.post(UpdateSongTime);
                }
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        playPauseButton = findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayPause();
            }
        });
    }

    public static String formatDuration(int durationInSeconds) {
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    private void togglePlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setImageResource(R.drawable.ic_play);
        } else {
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.ic_pause);
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int elapsedTimeInSeconds = currentPosition / 1000; // Convert to seconds

            startTime.setText(formatDuration(elapsedTimeInSeconds));
            seekBar.setProgress(elapsedTimeInSeconds);

            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = null;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}