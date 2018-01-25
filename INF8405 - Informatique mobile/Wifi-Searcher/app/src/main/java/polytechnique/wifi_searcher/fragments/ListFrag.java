package polytechnique.wifi_searcher.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import polytechnique.wifi_searcher.Adapter.BeaconListAdapter;
import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.activities.ViewBeaconActivity;
import polytechnique.wifi_searcher.models.Beacon;

/**
 * Created by Vincent on 2018-01-19.
 */

public class ListFrag extends Fragment {

    private ListView hotSpotList;

    private AdapterView.OnItemClickListener beaconListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Beacon tmp = (Beacon) parent.getAdapter().getItem(position);
            Intent viewBeacon = new Intent(getActivity().getApplicationContext(), ViewBeaconActivity.class);
            viewBeacon.putExtra("ssid", tmp.getSSID());
            viewBeacon.putExtra("bssid", "mettre le bssid du beacon");
            viewBeacon.putExtra("rssi", "mettre le rssi du beacon");
            viewBeacon.putExtra("encryptionKey", "WPA");
            viewBeacon.putExtra("wifiType", tmp.getTestWifiType());
            startActivity(viewBeacon);
        }
    };

    private void initializeView(View view){
        hotSpotList = (ListView)view.findViewById(R.id.hotSpotList);
        BeaconListAdapter adapter = new BeaconListAdapter(getContext(), getFakeBeaconData());
        hotSpotList.setAdapter(adapter);
        hotSpotList.setOnItemClickListener(beaconListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_hot_spot, container, false);
        initializeView(view);
        return view;
    }

    private ArrayList<Beacon> getFakeBeaconData(){
        Beacon beacon1 = new Beacon("Eduroam", "private", "2900 Edouard Montpetit Blvd, Montreal", "QC H3T 1J4", "Canada", "school");
        Beacon beacon2 = new Beacon("Mc Donald's", "public", "1500 Chemin de Saint-Jean, La Prairie", "QC J5R 2L7", "Canada", "resto");
        Beacon beacon3 = new Beacon("Best Western Plus", "private", "1240 Drummond St, Montreal", "QC H3G 1V7", "Canada", "hotel");
        Beacon beacon4 = new Beacon("Mount Royal Park", "public", "1260 Remembrance Rd, Montreal", "QC H3H 1A2", "Canada", "open");
        Beacon beacon5 = new Beacon("Cineplex Odeon", "public", "9350 Boulevard Leduc, Brossard", "QC J4Y 0B3", "Canada", "entertainment");
        ArrayList<Beacon> items = new ArrayList<>();
        items.add(beacon1);
        items.add(beacon2);
        items.add(beacon3);
        items.add(beacon4);
        items.add(beacon5);
        return items;
    }
}
