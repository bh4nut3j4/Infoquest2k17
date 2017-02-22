package in.errorlabs.infoquest2k17.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import in.errorlabs.infoquest2k17.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng jbiet_entrance = new LatLng(17.331382, 78.297474);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions().position(jbiet_entrance).title("JBIET Entrance"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330222, 78.297701)).title("JBIET Main Block"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330406, 78.297228)).title("JBIET First Yr Block"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.331039, 78.297505)).title("BasketBall Court"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.331188, 78.297355)).title("SAE-JBIET"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330632, 78.297685)).title("Coke Hub"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.329808, 78.300068)).title("Cricket Ground"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.331444, 78.297610)).title("Syndicate Bank"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330048, 78.297193)).title("Cafeteria"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.331339, 78.297978)).title("Central Canteen"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330186, 78.297791)).title("MNR Auditorium Second Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330877, 78.297867)).title("Parking Lot 1"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330845, 78.297234)).title("Parking Lot 2"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330455, 78.298562)).title("Civil & EEE Block"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330284, 78.298522)).title("Dept. of EEE Ground Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330134, 78.298629)).title("Dept. of CIVIL First Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330788, 78.297339)).title("Dept. of MECHANICAL First Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330194, 78.298081)).title("Dept. of CSE First Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330089, 78.298318)).title("Dept. of ECE Second Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.329968, 78.297962)).title("Dept. of IT First Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.329865, 78.298197)).title("Dept. of ECM Second Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330789, 78.297375)).title("Transport Office"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330739, 78.297404)).title("Stationery"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330040, 78.297923)).title("SAC-JBIET Ground Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.329821, 78.298285)).title("Sports Room Ground Floor"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330316, 78.297852)).title("Placement Cell"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330096, 78.297708)).title("Administration Block"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.330657, 78.297293)).title("MBA Block"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(17.331382, 78.297474), 20);
        mMap.animateCamera(cameraUpdate);
    }




}
