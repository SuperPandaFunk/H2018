package polytechnique.wifi_searcher.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    private ArrayAdapter adapter;
    private ListView list;
    private ArrayList<String> wifis;
    private WifiInfo wifiInfo;
    private WifiManager wifiManager;
    private  WifiScanReceiver wifiReceiver;
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
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 5000, 10000);
        return view;
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            searchForWifi();
        }
    };

    private void searchForWifi()
    {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiReceiver = new WifiScanReceiver();
        wifiInfo = wifiManager.getConnectionInfo();



        wifis = new ArrayList<String>(); //initialize wifis
        wifis.add("loading...");

        Log.d("debug", "Start scraning");
        wifiManager.startScan();

        /*mGoogleMap.addMarker(new MarkerOptions().position(_LatLng))
        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));*/
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
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

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            //wifis = new String[wifiScanList.size()]; //remove this
            wifis.clear(); //add this
            Log.d("debug", "before loop");
            for (int i = 0; i < wifiScanList.size(); i++) {
                String ssid = wifiScanList.get(i).SSID; //Get the SSID
                String bssid =  wifiScanList.get(i).BSSID; //Get the BSSID
                //use add here:
                wifis.add( ssid + " " + bssid + " " +((wifiScanList.get(i)).toString()) ); //append to the other data
                Log.d("debug", ssid + "\t" + bssid);
            }

            adapter.notifyDataSetChanged(); //add this
           // wifiManager.startScan(); //start a new scan to update values faster

            //ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
            //       android.R.layout.simple_list_item_1, wifis)
            //list.setAdapter(adapter);
        }
    }
}
