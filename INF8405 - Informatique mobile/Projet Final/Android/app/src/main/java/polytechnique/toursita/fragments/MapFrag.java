package polytechnique.toursita.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import polytechnique.toursita.R;

/**
 * Created by Vincent on 2018-03-25.
 */

public class MapFrag extends Fragment{

    private Button othersExperience, myExperience, nearExperience;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationClient;

    View.OnClickListener otherListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            myExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
            nearExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
            othersExperience.setBackgroundResource(R.drawable.round_corners_button_cyan);
        }
    };

    View.OnClickListener meListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            myExperience.setBackgroundResource(R.drawable.round_corners_button_cyan);
            nearExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
            othersExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
        }
    };

    View.OnClickListener nearListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            myExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
            nearExperience.setBackgroundResource(R.drawable.round_corners_button_cyan);
            othersExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
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

        othersExperience = view.findViewById(R.id.othersExperience);
        myExperience = view.findViewById(R.id.myExperience);
        nearExperience = view.findViewById(R.id.nearExperience);

        othersExperience.setOnClickListener(otherListener);
        myExperience.setOnClickListener(meListener);
        nearExperience.setOnClickListener(nearListener);

        othersExperience.setBackgroundResource(R.drawable.round_corners_button_cyan);
        myExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
        nearExperience.setBackgroundResource(R.drawable.round_corners_button_orang);
    }

    private void centerToLocation(double lat, double lon) {
        LatLng latlong = new LatLng(lat, lon);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15));
    }

}
