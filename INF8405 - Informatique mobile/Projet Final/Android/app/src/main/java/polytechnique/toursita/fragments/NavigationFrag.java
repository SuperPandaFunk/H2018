package polytechnique.toursita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import polytechnique.toursita.R;
import polytechnique.toursita.activities.MapActivity;

public class NavigationFrag extends Fragment{
    private ImageView mapMarker, battery;
    private static final int MAP_FRAG = 0;
    private static final int POWER_FRAG = 1;

    private ViewPager viewPager;


    private View.OnClickListener mapListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewPager = ((MapActivity)getActivity()).getViewPager();

            if (viewPager != null){
                viewPager.setCurrentItem(MAP_FRAG, true);
            }
        }
    };

    private View.OnClickListener batteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewPager = ((MapActivity)getActivity()).getViewPager();

            if (viewPager != null){
                viewPager.setCurrentItem(POWER_FRAG, true);
            }
        }
    };

    private void initializeView(View view){
        mapMarker = view.findViewById(R.id.map_marker);
        battery = view.findViewById(R.id.battery);

        mapMarker.setOnClickListener(mapListener);
        battery.setOnClickListener(batteryListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_bar, container, false);
        viewPager = ((MapActivity)getActivity()).getViewPager();
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
                mapMarker.setColorFilter(ContextCompat.getColor(getContext(), R.color.orang));
                battery.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                break;
            case POWER_FRAG:
                mapMarker.setColorFilter(ContextCompat.getColor(getContext(), R.color.silver));
                battery.setColorFilter(ContextCompat.getColor(getContext(), R.color.orang));
                break;
        }
    }
}
