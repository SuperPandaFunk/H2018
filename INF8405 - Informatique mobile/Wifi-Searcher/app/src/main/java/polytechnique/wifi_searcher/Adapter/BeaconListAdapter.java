package polytechnique.wifi_searcher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.models.Beacon;

/**
 * Created by Vincent on 2018-01-25.
 */

public class BeaconListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Beacon> beaconList;

    public BeaconListAdapter(Context context, ArrayList<Beacon> _beaconList){
        mContext = context;
        beaconList = new ArrayList<>();
        beaconList.addAll(_beaconList);
        beaconList = _beaconList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Beacon thisBeacon = beaconList.get(position);

        if (convertView != null){
            holder = (ViewHolder)convertView.getTag();
        }else {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.beacon_row, parent, false);
            holder = new ViewHolder();
            holder.ssid = (TextView)convertView.findViewById(R.id.ssidName);
            holder.streetAddress = (TextView)convertView.findViewById(R.id.streetAddress);
            holder.zipCode = (TextView)convertView.findViewById(R.id.zipCode);
            holder.country = (TextView)convertView.findViewById(R.id.country);
            holder.type = (ImageView)convertView.findViewById(R.id.wifiType);
            holder.protection = (ImageView)convertView.findViewById(R.id.protection);
        }
        if (thisBeacon != null){
            holder.ssid.setText(thisBeacon.getSSID());

            //TODO Mettre a jour les informations du beacon
            holder.streetAddress.setText(thisBeacon.getTestStreetAddress());
            holder.zipCode.setText(thisBeacon.getTestZipCode());
            holder.country.setText(thisBeacon.getTestCountry());
            holder.type.setImageResource(thisBeacon.getTestWifiType());
            holder.protection.setImageResource(thisBeacon.getTestSecurity());

        }
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return beaconList.get(position);
    }

    @Override
    public int getCount() {
        return beaconList.size();
    }

    private class ViewHolder{
        TextView ssid, streetAddress, zipCode, country;
        ImageView type, protection;
    }
}
