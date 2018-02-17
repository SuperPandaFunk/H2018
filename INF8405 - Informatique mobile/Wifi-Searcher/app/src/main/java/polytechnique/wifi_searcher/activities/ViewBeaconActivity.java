package polytechnique.wifi_searcher.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import io.realm.Realm;
import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.manager.EnergyManager;
import polytechnique.wifi_searcher.models.Beacon;

/**
 * Created by Vincent on 2018-01-25.
 */

public class ViewBeaconActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView ssid, bssid, rssi, encryptionKey, street;
    private ImageButton favorite, direction, share;
    private Realm realm;
    protected Beacon beacon;
    private boolean ReallyLeaving;

    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Regarde, j'ai trouvé un super réseau: \nNom: " + beacon.getSSID() + "\nAdresse: " + beacon.getStreetAddress() + "\nSécurité: " + beacon.getSecurity());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Choisir l'application sur laquelle partager"));
        }
    };

    private View.OnClickListener directionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri googleMap = Uri.parse("google.navigation:q=" + beacon.getPositionString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMap);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    };

    private View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            realm.beginTransaction();
            beacon.toggleFavorite();
            realm.copyToRealmOrUpdate(beacon);
            realm.commitTransaction();
            favorite.setColorFilter(beacon.isFavorite() ? ContextCompat.getColor(getApplicationContext(), R.color.yellow) : ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    };

    private void initializeView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        ssid = (TextView)findViewById(R.id.ssid);
        bssid = (TextView)findViewById(R.id.bssid);
        rssi = (TextView)findViewById(R.id.rssid);
        encryptionKey = (TextView)findViewById(R.id.encryptionKey);
        street = (TextView)findViewById(R.id.addressValue);
        //TODO: Ajouter des OnItemClickListener pour les bouttons et faire de quoi avec
        favorite = (ImageButton) findViewById(R.id.favorite);
        direction = (ImageButton)findViewById(R.id.direction);
        share = (ImageButton)findViewById(R.id.share);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            final Drawable arrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
            arrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(arrow);
        }

        favorite.setOnClickListener(favoriteListener);
        direction.setOnClickListener(directionListener);
        share.setOnClickListener(shareListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.view_beacon);
        initializeView();
        populateView();
        ReallyLeaving = true;
    }
    private void populateView(){
        getBeacon();
        ssid.setText(beacon.getSSID());
        bssid.setText(beacon.getBSSID());
        rssi.setText(Integer.toString(beacon.getRSSI()) + " dp");
        encryptionKey.setText(beacon.getSecurity());
        street.setText(beacon.getStreetAddress());
        favorite.setColorFilter(beacon.isFavorite() ? ContextCompat.getColor(getApplicationContext(), R.color.yellow) : ContextCompat.getColor(getApplicationContext(), R.color.white));
    }

    private void getBeacon(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                beacon = realm.where(Beacon.class).equalTo("_BSSID", getIntent().getStringExtra("bssid")).findFirst();
            }
        });
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ReallyLeaving = false;
    }
}
