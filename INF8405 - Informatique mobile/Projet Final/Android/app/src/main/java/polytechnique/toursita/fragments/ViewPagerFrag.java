package polytechnique.toursita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import polytechnique.toursita.R;
import polytechnique.toursita.adapter.CustomViewPagerAdapter;

public class ViewPagerFrag extends Fragment {
    private final int MAP_FRAG = 0;
    private final int POWER_FRAG = 1;
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
                    ((NavigationFrag)getActivity().getSupportFragmentManager().findFragmentById(R.id.navigationBarContainer)).changeColor(MAP_FRAG);
                    break;
                case POWER_FRAG:
                    ((NavigationFrag)getActivity().getSupportFragmentManager().findFragmentById(R.id.navigationBarContainer)).changeColor(POWER_FRAG);
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
        viewPager = view.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(pagerListener);
        viewPager.setAdapter(new CustomViewPagerAdapter(getActivity().getSupportFragmentManager()));
    }

    public ViewPager getViewPager(){
        return viewPager;
    }

    public void updateNavBar(){
        ((NavigationFrag)getActivity().getSupportFragmentManager().findFragmentById(R.id.navigationBarContainer)).changeColor(viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == POWER_FRAG){

        }
    }
}
