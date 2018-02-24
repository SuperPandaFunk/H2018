package polytechnique.wifi_searcher.models;

import android.location.Address;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.support.v4.app.NotificationCompatSideChannelService;

import com.google.android.gms.maps.model.LatLng;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import polytechnique.wifi_searcher.R;

/**
 * Created by Étienne on 2018-01-24.
 */

public class Beacon extends RealmObject {
    @PrimaryKey
    private String _BSSID;
    private String _SSID;
    private int _RSSI;
    private String _security;
    private String _streetAddress, _zipCode, _country;
    private double _longitude;
    private double _latitude;
    private boolean _Favorite;
    private boolean _isPublic;



    public Beacon(){}
    public Beacon(ScanResult scan, Address address){
        _BSSID = scan.BSSID;
        _SSID = scan.SSID;
        _RSSI = scan.level;
        _security = scan.capabilities;
        _isPublic = isWifiPublic(scan.capabilities);
        _Favorite = false;
        changeAddress(address);
    }

    public void setBeacon(ScanResult scan, Address address)
    {
        _SSID = scan.SSID;
        _RSSI = scan.level;
        _security = scan.capabilities;
        _isPublic = isWifiPublic(scan.capabilities);
        _Favorite = false;
        changeAddress(address);
    }

    public  boolean isStronger(int level)
    {
        return level > _RSSI;
    }

    public void changeAddress(Address address)
    {
        _latitude = address.getLatitude();
        _longitude = address.getLongitude();
        _streetAddress = address.getAddressLine(0);
        _zipCode = address.getPostalCode();
        _country = address.getCountryName();
    }

    private boolean isWifiPublic(String capabilities)
    {
        return !(capabilities.indexOf("WPA") >= 0 || capabilities.indexOf("EAP") >= 0);
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

    public boolean isPublic(){
        return _isPublic;
    }

    public boolean isFavorite(){
        return _Favorite;
    }

    public double getLongitude(){
        return _longitude;
    }

    public double getLatitude(){
        return _latitude;
    }

    public Location getLocation()
    {
        Location l = new Location("Pos");
        l.setLongitude(_longitude);
        l.setLatitude(_latitude);
        return l;
    }

    public String getPositionString(){
        return Double.toString(_latitude) + "," + Double.toString(_longitude);
    }

    public void toggleFavorite(){
        _Favorite = !_Favorite;
    }
    /************************* Ajouter pour tester ********************************************/


    public Beacon(String ssid, String security, String streetAddress, String zipCode, String country){
        _SSID = ssid;
        _streetAddress = streetAddress;
        _security = security;
        _zipCode = zipCode;
        _country = country;
    }

    public int getSecurityIco(){
        if (_isPublic)
            return R.drawable.open_lock;
        else
            return R.drawable.lock;
    }

    public int getSecurityColor(){
        if (_isPublic)
            return R.color.green;
        else
            return R.color.red;
    }

    public String getStreetAddress(){
        return _streetAddress;
    }

    public String getZipCode(){
        return _zipCode;
    }

    public String getCountry(){
        return _country;
    }
}
