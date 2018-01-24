package polytechnique.wifi_searcher.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import polytechnique.wifi_searcher.R;

/**
 * Created by Vincent on 2018-01-19.
 */

public class MapFrag extends Fragment{

    //SupportMapFragment mapFrag;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mGoogleMap;
    private Timer timer;
    private LatLng _LatLng;
    private Realm realm;


    WifiManager mainWifi;
    WifiReceiver receiverWifi;

    private final Handler handler = new Handler();

    MapView mMapView;
    OnSuccessListener locationSuccess =  new OnSuccessListener<Location>() {
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
            //TODO: Ajouter les marqueurs ici avec l'objet googleMap
            mGoogleMap = googleMap;
            if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            googleMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(locationSuccess);
        }
    };

    private void centerToLocation(double lat, double lon){
        LatLng latlong = new LatLng(lat, lon);
        _LatLng = latlong;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_layout, container, false);
        initializeView(view, savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        searchForWifi();
        return view;
    }


    private void searchForWifi()
    {

        Log.d("debug", "Start scanning");
        mainWifi = (WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        getContext().getApplicationContext().registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(mainWifi.isWifiEnabled()==false)
        {
            mainWifi.setWifiEnabled(true);
        }


        doInback();

        /*mGoogleMap.addMarker(new MarkerOptions().position(_LatLng))
        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));*/
    }

    public void doInback()
    {
        handler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if (receiverWifi==null) {
                    receiverWifi = new WifiReceiver();
                }
                getActivity().getApplicationContext().registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                doInback();
            }
        }, 10000);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initializeView(View view, Bundle savedInstanceState){
        mMapView = (MapView)view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(onMapReadyCallback);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        getContext().getApplicationContext().registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        getContext().getApplicationContext().unregisterReceiver(receiverWifi);
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {
            List<ScanResult> wifiList = mainWifi.getScanResults();
            for(int i = 0; i < wifiList.size(); ++i)
            {
                Log.d("debug",i + "\t" + wifiList.get(i).SSID);
            }
        }
    }
}
