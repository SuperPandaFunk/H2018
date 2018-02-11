package polytechnique.wifi_searcher.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import polytechnique.wifi_searcher.Adapter.BeaconListAdapter;
import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.activities.ViewBeaconActivity;
import polytechnique.wifi_searcher.models.Beacon;

/**
 * Created by Vincent on 2018-01-19.
 */

public class ListFrag extends Fragment {

    private ListView hotSpotList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BeaconListAdapter adapter;
    private ArrayList<Beacon> allBeacons;
    private Realm realm;

    private AdapterView.OnItemClickListener beaconListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Beacon tmp = (Beacon) parent.getAdapter().getItem(position);
            Intent viewBeacon = new Intent(getActivity().getApplicationContext(), ViewBeaconActivity.class);
            viewBeacon.putExtra("bssid", tmp.getBSSID());
            startActivity(viewBeacon);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            allBeacons.clear();
            allBeacons.addAll(getFakeBeaconData());
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    };


    private void initializeView(View view){
        allBeacons = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        hotSpotList = (ListView)view.findViewById(R.id.hotSpotList);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe2refresh);
        allBeacons = getFakeBeaconData();
        adapter = new BeaconListAdapter(getContext(), allBeacons);
        hotSpotList.setAdapter(adapter);
        hotSpotList.setOnItemClickListener(beaconListener);
        swipeRefreshLayout.setOnRefreshListener(swipeListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_hot_spot, container, false);
        initializeView(view);
        return view;
    }

    private ArrayList<Beacon> getFakeBeaconData(){
        List<Beacon> result = realm.where(Beacon.class).findAll();

        ArrayList<Beacon> items = new ArrayList<>();

        items.addAll(result);
        return items;
    }
}
