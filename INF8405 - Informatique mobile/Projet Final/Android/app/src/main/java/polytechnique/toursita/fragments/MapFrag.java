package polytechnique.toursita.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import polytechnique.toursita.R;
import polytechnique.toursita.activities.AddExperienceActivity;

/**
 * Created by Vincent on 2018-03-25.
 */

public class MapFrag extends Fragment{

    private Button addExperience, refineSearch;
    private EditText distance;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationClient;

    View.OnClickListener addExperienceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), AddExperienceActivity.class);
            startActivity(intent);
        }
    };

    OnSuccessListener locationSuccess = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                centerToLocation(location.getLatitude(), location.getLongitude());
            }
        }
    };

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            googleMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(locationSuccess);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_layout, container, false);
        initializeView(view, savedInstanceState);
        return view;
    }

    private void initializeView(View view, Bundle savedInstanceState){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(onMapReadyCallback);

        addExperience = view.findViewById(R.id.ajouteExperience);
        refineSearch = view.findViewById(R.id.refineSearch);
        distance = view.findViewById(R.id.distance);

        addExperience.setOnClickListener(addExperienceListener);
    }

    private void centerToLocation(double lat, double lon) {
        LatLng latlong = new LatLng(lat, lon);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15));
    }

}
