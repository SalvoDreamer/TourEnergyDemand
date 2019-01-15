package it.politecnico_di_bari.sorbo_cantoro.tourenergydemand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomepageTED extends AppCompatActivity {

    private Button mapButton;

    private Button routeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_ted);

        mapButton = (Button) findViewById(R.id.btnMaps);

        routeButton = (Button) findViewById(R.id.btnRoute);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomepageTED.this,Mappa.class));
            }
        });

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomepageTED.this,Percorsi.class));
            }
        });
    }
}
