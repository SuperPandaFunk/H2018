package polytechnique.wifi_searcher.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.activities.mapActivity;

/**
 * Created by vince on 2018-01-24.
 */

public class NavigationBarFrag extends Fragment{

    private ImageView mapMarker, beaconList, battery;
    private static final int MAP_FRAG = 0;
    private static final int LIST_FRAG = 1;
    private static final int BATTERY_FRAG = 2;

    private ViewPager viewPager;


    private View.OnClickListener mapListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewPager = ((mapActivity)getActivity()).getViewPager();

            changeColor(MAP_FRAG);
            if (viewPager != null){
                viewPager.setCurrentItem(MAP_FRAG, true);
            }
        }
    };

    private View.OnClickListener beaconListListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewPager = ((mapActivity)getActivity()).getViewPager();

            changeColor(LIST_FRAG);
            if (viewPager != null){
                viewPager.setCurrentItem(LIST_FRAG, true);
            }
        }
    };

    private View.OnClickListener batteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewPager = ((mapActivity)getActivity()).getViewPager();

            changeColor(BATTERY_FRAG);
            if (viewPager != null){
                viewPager.setCurrentItem(BATTERY_FRAG, true);
            }
        }
    };

    private void initializeView(View view){
        mapMarker = (ImageView)view.findViewById(R.id.map_marker);
        beaconList = (ImageView)view.findViewById(R.id.beacon_list);
        battery = (ImageView)view.findViewById(R.id.battery);

        mapMarker.setOnClickListener(mapListener);
        beaconList.setOnClickListener(beaconListListener);
        battery.setOnClickListener(batteryListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_bar, container, false);
        viewPager = ((mapActivity)getActivity()).getViewPager();
        initializeView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void changeColor(int position){
        switch (position) {
            default:
            case MAP_FRAG:
                mapMarker.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                beaconList.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                battery.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                break;
            case LIST_FRAG:
                mapMarker.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                beaconList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                battery.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                break;
            case BATTERY_FRAG:
                mapMarker.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                beaconList.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                battery.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
        }
    }

}
