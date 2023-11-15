package nhom2.voztify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {

    Button bthSignUp, btnLoginTrans, btnResFace, btnResGoogle, btnResPhoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bthSignUp = findViewById(R.id.bthSignUp);
        btnLoginTrans = findViewById(R.id.btnLoginTrans);
        btnResFace = findViewById(R.id.btnResFace);
        btnResPhoneNum = findViewById(R.id.btnResPhoneNum);
        btnResGoogle = findViewById(R.id.btnResGoogle);

        btnLoginTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        bthSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,SignUp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

}