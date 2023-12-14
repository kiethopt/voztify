package nhom2.voztify.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import nhom2.voztify.Controller.ArtistResponse;
import nhom2.voztify.Model.Artist;
import nhom2.voztify.Model.Track;
import nhom2.voztify.Controller.DeezerRadio;
import nhom2.voztify.Controller.DeezerResponse;
import nhom2.voztify.R;
import nhom2.voztify.Model.TopRadioGenre;
import nhom2.voztify.Controller.TrackResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    // Khai báo views và adapters
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

        // Khởi tạo views và RecyclerViews
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
        //clearSharedPreferences();

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

        profilePhoto.setOnClickListener(new View.OnClickListener() {
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
        // Yêu cầu quyền lưu trữ
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
//    private void clearSharedPreferences() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//    }

    private void openEditProfileActivity() {
        //getActivity() lấy fragment hiện tại
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
        // Tạo tham chiếu đến nút "users" trong Firebase Realtime Database với userId là con của nút "users"
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        // Thực hiện lắng nghe một lần duy nhất để đọc dữ liệu từ Firebase Realtime Database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem có dữ liệu tồn tại hay không
                if (dataSnapshot.exists()) {
                    // Lấy tên người dùng từ nút "name" trong dữ liệu Firebase
                    userName = dataSnapshot.child("name").getValue(String.class);
                    // Hiển thị thông tin người dùng trên giao diện
                    emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    nameTextView.setText(userName);
                    bioTextView.setText(dataSnapshot.child("bio").getValue(String.class));

                    // Hiển thị ngày tham gia
                    dateJoinedTextView.setText("Participation date: " + getFormattedDateJoined(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp()));

                    // Lấy URL hình ảnh trực tiếp từ Firebase và hiển thị nó sử dụng thư viện Picasso
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
        // Định dạng ngày tham gia sử dụng SimpleDateFormat với định dạng "dd/MM/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Sử dụng định dạng để chuyển đổi timestamp thành chuỗi ngày tham gia
        return sdf.format(new Date(timestamp));
    }

    private void saveImageUriToSharedPreferences(String userId, String imageUri) {
        /// Lưu đường dẫn ảnh vào SharedPreferences với key là "profileImageUri_" + userId
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUri_" + userId, imageUri);
        editor.apply();
    }

    private String loadImageUriFromSharedPreferences(String userId) {
        // Đọc đường dẫn ảnh từ SharedPreferences với key là "profileImageUri_" + userId
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // Trả về đường dẫn ảnh hoặc null nếu không tìm thấy
        return sharedPreferences.getString("profileImageUri_" + userId, null);
    }

    // =============================== TOP TRACKS ===================================================
    private void fetchTracks() {
        // Tạo một đối tượng DZService từ DeezerService
        DZService service = DeezerService.getService();
        Call<JsonObject> call = service.getTopTracks(); // Giả sử JsonObject là kiểu dữ liệu phản hồi
        // Gọi API để lấy danh sách Top Tracks
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Kiểm tra xem cuộc gọi API có thành công không và phản hồi có dữ liệu không
                if (response.isSuccessful() && response.body() != null) {
                    // Lấy dữ liệu JSON từ phản hồi
                    JsonObject jsonResponse = response.body();
                    // Kiểm tra xem JSON có chứa trường "tracks" không
                    if (jsonResponse.has("tracks")) {
                        // Lấy đối tượng JSON chứa danh sách các track
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("tracks");
                        // Chuyển đổi JSON thành đối tượng TrackResponse bằng Gson
                        TrackResponse trackData = new Gson().fromJson(tracksObject, TrackResponse.class);
                        // Lấy danh sách các Track từ đối tượng TrackResponse
                        trackList = trackData.getTracks();
                        // Method Cập nhật RecyclerView hiển thị danh sách Track
                        updateTrackListView(trackList);
                        // Đặt bố cục của RecyclerView là LinearLayoutManager theo chiều ngang
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
                // Xử lý khi có lỗi trong quá trình gọi API
                Log.e("TrackFragment", "Error fetching tracks: " + t.getMessage());
            }
        });
    }



    private void updateTrackListView(List<Track> tracks) {
        // Đảo ngược danh sách các Track
        Collections.reverse(tracks);
        // Tạo adapter cho RecyclerView sử dụng danh sách các Track và thiết lập sự kiện khi một Track được chọn
        tracksAdapter = new TracksAdapter(context, tracks, new TracksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Xử lý sự kiện khi một Track được chọn
                Track track = tracks.get(position);
                // Tạo Intent để mở PlayMusicActivity và chuyển dữ liệu Track và danh sách Track
                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                intent.putExtra("Track", track);
                intent.putExtra("TracksList", (Serializable) trackList);
                // Gọi startActivityForResult để có thể nhận kết quả từ PlayMusicActivity
                startActivityForResult(intent, REQUEST_CODE_PLAY_MUSIC);
            }
        });

        TracksAdapter.updateTextSize(getContext(),20);
        TracksAdapter.updateTextGravity(getContext(), Gravity.CENTER);
        TracksAdapter.updateSongArtistVisibility(getContext(), View.VISIBLE);

        recyclerViewTopTracks.setAdapter(tracksAdapter);
    }
    //======================================================================================================
    //==================================== TOP RADIO ===================================================================
    private void loadTopRadio() {
        // Tạo đối tượng DZService từ DeezerService
        DZService dzService = DeezerService.getService();
        // Gọi API để lấy danh sách Top Radios
        Call<DeezerResponse> call = dzService.getTopRadios();

        call.enqueue(new Callback<DeezerResponse>() {
            @Override
            // Xử lý khi cuộc gọi API thành công
            public void onResponse(Call<DeezerResponse> call, Response<DeezerResponse> response) {
                if (response.isSuccessful()) {
                    // Lấy đối tượng DeezerResponse từ phản hồi
                    DeezerResponse deezerResponse = response.body();
                    // Kiểm tra xem deezerResponse có khác null không
                    if (deezerResponse != null) {
                        // Lấy danh sách Top Radios từ deezerResponse
                        List<DeezerRadio> topRadios = deezerResponse.getData();

                        // Đảo ngược thứ tự của danh sách
                        Collections.reverse(topRadios);
                        // Tạo adapter cho RecyclerView và đặt adapter cho RecyclerView
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

    //======================================================================================================
    // ============================== TOP GENRES RADIO ===================================================================
    // Phương thức này gọi API để lấy danh sách Top Genres Radio từ Deezer
    private void fetchTopRadioGenres() {
        // Sử dụng một luồng mới để thực hiện cuộc gọi mạng, tránh chặn luồng chính
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Tạo URL cho API Deezer Radio
                    URL url = new URL("https://api.deezer.com/radio");
                    // Mở kết nối tới URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    // Lấy mã phản hồi từ kết nối
                    int responseCode = conn.getResponseCode();
                    // Kiểm tra xem kết nối thành công (HTTP_OK) hay không
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Đọc dữ liệu từ InputStream
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Gọi phương thức parseTopRadioGenres để chuyển đổi dữ liệu JSON thành danh sách TopRadioGenre
                        List<TopRadioGenre> topRadioGenres = parseTopRadioGenres(response.toString());

                        // Cập nhật giao diện người dùng trên luồng chính
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
        }).start(); // Khởi động luồng mới
    }
    // Phương thức này chuyển đổi dữ liệu JSON thành danh sách các đối tượng TopRadioGenre
    private List<TopRadioGenre> parseTopRadioGenres(String json) {
        List<TopRadioGenre> topRadioGenres = new ArrayList<>();
        try {
            // Tạo đối tượng JSONObject từ chuỗi JSON
            JSONObject jsonObject = new JSONObject(json);
            // Lấy mảng dữ liệu có tên là "data" từ đối tượng JSON
            JSONArray data = jsonObject.getJSONArray("data");
            // Lặp qua mỗi đối tượng trong mảng data
            for (int i = data.length() - 1; i >= 0; i--) {
                // Lấy đối tượng JSON tại vị trí i
                JSONObject radioGenreObject = data.getJSONObject(i);
                // Lấy thông tin cần thiết (id, title, pictureUrl) từ đối tượng JSON
                String id = radioGenreObject.getString("id");
                String title = radioGenreObject.getString("title");
                String pictureUrl = radioGenreObject.getString("picture");
                // Tạo đối tượng TopRadioGenre và thêm vào danh sách
                TopRadioGenre topRadioGenre = new TopRadioGenre(id, title, pictureUrl);
                topRadioGenres.add(topRadioGenre);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topRadioGenres;
    }


    // ================================ TOP GENRE ARTIST ================================================================
    private void fetchArtists() {
        // Khởi tạo một đối tượng DZService từ DeezerService
        DZService service = DeezerService.getService();
        // Gọi API để lấy danh sách các nghệ sĩ hàng đầu
        Call<JsonObject> call = service.getTopArtists();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Kiểm tra xem response có thành công và có dữ liệu không
                if (response.isSuccessful() && response.body() != null) {
                    // Chuyển đổi response thành đối tượng JsonObject
                    JsonObject jsonResponse = response.body();
                    // Kiểm tra xem có trường "artists" trong JsonObject không
                    if (jsonResponse.has("artists")) {
                        // Lấy đối tượng Json chứa danh sách nghệ sĩ
                        JsonObject tracksObject = jsonResponse.getAsJsonObject("artists");
                        // Chuyển đối tượng Json thành đối tượng ArtistResponse sử dụng Gson
                        ArtistResponse artistData = new Gson().fromJson(tracksObject, ArtistResponse.class);
                        // Lấy danh sách nghệ sĩ từ đối tượng ArtistResponse
                        artistList = artistData.getArtists();
                        // Cập nhật giao diện danh sách nghệ sĩ
                        updateListView(artistList);
                        // Thiết lập layout manager cho RecyclerView hiển thị danh sách nghệ sĩ
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
    // Cập nhật giao diện danh sách nghệ sĩ trong RecyclerView
    private void updateListView(List<Artist> artists) {
        // Đảo ngược danh sách nghệ sĩ để hiển thị từ mới nhất đến cũ nhất
        Collections.reverse(artists);
        // Khởi tạo Adapter và thiết lập OnItemClickListener cho RecyclerView
        artistAdapter = new ArtistAdapter(getContext(), artists, new ArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Artist artist = artists.get(position);
                // Tạo Intent để mở Activity chi tiết nghệ sĩ và chuyển thông tin nghệ sĩ qua Intent
                Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
                intent.putExtra("artist", artist);
                startActivity(intent);
            }
        });
        // Thiết lập Adapter cho RecyclerView
        recyclerViewTopArtist.setAdapter(artistAdapter);
    }
    // =================================================================================================================

    @Override
    public void onResume() {
        super.onResume();
        // Khi Fragment được resumed, ta load đường dẫn ảnh từ SharedPreferences
        String savedImageUri = loadImageUriFromSharedPreferences(userId);
        // Nếu có đường dẫn ảnh đã lưu, ta sử dụng thư viện Picasso để load ảnh vào ImageView (profilePhoto)
        if (savedImageUri != null && !savedImageUri.isEmpty()) {
            Picasso.get().load(savedImageUri).into(profilePhoto);
        }
    }
}
