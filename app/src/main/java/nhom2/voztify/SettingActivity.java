package nhom2.voztify;

import static nhom2.voztify.Login.LOGIN_STATE_KEY;
import static nhom2.voztify.Login.SHARE_PREFS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView settingsListView;
    Button Logout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbarSetting);
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


        settingsListView = findViewById(R.id.settingsListView);
        settingsListView.setAdapter(new SettingAdapter());
        // Handle item click (if needed)
        settingsListView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click here
            // Example: Open a new screen based on the clicked setting
        });

        //đăng xuất
        Logout = findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Register.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                // Xóa thông tin đăng nhập từ SharedPreferences
                clearLoginState();
            }
        });
    }
    // Xử lý sự kiện khi nút back được nhấn
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearLoginState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearLoginState();
    }

    private void clearLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(LOGIN_STATE_KEY);
        editor.apply();
    }
}