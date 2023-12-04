package nhom2.voztify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nhom2.voztify.Api.SpotifyApi;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewTopRadioGenres;
    private List<TopRadioGenre> topRadioGenresData;
    private TopRadioGenresAdapter topRadioGenresAdapter;


    private TextView emailTextView;
    private TextView nameTextView;
    private TextView dateJoinedTextView;
    private ImageView editProfileImageView, profilePhoto;
    private String userName;
    private TextView bioTextView;

    // Thêm tên của SharedPreferences
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;

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

        // Initialize RecyclerView, data list, and adapter
        RecyclerView recyclerViewTopRadioGenres = view.findViewById(R.id.rvTopRadioGenres);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopRadioGenres.setLayoutManager(layoutManager);

        // Initialize the data list and the adapter
        topRadioGenresData = new ArrayList<>();
        topRadioGenresAdapter = new TopRadioGenresAdapter(topRadioGenresData);
        recyclerViewTopRadioGenres.setAdapter(topRadioGenresAdapter);

        fetchTopRadioGenres();



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


    // ============================== TOP GENRES - API =================================
    // Thêm vào HomeFragment.java
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
            for (int i = 0; i < data.length(); i++) {
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

    // ===============================================================================


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
}
