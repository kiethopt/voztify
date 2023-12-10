package nhom2.voztify.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nhom2.voztify.R;

public class Login extends AppCompatActivity {

    EditText edtEmailOrPhoneNum, edtPass;
    TextView tvFPass;
    Button btnLogin;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private boolean shouldGoToRegister = true;

    //Khai báo Viewpaker và handler
    private ViewPager viewPager;
    private IntroPagerAdapter introPagerAdapter;
    private Handler autoScrollHandler;
    private final long AUTO_SCROLL_DELAY = 3000;


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


        //kt đn

        //ViewPaker================================================================================

        viewPager = findViewById(R.id.viewPager);
        introPagerAdapter = new IntroPagerAdapter(this);
        viewPager.setAdapter(introPagerAdapter);

        // Thêm sự kiện lắng nghe để thay đổi hình nền khi lướt qua trang
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Làm gì đó khi trang được lướt
                changeBackground(position);
            }

            @Override
            public void onPageSelected(int position) {
                // Thay đổi hình nền dựa trên vị trí của trang hiện tại
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Làm gì đó khi trạng thái cuộn trang thay đổi
            }
        });
        startAutoScroll();

        //=====================================================================

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

    //Method của ViewPaker ================================================
    private void startAutoScroll() {
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = introPagerAdapter.getCount();

            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                viewPager.setCurrentItem(0);
            }

            // Schedule the next auto-scroll
            autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
        }
    };

    private void changeBackground(int position) {
        // Thay đổi hình nền dựa trên vị trí của trang hiện tại
        // Điều này có thể làm bằng cách thay đổi màu nền của RelativeLayout chứa ViewPager
        // Ví dụ:
        if (position == 0) {
            findViewById(R.id.relativeLayout).setBackgroundColor(getResources().getColor(R.color.background_intro_1));
        } else if (position == 1) {
            findViewById(R.id.relativeLayout).setBackgroundColor(getResources().getColor(R.color.background_intro_2));
        } else if (position == 2) {
            findViewById(R.id.relativeLayout).setBackgroundColor(getResources().getColor(R.color.background_intro_3));
        }
    }
    //================================================================================
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

        // Lưu thời gian hết hạn (thời điểm hiện tại + 3 ngày)
//        long expirationTime = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000); // 3 ngày
//        editor.putLong("EXPIRATION_TIME", expirationTime);
        editor.apply();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoScroll();
        saveLoginState();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAutoScroll();
        saveLoginState();
    }


}