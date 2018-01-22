package polytechnique.wifi_searcher.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import polytechnique.wifi_searcher.R;

/**
 * Created by Vincent on 2018-01-19.
 */

public class ListFrag extends Fragment {

    private ListView hotSpotList;


    private void initializeView(View view){
        hotSpotList = (ListView)view.findViewById(R.id.hotSpotList);

        //TODO: Avoir un vrai adapteur avec des vrais donnees ici
        ArrayList<String> items = new ArrayList<String>();
        items.add("HotSpot McDo");
        items.add("HotSpot Harveys");
        items.add("Wi-fi chez nous");
        items.add("Un autre affaire random");
        ListAdapter itemAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        hotSpotList.setAdapter(itemAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_hot_spot, container, false);
        initializeView(view);
        return view;
    }
}
