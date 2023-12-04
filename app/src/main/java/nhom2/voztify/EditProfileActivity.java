package nhom2.voztify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private TextView emailTextView;
    private EditText nameEditText;
    private EditText bioEditText;
    private EditText phoneNumEditText;
    private EditText birthEditText;
    private Spinner genderSpinner;
    private Button saveChangesButton;

    // Move the adapter declaration here
    private ArrayAdapter<CharSequence> adapter;
    private ImageView profileImageView;
    private Uri selectedImageUri;  // Đường dẫn của ảnh đã chọn

    // New variable to store the image URI temporarily
    private Uri tempImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        emailTextView = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        phoneNumEditText = findViewById(R.id.phoneNumEditText);
        birthEditText = findViewById(R.id.birthEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        //Sự kiện đóng activity
        ImageView closeIcon = findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khi closeIcon được nhấn, đóng Activity
                finish();
            }
        });

        // Trong phương thức onCreate hoặc nơi thích hợp khác
        TextInputEditText nameEditText = findViewById(R.id.nameEditText);
        TextInputEditText bioEditText = findViewById(R.id.bioEditText);
        TextInputEditText phoneNumEditText = findViewById(R.id.phoneNumEditText);

        // Ràng buộc độ dài cho edtName (tối đa 10 kí tự)
        nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        // Ràng buộc độ dài cho edtBio (tối đa 30 kí tự)
        bioEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        // Ràng buộc độ dài cho edtSDT (tối đa 12 kí tự)
        phoneNumEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});


        // Initialize the adapter
        adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        displayUserInfo();

        // Đặt sự kiện click cho nút "Save Changes"
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        // Đặt sự kiện click cho ImageView đại diện
        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chọn ảnh từ thư viện
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");

                // Tạo một Intent để xử lý cả việc chọn ảnh
                Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Image");

                startActivityForResult(chooserIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Xử lý khi người dùng đã chọn ảnh từ thư viện hoặc chụp ảnh từ Camera
            tempImageUri = data.getData();
            profileImageView.setImageURI(tempImageUri);
        }
    }

    private void saveImageUriToFirebase() {
        if (tempImageUri != null) {
            // Lưu đường dẫn của ảnh vào Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child("profileImageUrl").setValue(tempImageUri.toString());
        }
    }


    private void displayUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        emailTextView.setText(user.getEmail());
                        nameEditText.setText(dataSnapshot.child("name").getValue(String.class));
                        bioEditText.setText(dataSnapshot.child("bio").getValue(String.class));
                        phoneNumEditText.setText(dataSnapshot.child("phoneNum").getValue(String.class));
                        birthEditText.setText(dataSnapshot.child("birth").getValue(String.class));

                        // Lấy giá trị giới tính từ Firebase và thiết lập cho Spinner
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        if (gender != null) {
                            int position = adapter.getPosition(gender);
                            genderSpinner.setSelection(position);
                        }

                        // Lấy đường dẫn hình ảnh từ Firebase và hiển thị
                        String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // Load và hiển thị hình ảnh trong profileImageView, bạn có thể sử dụng thư viện Picasso hoặc Glide để đơn giản hóa quá trình này
                            Picasso.get().load(profileImageUrl).into(profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                }
            });
        }
    }

    private void saveChanges() {
        String name = nameEditText.getText().toString();
        String bio = bioEditText.getText().toString();
        String phoneNum = phoneNumEditText.getText().toString();
        String birth = birthEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

            Map<String, Object> updatedInfo = new HashMap<>();
            updatedInfo.put("name", name);
            updatedInfo.put("bio", bio);
            updatedInfo.put("phoneNum", phoneNum);
            updatedInfo.put("birth", birth);
            updatedInfo.put("gender", gender);

            // Kiểm tra xem nếu có đường dẫn hình ảnh mới được chọn, thì mới lưu vào Firebase
            if (tempImageUri != null) {
                // Lưu đường dẫn của ảnh vào 'profileImageUrl'
                updatedInfo.put("profileImageUrl", tempImageUri.toString());

                // Thêm đường dẫn hình ảnh vào Intent để chuyển về HomeFragment
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedProfileImage", tempImageUri.toString());

                // Đặt kết quả và kết thúc Activity
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            saveImageUriToFirebase(); // Save the image URI to Firebase here
            userRef.updateChildren(updatedInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfileActivity.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();

                            // Tạo Intent để gửi thông tin đã cập nhật về HomeFragment
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updatedName", name);
                            resultIntent.putExtra("updatedBio", bio);
                            if (tempImageUri != null) {
                                // Save the image URL directly to the Firebase Realtime Database
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                userRef.child("profileImageUrl").setValue(tempImageUri.toString());
                            }

                            // Đặt kết quả và kết thúc Activity
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Failed to save changes. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
