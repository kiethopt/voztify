package nhom2.voztify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import nhom2.voztify.Class.User;

public class SignUp extends AppCompatActivity {

    EditText edtMail,edtPhoneNum,edtCreatePass,edtConfirmPass,edtBirth,edtGender,edtName;
    Spinner spinnerGender;
    Button btnCreateAcc;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    public User users;
    public String gend;

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
        // Tìm ID cho các EditText
        edtMail = findViewById(R.id.edtMail);
        edtCreatePass = findViewById(R.id.edtCreatePass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        edtBirth = findViewById(R.id.edtBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        edtName = findViewById(R.id.edtName);
        edtPhoneNum = findViewById(R.id.edtPhoneNum);
        // Tìm ID cho Button
        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Spinner Gender
        // Lấy danh sách giới tính từ mảng string
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Chọn layout cho dropdown list
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Đặt Adapter cho Spinner
        spinnerGender.setAdapter(adapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy giá trị được chọn từ Spinner
                String selectedGender = parent.getItemAtPosition(position).toString();
                // Làm gì đó với giới tính được chọn
                gend = selectedGender;

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

        //button đăng nhập
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // Xử lý sự kiện khi nút back được nhấn
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //Xử lý chọn ngày sinh
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edtBirth.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    // Xử lý đăng ký tài khoản
    public void signUp(){
        // Lấy dữ liệu từ các EditText
        String email = edtMail.getText().toString();
        String password = edtCreatePass.getText().toString();
        String confirmPassword = edtConfirmPass.getText().toString();
        String birthDate = edtBirth.getText().toString();
        String name = edtName.getText().toString();
        String phone = edtPhoneNum.getText().toString();
        // Kiểm tra xem có ô nào trống không
        if (   confirmPassword.isEmpty() || birthDate.isEmpty()  || name.isEmpty()) {
            // Hiển thị thông báo nếu có ô trống
            Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(SignUp.this, "Please fill your Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty() ) {
            Toast.makeText(SignUp.this, "Please fill your Password", Toast.LENGTH_SHORT).show();
            return;
        } else if(!password.equals(confirmPassword)) {
            Toast.makeText(SignUp.this, "Confirm Password Wrong !!!", Toast.LENGTH_SHORT).show();
            edtConfirmPass.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        users = new User(email,name,phone,birthDate,gend,password);
                        FirebaseUser user=mAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(users);

                        Toast.makeText(SignUp.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this,Login.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUp.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}