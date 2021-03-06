package it.politecnico_di_bari.sorbo_cantoro.tourenergydemand;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mappa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Miriam's house and move the camera
        LatLng miriamHouse = new LatLng(41.107247, 16.872925);
        LatLng totoHouse = new LatLng(41.111715, 16.888987);
        LatLng finconsOffices = new LatLng(41.106078, 16.852620);
        mMap.addMarker(new MarkerOptions().position(miriamHouse).title("Marker in Miriam's house"));
        mMap.addMarker(new MarkerOptions().position(totoHouse).title("Toto's house"));
        mMap.addMarker(new MarkerOptions().position(finconsOffices).title("Fincons Offices"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miriamHouse));
    }
}
