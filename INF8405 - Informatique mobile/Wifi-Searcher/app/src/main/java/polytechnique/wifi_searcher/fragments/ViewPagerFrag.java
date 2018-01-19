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

    private ViewPager viewPager;

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
        viewPager.setAdapter(new CustumViewPagerAdapter(getActivity().getSupportFragmentManager()));
    }
}
