package polytechnique.toursita.fragments;

import android.Manifest;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import polytechnique.toursita.R;
import polytechnique.toursita.activities.AddExperienceActivity;
import polytechnique.toursita.activities.ExperienceActivity;
import polytechnique.toursita.activities.MapActivity;
import polytechnique.toursita.webService.LocationRequestResponse;
import polytechnique.toursita.webService.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vincent on 2018-03-25.
 */

public class MapFrag extends Fragment{

    private Button addExperience, refineSearch;
    private EditText distance;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private WebService webService;
    private List<Marker> markers;

    Callback<LocationRequestResponse[]> locationsCallback = new Callback<LocationRequestResponse[]>() {
        @Override
        public void onResponse(Call<LocationRequestResponse[]> call, Response<LocationRequestResponse[]> response) {
            if (!response.isSuccessful() && response.body().length == 0){
                Toast.makeText(getActivity(), "Oops, il semblerait qu\'aucune activite ne soit disponible pres de vous...", Toast.LENGTH_LONG).show();
                return;
            }

            for (Marker m : markers){
                m.remove();
            }
            markers.clear();
            for (LocationRequestResponse location : response.body()){
                Marker m = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.lat, location.lon)));
                m.setTag(location._id);
                markers.add(m);
            }
            ((MapActivity)getActivity()).hideLoadingScreen();
        }

        @Override
        public void onFailure(Call<LocationRequestResponse[]> call, Throwable t) {
            Toast.makeText(getActivity(), "Une erreur c\'est produite lors de l\'aquisition de lieu pres de vous.", Toast.LENGTH_LONG).show();
            ((MapActivity)getActivity()).hideLoadingScreen();
        }
    };

    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Intent viewBeacon = new Intent(getActivity().getApplicationContext(), ExperienceActivity.class);
            viewBeacon.putExtra("id", (String)marker.getTag());
            startActivity(viewBeacon);
            return false;
        }
    };

    View.OnClickListener refreshExperiences = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(locationSuccess);
        }
    };

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
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                getLocations(position);
            }
        }
    };

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            mGoogleMap.setOnMarkerClickListener(onMarkerClickListener);
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
        markers = new ArrayList<>();
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
        refineSearch.setOnClickListener(refreshExperiences);
        webService = new WebService();
    }

    private void centerToLocation(double lat, double lon) {
        LatLng latlong = new LatLng(lat, lon);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15));
    }

    private void getLocations(LatLng position){
        ((MapActivity)getActivity()).showLoadingScreen();
        int range = Integer.parseInt(distance.getText().toString());
        webService.getNearLocations(range, position).enqueue(locationsCallback);
    }

}
