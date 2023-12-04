package nhom2.voztify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText edtEmailOrPhoneNum, edtPass;
    TextView tvFPass;
    Button btnLogin;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private boolean shouldGoToRegister = true;

    public static final String SHARE_PREFS = "sharedPrefs";
    public static final String LOGIN_STATE_KEY = "login_state";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmailOrPhoneNum = findViewById(R.id.edtEmailOrPhoneNum);
        edtPass = findViewById(R.id.edtPass);
        tvFPass = findViewById(R.id.tvFPass);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);

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

//
//        // Kiểm tra đã đăng nhập và thời gian hết hạn
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
//        String check = sharedPreferences.getString(LOGIN_STATE_KEY, "");
//        long expirationTime = sharedPreferences.getLong("EXPIRATION_TIME", 0);
//
//        if (check.equals("true") && System.currentTimeMillis() < expirationTime) {
//            // Nếu trạng thái đăng nhập còn hiệu lực, chuyển đến HomeActivity
//            Toast.makeText(this, "Log in Success!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, HomeActivity.class));
//            finish();
//        }

        // btn đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        // btn Quên pass
        tvFPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, LoginWithoutPassWordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }
    // Xử lý sự kiện khi nút back được nhấn
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }

    //Xử lý Log in
    public void LogIn(){
        String user = edtEmailOrPhoneNum.getText().toString();
        String pass = edtPass.getText().toString();

        if (user.isEmpty() || pass.isEmpty() ) {
            Toast.makeText(Login.this, "Please enter the information !!!", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        saveLoginState();

                        Toast.makeText(Login.this, "Log in Success!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this , HomeActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Log in Fails"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void saveLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_STATE_KEY, "true");

//        // Lưu thời gian hết hạn (thời điểm hiện tại + 3 ngày)
//        long expirationTime = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000); // 3 ngày
//        editor.putLong("EXPIRATION_TIME", expirationTime);
        editor.apply();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shouldGoToRegister) {
            saveLoginState();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLoginState();
    }
}