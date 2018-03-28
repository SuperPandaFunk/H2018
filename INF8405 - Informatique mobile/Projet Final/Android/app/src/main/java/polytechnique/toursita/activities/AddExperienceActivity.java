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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import polytechnique.toursita.R;
import polytechnique.toursita.manager.SharedPreferenceManager;
import polytechnique.toursita.webService.LocationResponse;
import polytechnique.toursita.webService.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExperienceActivity extends AppCompatActivity {

    private EditText strAddress, city, description;
    private Button confirm;
    private ImageView backArrow;
    private WebService webService;

    Callback<LocationResponse> addLocationCallback = new Callback<LocationResponse>() {
        @Override
        public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
            Toast.makeText(getApplicationContext(), "Upload reussi!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        @Override
        public void onFailure(Call<LocationResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Un probleme avec le serveur a ete rencontrer", Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener confirmExperienceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (strAddress.getText().toString().matches("") || city.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs!", Toast.LENGTH_LONG).show();
                return;
            }
            String address = strAddress.getText().toString() + ", " + city.getText().toString();
            LatLng result = getLocationFromAddress(getApplicationContext(), address);
            if (result != null){
                webService.createLocation(result.latitude, result.longitude, getCompleteAddressString(result), description.getText().toString(), SharedPreferenceManager.getUserId(getApplicationContext())).enqueue(addLocationCallback);
                Log.d("Experience", "lat: "+ result.latitude + "\tlon: " + result.longitude);
            }else{
                Toast.makeText(getApplicationContext(), "L'addresse entrer n'est pas valide!", Toast.LENGTH_LONG).show();
            }

        }
    };

    View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_experience_layout);
        initializeView();
        webService = new WebService();
    }

    private void initializeView(){
        strAddress = findViewById(R.id.address);
        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        confirm = findViewById(R.id.confirmExperience);
        backArrow = findViewById(R.id.backArrow);

        confirm.setOnClickListener(confirmExperienceListener);
        backArrow.setOnClickListener(backArrowListener);
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

}
