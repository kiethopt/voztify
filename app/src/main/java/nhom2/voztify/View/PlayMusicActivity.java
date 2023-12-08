    package nhom2.voztify.View;

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

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ServerValue;
    import com.squareup.picasso.Picasso;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;

    import nhom2.voztify.Model.SongForU;
    import nhom2.voztify.Model.Track;
    import nhom2.voztify.R;


    public class PlayMusicActivity extends AppCompatActivity {
        private List<Track> trackList;
        private Track track;
        private Handler handler;
        private MediaPlayer mediaPlayer;
        private ImageButton playPauseButton, minimizeButton, previousButton, nextButton;
        private ImageView imgViewSong;
        private TextView songTitleTextView, songArtistTextView, startTime, endTime;
        private SeekBar seekBar;
        private ImageButton repeatButton, shuffleButton;

        private boolean isRepeat = false;
        private boolean isShuffle = false;
        private String currentTrackTitle; // Added variable to keep track of the current track title
        // For Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

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

            // Check if trackList is null
            if (trackList == null) {
                // Initialize trackList and add the track
                trackList = new ArrayList<>();
                trackList.add(track);
            } else {
                // Ensure track is not null before adding
                if (track != null) {
                    trackList.add(track);
                }
            }

            currentTrackTitle = track.getTitle();
            // Update history for the current track
            updateHistory(track, userId);
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
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playPauseButton.setImageResource(R.drawable.ic_play);
                        if (isRepeat) {
                            mediaPlayer.seekTo(0);
                            mediaPlayer.start();
                        } else {
                            loadNextTrack();
                        }
                    }
                });
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
            repeatButton = findViewById(R.id.repeatButton);
            shuffleButton = findViewById(R.id.shuffleButton);

            repeatButton.setImageResource(isRepeat ? R.drawable.repeat_on : R.drawable.repeat_off);
            shuffleButton.setImageResource(isShuffle ? R.drawable.shuffle_on : R.drawable.shuffle_off);
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

            repeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleRepeat();
                }
            });

            shuffleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleShuffle();
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

            if (isShuffle) {
                if (currentIndex >= 0) {
                    int randomIndex = new Random().nextInt(trackList.size());
                    while (randomIndex == currentIndex) {
                        randomIndex = new Random().nextInt(trackList.size());
                    }
                    Track randomTrack = trackList.get(randomIndex);
                    currentTrackTitle = randomTrack.getTitle();
                    startNewTrack(randomTrack);
                    updateHistory(randomTrack, userId);
                }
            }else{
                if (currentIndex > 0) {
                    Track previousTrack = trackList.get(currentIndex - 1);
                    currentTrackTitle = previousTrack.getTitle();
                    startNewTrack(previousTrack);
                    updateHistory(previousTrack,userId);
                } else {
                    Track lastTrack = trackList.get(trackList.size() - 1);
                    currentTrackTitle = lastTrack.getTitle();
                    startNewTrack(lastTrack);
                    updateHistory(lastTrack,userId);
                }
            }
        }

        private void loadNextTrack() {
            int currentIndex = 1;

            for (int i = 0; i < trackList.size(); i++) {
                if (trackList.get(i).getTitle().equalsIgnoreCase(currentTrackTitle)) {
                    currentIndex = i;
                    break;
                }
            }

            if (isShuffle) {
                if (currentIndex >= 0) {
                    int randomIndex = new Random().nextInt(trackList.size());
                    while (randomIndex == currentIndex) {
                        randomIndex = new Random().nextInt(trackList.size());
                    }
                    Track randomTrack = trackList.get(randomIndex);
                    currentTrackTitle = randomTrack.getTitle();
                    startNewTrack(randomTrack);
                    updateHistory(randomTrack, userId);
                }
            } else {
                if (currentIndex >= 0 && currentIndex < trackList.size() - 1) {
                    Track nextTrack = trackList.get(currentIndex + 1);
                    currentTrackTitle = nextTrack.getTitle();
                    startNewTrack(nextTrack);
                    updateHistory(nextTrack,userId); // Update history for the next track
                } else if (currentIndex == trackList.size() - 1) {
                    Track firstTrack = trackList.get(0);
                    currentTrackTitle = firstTrack.getTitle();
                    startNewTrack(firstTrack);
                    updateHistory(firstTrack,userId); // Update history for the first track
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid track index", Toast.LENGTH_SHORT).show();
                }
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

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playPauseButton.setImageResource(R.drawable.ic_play);
                        if (isRepeat) {
                            mediaPlayer.seekTo(0);
                            mediaPlayer.start();
                        } else {
                            loadNextTrack();
                        }
                    }
                });

                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            // Cập nhật lại UI cho mới.
            updateUI(newTrack);
            updateHistory(newTrack,userId);
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
        private void updateHistory(Track track, String userId) {
            // Get the current timestamp
            String historyId = databaseReference.child("user_history").child(userId).push().getKey();
            String historyIde = track.getId();

            // Create a SongForU object
            SongForU songForU = new SongForU(track.getTitle(), track.getArtist().getName(),track.getMd5_image(), ServerValue.TIMESTAMP);

            // Save the SongForU object to Firebase
            databaseReference.child(userId).child("user_history").child(historyIde).setValue(songForU);
        }

        private void toggleRepeat() {
            isRepeat = !isRepeat;
            repeatButton.setImageResource(isRepeat ? R.drawable.repeat_on : R.drawable.repeat_off);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isRepeat) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    } else {
                        // Handle next track or stop playback
                    }
                }
            });
        }

        private void toggleShuffle() {
            isShuffle = !isShuffle;
            shuffleButton.setImageResource(isShuffle ? R.drawable.shuffle_on : R.drawable.shuffle_off);

            currentTrackTitle = track.getTitle();
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