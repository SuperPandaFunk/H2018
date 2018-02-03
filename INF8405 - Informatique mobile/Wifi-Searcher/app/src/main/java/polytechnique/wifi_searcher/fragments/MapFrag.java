package polytechnique.wifi_searcher.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.models.Beacon;

/**
 * Created by Vincent on 2018-01-19.
 */

public class MapFrag extends Fragment {

    //SupportMapFragment mapFrag;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mGoogleMap;
    private Timer timer;
    private LatLng _LatLng;
    private Address address;
    private Realm realm;
    private Geocoder geocoder;
    private Map<String, Marker> markers;

    private  List<Beacon> beacons;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;

    private final Handler handler = new Handler();

    MapView mMapView;
    OnSuccessListener locationSuccess = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                centerToLocation(location.getLatitude(), location.getLongitude());
            }
        }
    };

    OnCompleteListener locationComplete = new OnCompleteListener<Location>() {
        @Override
        public void onComplete(Task<Location> task) {
            _LatLng = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
            try {
                address = geocoder.getFromLocation(_LatLng.latitude,_LatLng.longitude,1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //TODO: Ajouter les marqueurs ici avec l'objet googleMap
            mGoogleMap = googleMap;
            addAllMArkers();
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            googleMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(locationSuccess);

        }
    };

    private void centerToLocation(double lat, double lon) {
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
        markers = new HashMap<String, Marker>();
        searchForWifi();
        return view;
    }


    private void addAllMArkers()
    {

        List<Beacon> lBeacon = realm.where(Beacon.class).findAll();
        for(int i = 0; i < lBeacon.size();++i)
        {
            if(lBeacon.get(i).getBSSID() != null)
            {
                Marker m = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lBeacon.get(i).getLatitude(), lBeacon.get(i).getLongitude())));
                markers.put(lBeacon.get(i).getBSSID(), m);
            }
        }
    }

    private void searchForWifi() {

        Log.d("debug", "Start scanning");
        mainWifi = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();
        getContext().getApplicationContext().registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (mainWifi.isWifiEnabled() == false) {
            mainWifi.setWifiEnabled(true);
        }
        doInback();
    }

    public void doInback() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (getActivity().getApplicationContext() != null) {
                    mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (receiverWifi == null) {
                    receiverWifi = new WifiReceiver();
                }
                getActivity().getApplicationContext().registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(locationComplete);
                mainWifi.startScan();
                }

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

    private boolean isnear(Location l)
    {
        Location locationB = new Location("point B");
        locationB.setLongitude(_LatLng.longitude);
        locationB.setLongitude(_LatLng.latitude);

        return l.distanceTo(locationB) < 50;
    }
    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {
            List<ScanResult> wifiList = mainWifi.getScanResults();

            for(int i = 0; i < wifiList.size(); ++i)
            {
                realm.beginTransaction();
                Beacon result = realm.where(Beacon.class).equalTo("_BSSID",wifiList.get(i).BSSID).findFirst();
                if(result == null)
                {
                    if(wifiList.get(i).SSID != null)
                    {
                        Beacon b = realm.createObject(Beacon.class, wifiList.get(i).BSSID);
                        b.setBeacon(wifiList.get(i), address);
                        Marker m = mGoogleMap.addMarker(new MarkerOptions().position(_LatLng));
                        markers.put(b.getBSSID(), m);
                    }
                }
                else
                {
                    if(result.isStronger(wifiList.get(i).level))
                    {
                        result.changeAddress(address);

                        Marker edit= markers.get(result.getBSSID());

                        if(edit != null)
                        {
                            edit.setPosition(_LatLng);
                        }
                        else
                        {
                            Marker m = mGoogleMap.addMarker(new MarkerOptions().position(_LatLng));
                            markers.put(result.getBSSID(), m);
                        }
                    }
                }
                realm.commitTransaction();
            }
        }
    }
}
