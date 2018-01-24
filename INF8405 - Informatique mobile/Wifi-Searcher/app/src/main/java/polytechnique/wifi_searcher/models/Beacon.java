package polytechnique.wifi_searcher.models;

import android.location.Address;
import android.net.wifi.ScanResult;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Étienne on 2018-01-24.
 */

public class Beacon extends RealmObject {
    @PrimaryKey
    private String _BSSID;
    private String _SSID;
    private int _RSSI;
    private String _security;
    private LatLng _location;
    private Address _addresse;
    private boolean _isPublic;
    private int _typeEmplacement;
    private boolean _isFavorite;

    public Beacon(){}
    public Beacon(ScanResult scan, LatLng localisation, Address address){
        _BSSID = scan.BSSID;
        _SSID = scan.SSID;
        _RSSI = scan.level;
        _security = scan.capabilities;
        _isPublic = isWifiPublic(scan.capabilities);
        _isFavorite = false;
        _location = localisation;
        _typeEmplacement = -1;
        _addresse = address;

    }

    public  boolean isStronger(int level)
    {
        return level > _RSSI;
    }

    public void changeAddress(LatLng location, Address address)
    {
        _location = location;
        _addresse = address;

    }

    private boolean isWifiPublic(String capabilities)
    {
        return (capabilities.indexOf("WPA") >= 0 || capabilities.indexOf("EAP") >= 0);
    }

    public void setTypeEmplacement(int type){
        _typeEmplacement = type;
    }

    public String getBSSID(){
        return _BSSID;
    }

    public String getSSID(){
        return _SSID;
    }

    public String getSecurity(){
        return _security;
    }

    public int getRSSI(){
        return _RSSI;
    }

    public int getTypeEmplacement()
    {
        return _typeEmplacement;
    }

    public  LatLng getLocation(){
        return _location;
    }

    public Address getAddresse(){
        return _addresse;
    }

    public boolean isPublic(){
        return _isPublic;
    }

    public boolean isFavorite(){
        return _isFavorite;
    }
}
