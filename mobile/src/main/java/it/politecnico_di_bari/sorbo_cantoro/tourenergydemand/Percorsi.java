package it.politecnico_di_bari.sorbo_cantoro.tourenergydemand;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Percorsi extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, RoutingListener {

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private GoogleMap mMap = null;
    private LocationManager locationManager = null;
    private TextView txtDistance, txtTime;

    //Global UI Map markers
    private Marker currentMarker = null;
    private Marker destMarker = null;
    private LatLng currentLatLng = null;
    private Polyline line = null;

    //Global flags
    private boolean firstRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorsi);
        Constants.POINT_DEST = new LatLng(41.111715, 16.888987);  //casa di salvo destination.
        Constants.POINT_START = new LatLng(41.106078, 16.852620);  //fincons start.
        //Load the map fragment on UI
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mappa);
        mapFragment.getMapAsync(this);
        txtDistance = (TextView) findViewById(R.id.txt_distance);
        txtTime = (TextView) findViewById(R.id.txt_time);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Percorsi.this.getRoutingPath();
                Snackbar.make(v, "Recupero del percorso", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        firstRefresh = true;
        //Ensure the GPS is ON and location permission enabled for the application.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Toast.makeText(Percorsi.this, "Primo recupero del percorso", Toast.LENGTH_SHORT).show();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(LOCATION_PERMS,1);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
            }

        } catch (Exception e) {
            Toast.makeText(Percorsi.this, "ERROR: Cannot start location listener", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        if (locationManager != null) {
            //Check needed in case of  API level 23.

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            try {
                locationManager.removeUpdates(this);
            } catch (Exception e) {
            }
        }
        locationManager = null;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    /**
     * @desc LocationListener Interface Methods implemented.
     */

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentLatLng = new LatLng(lat, lng);
        if (firstRefresh) {
            //Add Start Marker.
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Position"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
            firstRefresh = false;
            destMarker = mMap.addMarker(new MarkerOptions().position(Constants.POINT_DEST).title("Destination"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.POINT_DEST));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            getRoutingPath();
        } else {
            currentMarker.setPosition(currentLatLng);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     * @desc MapMarker Interface Methods Implemented.
     */

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().contains("Destination")) {
            //Do some task on dest pin click
        } else if (marker.getTitle().contains("Current")) {
            //Do some task on current pin click
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        canAccessLocation();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
    /**
     * @desc Routing Listener interface methods implemented.
     **/
    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(Percorsi.this, "Routing Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> list, int i) {
        try {
            //Get all points and plot the polyLine route.
            List<LatLng> listPoints = list.get(0).getPoints();
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            Iterator<LatLng> iterator = listPoints.iterator();
            while (iterator.hasNext()) {
                LatLng data = iterator.next();
                options.add(data);
            }

            //If line not null then remove old polyline routing.
            if (line != null) {
                line.remove();
            }
            line = mMap.addPolyline(options);

            //Show distance and duration.
            txtDistance.setText("Distance: " + list.get(0).getDistanceText());
            txtTime.setText("Duration: " + list.get(0).getDurationText());

            //Focus on map bounds
            mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(0).getLatLgnBounds().getCenter()));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLatLng);
            builder.include(Constants.POINT_DEST);
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        } catch (Exception e) {
            Toast.makeText(Percorsi.this, "EXCEPTION: Cannot parse routing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(Percorsi.this, "Routing Cancelled", Toast.LENGTH_SHORT).show();
    }

    /**
     * @method getRoutingPath
     * @desc Method to draw the google routed path.
     */
    private void getRoutingPath() {
        try {
            //Do Routing
            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(Constants.POINT_START, Constants.POINT_DEST)
                    .build();
            routing.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Percorsi.this, "Unable to Route", Toast.LENGTH_SHORT).show();
        }
    }

}