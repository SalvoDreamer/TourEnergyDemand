package it.politecnico_di_bari.sorbo_cantoro.tourenergydemand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setUIView();
        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //Upload data
                }else{
                    //Gestire errore con messaggio di errore
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
            }
        });
    }

    private void setUIView(){
        userName = (EditText) findViewById(R.id.etUserReg);
        userPassword = (EditText) findViewById(R.id.etPasswordReg);
        userEmail = (EditText) findViewById(R.id.etEmailReg);
        regButton = (Button) findViewById(R.id.btnRegistration);
        userLogin = (TextView) findViewById(R.id.tvReturnToLogin);
    }

    private Boolean validate(){
        Boolean result = true;
        String name = userName.getText().toString();
        String email = userPassword.getText().toString();
        String password = userPassword.getTransitionName().toString();

        //isEmpty?
        if(name.isEmpty()) {
            Toast.makeText(this, "Insert Username", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(email.isEmpty()) {
            Toast.makeText(this, "Insert Email", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(password.isEmpty()) {
            Toast.makeText(this, "Insert Password", Toast.LENGTH_SHORT).show();
            result = false;
        }

        //Name validation
        if(name.length()<=3 || name.length()>=21) {
            Toast.makeText(this, "Name must be between 3 and 20 characters", Toast.LENGTH_SHORT).show();
            result = false;
        }

        //Email validation
        if(!email.contains("@") || !email.endsWith(".com") || !email.endsWith(".it") || !email.endsWith(".net") || !email.endsWith(".org")) {
            Toast.makeText(this, "Something wrong in your email", Toast.LENGTH_SHORT).show();
            result = false;
        }

        //Password validation
        if(password.length()<=4 || password.length()>=21){
            Toast.makeText(this, "Password must be between 4 and 20 characters", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

}
