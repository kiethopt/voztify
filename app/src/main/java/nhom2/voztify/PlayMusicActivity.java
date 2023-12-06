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
    import android.widget.Toast;
    
    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ServerValue;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.squareup.picasso.Picasso;
    
    import java.util.List;

    import nhom2.voztify.Class.History;
    import nhom2.voztify.Class.Track;
    
    
    public class PlayMusicActivity extends AppCompatActivity {
        private List<Track> trackList;
        private Track track;
        private Handler handler;
        private MediaPlayer mediaPlayer;
        private ImageButton playPauseButton, minimizeButton, previousButton, nextButton;
        private ImageView imgViewSong;
        private TextView songTitleTextView, songArtistTextView, startTime, endTime;
        private SeekBar seekBar;
        private String currentTrackTitle; // Added variable to keep track of the current track title

    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_play_music);
    
            //Add Controls & Events
            addControls();
            addEvents();

            //Set Data
            handler = new Handler();
    
            track = (Track) getIntent().getSerializableExtra("Track");
            trackList = (List<Track>) getIntent().getSerializableExtra("TracksList");
            currentTrackTitle = track.getTitle();
    
            if (track != null && track.getMd5_image() != null) {
                String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/" + track.getMd5_image() + "/250x250-000000-80-0-0.jpg";
    
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_img)
                        .into(imgViewSong);
            } else {
                // Xử lý trường hợp không có hình ảnh thì thay bằng ảnh vinyl
                imgViewSong.setImageResource(R.drawable.silver);
            }
    
    
            songTitleTextView.setText(track.getTitle());
    
            songArtistTextView.setText(track.getArtist().getName());
    
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(track.getPreviewUrl()));
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        playPauseButton.setImageResource(R.drawable.ic_pause);
                        seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        endTime.setText(formatDuration(mediaPlayer.getDuration()/1000));
                        handler.post(UpdateSongTime);
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        public void addControls(){
            playPauseButton = findViewById(R.id.playPauseButton);
            minimizeButton = findViewById(R.id.minimizeButton);
            startTime = findViewById(R.id.startTime);
            endTime = findViewById(R.id.endTime);
            seekBar = findViewById(R.id.seekBar);
            imgViewSong = findViewById(R.id.imgViewSong);
            songTitleTextView = findViewById(R.id.songTitleTextView);
            previousButton = findViewById(R.id.previousButton);
            nextButton = findViewById(R.id.nextButton);
            songArtistTextView = findViewById(R.id.songArtistTextView);
        }
    
        public void addEvents() {
            minimizeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
    
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress * 1000);
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
    
            playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    togglePlayPause();
                }
            });

            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPreviousTrack();
                }
            });
    
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadNextTrack();
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
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int elapsedTimeInSeconds = currentPosition / 1000; // Convert to seconds
                    startTime.setText(formatDuration(elapsedTimeInSeconds));
                    seekBar.setProgress(elapsedTimeInSeconds);
                    handler.postDelayed(this, 100);
                }
            }
        };
    
        private void loadPreviousTrack() {
            int currentIndex = -1;
    
            for (int i = 0; i < trackList.size(); i++) {
                if (trackList.get(i).getTitle().equalsIgnoreCase(currentTrackTitle)) {
                    currentIndex = i;
                    break;
                }
            }
    
            if (currentIndex > 0) {
                Track previousTrack = trackList.get(currentIndex - 1);
                currentTrackTitle = previousTrack.getTitle();
                startNewTrack(previousTrack);
            } else {
                Track lastTrack = trackList.get(trackList.size() - 1);
                currentTrackTitle = lastTrack.getTitle();
                startNewTrack(lastTrack);
            }
        }
    
        private void loadNextTrack() {
            int currentIndex = -1;
    
            for (int i = 0; i < trackList.size(); i++) {
                if (trackList.get(i).getTitle().equalsIgnoreCase(currentTrackTitle)) {
                    currentIndex = i;
                    break;
                }
            }
    
            if (currentIndex >= 0 && currentIndex < trackList.size() - 1) {
                Track nextTrack = trackList.get(currentIndex + 1);
                currentTrackTitle = nextTrack.getTitle();
                startNewTrack(nextTrack);
            } else if (currentIndex == trackList.size() - 1) {
                Track firstTrack = trackList.get(0);
                currentTrackTitle = firstTrack.getTitle();
                startNewTrack(firstTrack);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid track index", Toast.LENGTH_SHORT).show();
            }
        }
    
        private void startNewTrack(Track newTrack) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            }
    
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(newTrack.getPreviewUrl()));
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        playPauseButton.setImageResource(R.drawable.ic_pause);
                        seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        endTime.setText(formatDuration(mediaPlayer.getDuration() / 1000));
                        handler.post(UpdateSongTime);
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            // Cập nhật lại UI cho mới.
            updateUI(newTrack);
        }
    
        private void updateUI(Track track) {
            songTitleTextView.setText(track.getTitle());
            songArtistTextView.setText(track.getArtist().getName());
    
            if (track.getMd5_image() != null) {
                String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/" + track.getMd5_image() + "/250x250-000000-80-0-0.jpg";
                Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_img).into(imgViewSong);
            } else {
                imgViewSong.setImageResource(R.drawable.silver);
            }
        }
    
        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = null;
        }



    }