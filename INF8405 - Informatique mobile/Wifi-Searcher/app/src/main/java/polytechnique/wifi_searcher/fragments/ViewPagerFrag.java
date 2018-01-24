package polytechnique.wifi_searcher.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import polytechnique.wifi_searcher.Adapter.CustumViewPagerAdapter;
import polytechnique.wifi_searcher.R;

/**
 * Created by Vincent on 2018-01-19.
 */

public class ViewPagerFrag extends Fragment {

    private final int MAP_FRAG = 0;
    private final int LIST_FRAG = 1;
    private final int BATTERY_FRAG = 2;
    private ViewPager viewPager;

    private ViewPager.OnPageChangeListener pagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case MAP_FRAG:
                default:
                    ((NavigationBarFrag)getActivity().getSupportFragmentManager().findFragmentById(R.id.navigationBarContainer)).changeColor(MAP_FRAG);
                    break;
                case LIST_FRAG:
                    ((NavigationBarFrag)getActivity().getSupportFragmentManager().findFragmentById(R.id.navigationBarContainer)).changeColor(LIST_FRAG);
                    break;
                case BATTERY_FRAG:
                    ((NavigationBarFrag)getActivity().getSupportFragmentManager().findFragmentById(R.id.navigationBarContainer)).changeColor(BATTERY_FRAG);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
    }

    private void initializeView(View view){
        viewPager = (ViewPager)view.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(pagerListener);
        viewPager.setAdapter(new CustumViewPagerAdapter(getActivity().getSupportFragmentManager()));
    }

    public ViewPager getViewPager(){
        return viewPager;
    }
}
