package polytechnique.toursita.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import polytechnique.toursita.fragments.MapFrag;
import polytechnique.toursita.fragments.PowerFrag;

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter{

    private final int FRAG_COUNT = 2;

    public CustomViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            default:
            case 0:
                return new MapFrag();
            case 1:
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
