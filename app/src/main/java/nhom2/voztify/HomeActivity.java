package nhom2.voztify;

import static nhom2.voztify.Login.LOGIN_STATE_KEY;
import static nhom2.voztify.Login.SHARE_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Kiểm tra trạng thái đăng nhập
        if (!isLoggedIn()) {
            // Nếu chưa đăng nhập, chuyển hướng đến Regis
            startActivity(new Intent(HomeActivity.this, Register.class));
            finish();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_home) {
                // Prevent reloading the HomeFragment if it's already displayed
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeFragment)) {
                    loadFragment(new HomeFragment());
                }
                return true;
            } else if (itemId == R.id.action_explore) {
                loadFragment(new ExploreFragment());
                return true;
            } else if (itemId == R.id.action_chart) {
                loadFragment(new ChartFragment());
                return true;
            } else if (itemId == R.id.action_search) {
                loadFragment(new SearchFragment());
                return true;
            } else if (itemId == R.id.action_social) {
                loadFragment(new TabViewPlaylistFragment());
                return true;
            }

            return false;
        });

        // Load the HomeFragment initially when the activity is created
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }


    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //SettingActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigatiom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_notification) {
            // Xử lý khi nhấn vào mục Notification
            // ...
            return true;
        } else if (itemId == R.id.menu_setting) {
            // Xử lý khi nhấn vào mục SettingActivity
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //check đăng nhập
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        // Kiểm tra nếu thời gian hiện tại nhỏ hơn thời gian hết hạn
//        long expirationTime = sharedPreferences.getLong("EXPIRATION_TIME", 0);
//        boolean isExpired = System.currentTimeMillis() > expirationTime;
//        return !isExpired && sharedPreferences.getString(LOGIN_STATE_KEY, "").equals("true");
        return sharedPreferences.getString(LOGIN_STATE_KEY,"").equals("true");
    }
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(LOGIN_STATE_KEY, "true");
//
//    }
}
