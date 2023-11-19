package nhom2.voztify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                loadFragment(new SocialFragment());
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

    //Setting


}
