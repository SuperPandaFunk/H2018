package polytechnique.wifi_searcher.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import polytechnique.wifi_searcher.fragments.ListFrag;
import polytechnique.wifi_searcher.fragments.MapFrag;
import polytechnique.wifi_searcher.fragments.PowerFrag;

/**
 * Created by Vincent on 2018-01-19.
 */

public class CustumViewPagerAdapter extends FragmentStatePagerAdapter {

    private final int FRAG_COUNT = 3;

    public CustumViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            default:
            case 0:
                return new MapFrag();
            case 1:
                return new ListFrag();
            case 2:
                return new PowerFrag();
        }
    }

    @Override
    public int getCount() {
        return FRAG_COUNT;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
