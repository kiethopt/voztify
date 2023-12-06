package nhom2.voztify;

import static nhom2.voztify.Login.LOGIN_STATE_KEY;
import static nhom2.voztify.Login.SHARE_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    Button bthSignUp, btnLoginTrans, btnResFace, btnResGoogle, btnResPhoneNum;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bthSignUp = findViewById(R.id.bthSignUp);
        btnLoginTrans = findViewById(R.id.btnLoginTrans);

        // Kiểm tra đã đăng nhập và thời gian hết hạn
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        String check = sharedPreferences.getString(LOGIN_STATE_KEY, "");

        if (check.equals("true") ) {
            // Nếu trạng thái đăng nhập còn hiệu lực, chuyển đến HomeActivity
            Toast.makeText(this, "Log in Success!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        bthSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,SignUp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btnLoginTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

}