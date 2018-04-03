package polytechnique.toursita.activities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import polytechnique.toursita.R;
import polytechnique.toursita.manager.EnergyManager;
import polytechnique.toursita.manager.SharedPreferenceManager;
import polytechnique.toursita.webService.LocationCreationResponse;
import polytechnique.toursita.webService.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExperienceActivity extends AppCompatActivity {

    private EditText strAddress, city, description;
    private Button confirm, viewMapButton;
    private ImageView backArrow;
    private WebService webService;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LinearLayout mapLayout;
    private Marker marker;
    private RelativeLayout loadingView;
    private boolean ReallyLeaving;

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
        }
    };

    Callback<LocationCreationResponse> addLocationCallback = new Callback<LocationCreationResponse>() {
        @Override
        public void onResponse(Call<LocationCreationResponse> call, Response<LocationCreationResponse> response) {
            Toast.makeText(getApplicationContext(), "Upload reussi!", Toast.LENGTH_LONG).show();
            closeKeyboard();
            hideLoadingScreen();
            onBackPressed();
        }

        @Override
        public void onFailure(Call<LocationCreationResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Un probleme avec le serveur a ete rencontrer", Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener confirmExperienceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showLoadingScreen();
            if (strAddress.getText().toString().matches("") || city.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs!", Toast.LENGTH_LONG).show();
                hideLoadingScreen();
                return;
            }
            String address = strAddress.getText().toString() + ", " + city.getText().toString();
            LatLng result = getLocationFromAddress(getApplicationContext(), address);
            if (result != null){
                webService.createLocation(result.latitude, result.longitude, getCompleteAddressString(result), description.getText().toString(), SharedPreferenceManager.getUserId(getApplicationContext())).enqueue(addLocationCallback);
            }else{
                Toast.makeText(getApplicationContext(), "L'addresse entrer n'est pas valide!", Toast.LENGTH_LONG).show();
            }
            hideLoadingScreen();
        }
    };

    View.OnClickListener lookOnMapListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showLoadingScreen();
            if (strAddress.getText().toString().matches("") || city.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs!", Toast.LENGTH_LONG).show();
                hideLoadingScreen();
                return;
            }
            String address = strAddress.getText().toString() + ", " + city.getText().toString();
            LatLng result = getLocationFromAddress(getApplicationContext(), address);
            if (result != null){
                closeKeyboard();
                centerToLocation(result);
                mapLayout.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(getApplicationContext(), "L'addresse entrer n'est pas valide!", Toast.LENGTH_LONG).show();
            }
            hideLoadingScreen();
        }
    };

    View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeKeyboard();
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_experience_layout);
        ReallyLeaving = true;
        initializeView(savedInstanceState);
        webService = new WebService();
    }

    private void initializeView(Bundle savedInstanceState){
        strAddress = findViewById(R.id.address);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        confirm = findViewById(R.id.confirmExperience);
        backArrow = findViewById(R.id.backArrow);
        mMapView = findViewById(R.id.mapView);
        mapLayout = findViewById(R.id.mapContainer);
        viewMapButton = findViewById(R.id.viewOnMap);
        loadingView = findViewById(R.id.loadingScreen);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(onMapReadyCallback);

        confirm.setOnClickListener(confirmExperienceListener);
        backArrow.setOnClickListener(backArrowListener);
        viewMapButton.setOnClickListener(lookOnMapListener);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {

            ex.printStackTrace();
        }
        return p1;
    }

    private String getCompleteAddressString(LatLng position) {
        String strAdd = "";
        double LATITUDE = position.latitude;
        double LONGITUDE = position.longitude;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Experience", strReturnedAddress.toString());
            } else {
                Log.w("Experience", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Experience", "Canont get Address!");
        }
        return strAdd;
    }

    protected void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void centerToLocation(LatLng position) {
        LatLng latlong = new LatLng(position.latitude, position.longitude);
        addMarker(position);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15));
    }

    private void addMarker(LatLng position){
        if (marker != null)
            marker.remove();
        marker = mGoogleMap.addMarker(new MarkerOptions()
                .position(position));
    }

    private void showLoadingScreen(){
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingScreen(){
        loadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EnergyManager.getInstance().StartCounting(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EnergyManager.getInstance().StopCounting(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ReallyLeaving)
            EnergyManager.getInstance().StopCounting(this);
        hideLoadingScreen();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ReallyLeaving = false;
    }
}
