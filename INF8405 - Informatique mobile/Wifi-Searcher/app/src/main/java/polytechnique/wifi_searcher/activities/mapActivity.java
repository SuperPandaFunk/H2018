package polytechnique.wifi_searcher.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.fragments.NavigationBarFrag;
import polytechnique.wifi_searcher.fragments.ViewPagerFrag;

/**
 * Created by Vincent on 2018-01-19.
 */

public class mapActivity extends AppCompatActivity{

    private void initializeView(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_container);
        initializeView();

        if (savedInstanceState == null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ViewPagerFrag viewPagerFrag = new ViewPagerFrag();
            NavigationBarFrag navigationBarFrag = new NavigationBarFrag();
            ft.add(R.id.navigationBarContainer, navigationBarFrag);
            ft.add(R.id.main_container, viewPagerFrag);
            ft.commit();
        }
    }

    public ViewPager getViewPager(){
        if (getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof ViewPagerFrag)
            return ((ViewPagerFrag)getSupportFragmentManager().findFragmentById(R.id.main_container)).getViewPager();
        return null;
    }


}
