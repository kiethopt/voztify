package nhom2.voztify;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nhom2.voztify.Class.User;

public class SignUp extends AppCompatActivity {

    EditText edtMail, edtPhoneNum, edtCreatePass, edtConfirmPass, edtBirth, edtName;
    Spinner spinnerGender;
    Button btnCreateAcc;
    FirebaseAuth mAuth;
    public User users;
    public String gend;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        // Tắt tiêu đề mặc định của ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set custom drawable as the navigation icon
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_ios_24);
        // Xử lý sự kiện khi nút back được nhấn
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Tìm ID cho các thành phần giao diện người dùng
        edtMail = findViewById(R.id.edtMail);
        edtCreatePass = findViewById(R.id.edtCreatePass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        edtBirth = findViewById(R.id.edtBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        edtName = findViewById(R.id.edtName);
        edtPhoneNum = findViewById(R.id.edtPhoneNum);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //Spinner Gender
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gend = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //EdittExt chọn ngày sinh
        edtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //button đăng ký
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // Hiển thị DatePickerDialog để chọn ngày sinh
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edtBirth.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    // Xử lý sự kiện khi nút back được nhấn
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    // Xử lý sự kiện Đăng ký tài khoản
    public void signUp() {
        String email = edtMail.getText().toString();
        String password = edtCreatePass.getText().toString();
        String confirmPassword = edtConfirmPass.getText().toString();
        String birthDate = edtBirth.getText().toString();
        String name = edtName.getText().toString();
        String phone = edtPhoneNum.getText().toString();

        if (confirmPassword.isEmpty() || birthDate.isEmpty() || name.isEmpty()) {
            Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(SignUp.this, "Please fill your Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(SignUp.this, "Please fill your Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUp.this, "Confirm Password Wrong !!!", Toast.LENGTH_SHORT).show();
            edtConfirmPass.requestFocus();
        } else {
           checkPhoneNumberExists(phone,email, password, name,birthDate);
        }
    }

    private void checkPhoneNumberExists(String phone, String email, String password, String name, String birthDate){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.orderByChild("phoneNum").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Phone number is already in use
                    Toast.makeText(SignUp.this, "Phone number is already in use", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                users = new User(email, name, phone, birthDate, gend, password);
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(users);

                                Toast.makeText(SignUp.this, "Create Account Success !!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, Login.class));
                                finish();
                            } else {
                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
