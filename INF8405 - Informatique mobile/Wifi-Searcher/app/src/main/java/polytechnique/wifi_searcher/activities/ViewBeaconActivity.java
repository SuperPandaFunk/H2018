package polytechnique.wifi_searcher.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import polytechnique.wifi_searcher.R;

/**
 * Created by Vincent on 2018-01-25.
 */

public class ViewBeaconActivity extends AppCompatActivity {

    private Toolbar toolbar;
    //Objet utile une fois realm et l'objet comme tel configurer
    //private Beacon thisBeacon;
    private String givenSSID;
    private TextView ssid, bssid, rssi, encryptionKey, street;
    private ImageButton favorite, direction;
    private ImageView wifiType;


    private void initializeView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        ssid = (TextView)findViewById(R.id.ssid);
        bssid = (TextView)findViewById(R.id.bssid);
        rssi = (TextView)findViewById(R.id.rssi);
        encryptionKey = (TextView)findViewById(R.id.encryptionKey);
        street = (TextView)findViewById(R.id.addressValue);
        //TODO: Ajouter des OnItemClickListener pour les bouttons et faire de quoi avec
        favorite = (ImageButton) findViewById(R.id.favorite);
        direction = (ImageButton)findViewById(R.id.direction);
        wifiType = (ImageView)findViewById(R.id.wifiType);

        //givenSSID = getIntent().getStringExtra("ssid");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_beacon);
        initializeView();
        populateView();
    }

    //TODO Prendre l'information du beacon avec: getIntent().getStringExtra("beacon") quelque part et mettre les informations du beacon dans la view avec la fonction populateView
    private void populateView(){
        //La fonction prend va chercher plein de chose avec getIntent().getStringExtra(),
        //mais il faut reecrire cette fonction une fois que l'objet Beacon est complet.
        ssid.setText(getIntent().getStringExtra("ssid"));
        bssid.setText(getIntent().getStringExtra("bssid"));
        rssi.setText(getIntent().getStringExtra("rssi"));
        encryptionKey.setText(getIntent().getStringExtra("encryptionKey"));
        street.setText(getIntent().getStringExtra("addressValue"));
        wifiType.setImageResource(getIntent().getIntExtra("wifiType", R.drawable.question_mark));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
