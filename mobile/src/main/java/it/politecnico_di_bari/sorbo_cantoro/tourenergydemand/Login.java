package it.politecnico_di_bari.sorbo_cantoro.tourenergydemand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button login_btn;
    private TextView info;
    private TextView messaggio;
    private int counter=7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        messaggio = (TextView) findViewById(R.id.tvMessage);
        info = (TextView) findViewById(R.id.tvCounter);
        login_btn = (Button) findViewById(R.id.btnLogin);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(),password.getText().toString());
            }
        });

    }

    private void validate(String userName, String userPassword){

        if(userName.equals("admin") && userPassword.equals("1234")){
            Intent intent = new Intent(Login.this, HomepageTED.class);
            startActivity(intent);
        }else{
            counter--;
            messaggio.setText("Username o Password errate, riprova");
            info.setText("N° of uncorrected attempts: " + counter);


            if(counter==0){
                login_btn.setEnabled(false);
            }
        }

    }
}
