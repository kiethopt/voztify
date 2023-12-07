package nhom2.voztify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nhom2.voztify.Api.DZService;
import nhom2.voztify.Api.DeezerService;
import nhom2.voztify.Api.SpotifyApi;
import nhom2.voztify.Class.Artist;
import nhom2.voztify.Class.History;
import nhom2.voztify.Class.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewTopRadio;
    private RadioAdapter radioAdapter;
    private RecyclerView recyclerViewTopArtist, recyclerViewTopTracks;
    private ArtistAdapter artistAdapter;
    private List<Artist> artistList;
    private List<TopRadioGenre> topRadioGenresData;
    private TopRadioGenresAdapter topRadioGenresAdapter;

    private TextView emailTextView;
    private TextView nameTextView;
    private TextView dateJoinedTextView;
    private ImageView editProfileImageView, profilePhoto;
    private String userName;
    private TextView bioTextView;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private TracksAdapter tracksAdapter;
    private List<Track> trackList;
    private Context context;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    // Thêm tên của SharedPreferences
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PERMISSION = 123;
    private static final int REQUEST_CODE_PLAY_MUSIC = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        emailTextView = view.findViewById(R.id.emailTextView);
        nameTextView = view.findViewById(R.id.nameTextView);
        dateJoinedTextView = view.findViewById(R.id.dateJoinedTextView);
        editProfileImageView = view.findViewById(R.id.editProfileImageView);
        bioTextView = view.findViewById(R.id.bioTextView);
        profilePhoto = view.findViewById(R.id.profilePhoto);
        recyclerViewTopArtist =view.findViewById(R.id.recyclerViewTopArtist);
        recyclerViewTopRadio = view.findViewById(R.id.recyclerViewTopRadio);
        recyclerViewTopTracks = view.findViewById(R.id.recyclerViewTopTracks);
        context = getContext();

        LinearLayoutManager layoutManagerTracks = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopTracks.setLayoutManager(layoutManagerTracks);

        LinearLayoutManager layoutManagerRadio = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopRadio.setLayoutManager(layoutManagerRadio);

        // Initialize RecyclerView, data list, and adapter
        RecyclerView recyclerViewTopGenres = view.findViewById(R.id.rvTopGenres);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopGenres.setLayoutManager(layoutManager);

        // Initialize the data list and the adapter
        topRadioGenresData = new ArrayList<>();
        topRadioGenresAdapter = new TopRadioGenresAdapter(topRadioGenresData);
        recyclerViewTopGenres.setAdapter(topRadioGenresAdapter);

        // Cấp quyền để hình ảnh không mất khi đăng nhập lại
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Quyền đã được cấp, tiến hành xử lý tương tác với tệp hình ảnh ở đây
        } else {
            // Nếu quyền chưa được cấp, bạn có thể yêu cầu quyền từ người dùng
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        //--------------------------------------------------

        // Gọi API để lấy danh sách Top Radio
        loadTopRadio();
        fetchArtists();
        fetchTopRadioGenres();
        fetchTracks();


        // Xóa dữ liệu cũ từ SharedPreferences
        clearSharedPreferences();

        // Hiển thị thông tin người dùng mới
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayUserInfo(user.getUid());

            // Log the saved image URI for debugging
            String savedImageUri = loadImageUriFromSharedPreferences(user.getUid());
                Log.d("HomeFragment", "Saved Image URI: " + savedImageUri);
        }
        editProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileActivity();
            }
        });

        return view;
    }

    // Cấp quyền để hình ảnh không mất khi đăng nhập lại
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            // Handle the result of the permission request
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with handling interactions with image files here
            } else {
                // Permission not granted, you can notify the user or perform other actions
            }
        }
    }
    //---------------------------------------------------

    // Thêm phương thức để xóa dữ liệu từ SharedPreferences
    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void openEditProfileActivity() {
        startActivityForResult(new Intent(getActivity(), EditProfileActivity.class), EDIT_PROFILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            // Lấy đường dẫn hình ảnh mới từ Intent và cập nhật giao diện
            String updatedProfileImage = data.getStringExtra("updatedProfileImage");
            if (updatedProfileImage != null) {
                // Load hình ảnh mới vào ImageView sử dụng Picasso hoặc Glide
                Picasso.get().load(updatedProfileImage).into(profilePhoto);

                // Lưu đường dẫn hình ảnh vào SharedPreferences
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    saveImageUriToSharedPreferences(user.getUid(), updatedProfileImage);
                }
            }

            // Lấy các thông tin văn bản đã cập nhật
            String updatedName = data.getStringExtra("updatedName");
            String updatedBio = data.getStringExtra("updatedBio");
            // Lấy các thông tin khác nếu cần

            // Cập nhật giao diện người dùng với thông tin văn bản đã cập nhật
            if (updatedName != null) {
                nameTextView.setText(updatedName);
            }
            if (updatedBio != null) {
                bioTextView.setText(updatedBio);
            }
            // Cập nhật các thông tin khác nếu cần
        }
    }

    private void displayUserInfo(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userName = dataSnapshot.child("name").getValue(String.class);

                    emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    nameTextView.setText(userName);
                    bioTextView.setText(dataSnapshot.child("bio").getValue(String.class));

                    // Hiển thị ngày tham gia
                    dateJoinedTextView.setText(getFormattedDateJoined(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp()));

                    // Load the image URL directly from Firebase and display
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).into(profilePhoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
            }
        });
    }

    private String getFormattedDateJoined(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(timestamp));
    }

    private void saveImageUriToSharedPreferences(String userId, String imageUri) {
        // Lưu đường dẫn ảnh vào SharedPreferences với key là userId
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUri_" + userId, imageUri);
        editor.apply();
    }

    private String loadImageUriFromSharedPreferences(String userId) {
        // Đọc đường dẫn ảnh từ SharedPreferences với key là userId
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString("profileImageUri_" + userId, null);
    }

    // ==============Top Track =================
    private void fetchTracks() {
        DZService service = DeezerService.getService();
        Call<JsonObject> call = service.getTopTracks(); // Assume JsonObject as the response type

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    if (jsonResponse.has("tracks")) {
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("tracks");

                        TrackResponse trackData = new Gson().fromJson(tracksObject, TrackResponse.class);
                        trackList = trackData.getTracks();
                        updateTrackListView(trackList);

                        recyclerViewTopTracks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    } else {
                        Log.e("TrackFragment", "Response does not contain 'tracks' field");
                    }
                } else {
                    try {
                        Log.e("TrackFragment", "Error fetching tracks: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TrackFragment", "Error fetching tracks: " + t.getMessage());
            }
        });
    }



    private void updateTrackListView(List<Track> tracks) {

        Collections.reverse(tracks);
        tracksAdapter = new TracksAdapter(context, tracks, new TracksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Track track = tracks.get(position);

                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                intent.putExtra("Track", track);
                intent.putExtra("TracksList", (Serializable) trackList);
                startActivityForResult(intent, REQUEST_CODE_PLAY_MUSIC);
            }
        });

        recyclerViewTopTracks.setAdapter(tracksAdapter);
    }
    //===================top radio================
    private void loadTopRadio() {
        DZService dzService = DeezerService.getService();
        Call<DeezerResponse> call = dzService.getTopRadios();

        call.enqueue(new Callback<DeezerResponse>() {
            @Override
            public void onResponse(Call<DeezerResponse> call, Response<DeezerResponse> response) {
                if (response.isSuccessful()) {
                    DeezerResponse deezerResponse = response.body();
                    if (deezerResponse != null) {
                        // Hiển thị danh sách Top Radio trong RecyclerView
                        List<DeezerRadio> topRadios = deezerResponse.getData();

                        // Reverse the order of the list
                        Collections.reverse(topRadios);

                        radioAdapter = new RadioAdapter(getContext(), topRadios);
                        recyclerViewTopRadio.setAdapter(radioAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeezerResponse> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
    }


    // ============================== TOP GENRES RADIO =================================
    private void fetchTopRadioGenres() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.deezer.com/radio");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse JSON and get top radio genres
                        List<TopRadioGenre> topRadioGenres = parseTopRadioGenres(response.toString());

                        // Update UI on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                topRadioGenresData.addAll(topRadioGenres);
                                topRadioGenresAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<TopRadioGenre> parseTopRadioGenres(String json) {
        List<TopRadioGenre> topRadioGenres = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = data.length() - 1; i >= 0; i--) {
                JSONObject radioGenreObject = data.getJSONObject(i);
                String id = radioGenreObject.getString("id");
                String title = radioGenreObject.getString("title");
                String pictureUrl = radioGenreObject.getString("picture");

                TopRadioGenre topRadioGenre = new TopRadioGenre(id, title, pictureUrl);
                topRadioGenres.add(topRadioGenre);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topRadioGenres;
    }


    // ================================TOP GENRE ARTIST===============================================
    private void fetchArtists() {
        DZService service = DeezerService.getService();
        Call<JsonObject> call = service.getTopArtists();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    if (jsonResponse.has("artists")) {
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("artists");

                        ArtistResponse artistData = new Gson().fromJson(tracksObject, ArtistResponse.class);
                        artistList = artistData.getArtists();
                        updateListView(artistList);
                        recyclerViewTopArtist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                    } else {
                        Log.e("ArtistFragment", "Response does not contain 'artists' field");
                    }
                } else {
                    try {
                        Log.e("ArtistFragment", "Error fetching artists: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ArtistFragment", "Error fetching artists: " + t.getMessage());
            }
        });
    }
    private void updateListView(List<Artist> artists) {
        Collections.reverse(artists);
        artistAdapter = new ArtistAdapter(getContext(), artists, new ArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Artist artist = artists.get(position);

                Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
                intent.putExtra("artist", artist);
                startActivity(intent);
            }
        });

        recyclerViewTopArtist.setAdapter(artistAdapter);
    }
    // ===============================================================================

    @Override
    public void onResume() {
        super.onResume();


        // Load the image URI from SharedPreferences each time the fragment is resumed
        String savedImageUri = loadImageUriFromSharedPreferences(userId);
        if (savedImageUri != null && !savedImageUri.isEmpty()) {
            Picasso.get().load(savedImageUri).into(profilePhoto);
        }
    }
}
