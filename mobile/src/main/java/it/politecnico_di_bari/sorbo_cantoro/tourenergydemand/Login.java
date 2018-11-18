package it.politecnico_di_bari.sorbo_cantoro.tourenergydemand;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button login_btn;
    private TextView info;
    private TextView messaggio;
    private TextView registration;
    private FirebaseAuth firebaseAuth;
    private int counter=7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUIView();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(),password.getText().toString());
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

    }

    private void setUIView() {
        name = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        messaggio = (TextView) findViewById(R.id.tvMessage);
        info = (TextView) findViewById(R.id.tvCounter);
        login_btn = (Button) findViewById(R.id.btnLogin);
        registration = (TextView) findViewById(R.id.tvGoToRegistration);
    }

    private void validate(String userName, String userPassword){

        firebaseAuth = FirebaseAuth.getInstance();

        Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(userName, userPassword);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AuthResult result = task.getResult();
                    Toast.makeText(Login.this,"Autentication successful, welcome to "+result.getUser().getDisplayName(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,HomepageTED.class));
                }else{
                    counter--;
                    messaggio.setText("Username o Password errate, riprova");
                    info.setText("N° of uncorrected attempts: " + counter);
                    if(counter==0){
                        login_btn.setEnabled(false);
                    }
                    Toast.makeText(Login.this,"Autentication failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
