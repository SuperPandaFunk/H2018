package polytechnique.toursita.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import polytechnique.toursita.R;
import polytechnique.toursita.fragments.MapFrag;
import polytechnique.toursita.fragments.NavigationFrag;
import polytechnique.toursita.fragments.ViewPagerFrag;
import polytechnique.toursita.manager.EnergyManager;

/**
 * Created by Vincent on 2018-03-25.
 */

public class MapActivity extends AppCompatActivity{

    private ImageButton optionButton;
    private RelativeLayout loadingLayout;

    View.OnClickListener optionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_container);
        initializeView();

        if (savedInstanceState == null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ViewPagerFrag viewPagerFrag = new ViewPagerFrag();
            NavigationFrag navigationBarFrag = new NavigationFrag();
            ft.add(R.id.navigationBarContainer, navigationBarFrag);
            ft.add(R.id.main_container, viewPagerFrag);
            ft.commit();
        }
    }

    private void initializeView(){
        optionButton = findViewById(R.id.option);
        loadingLayout = findViewById(R.id.loadingScreen);

        optionButton.setOnClickListener(optionListener);
    }

    public ViewPager getViewPager(){
        if (getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof ViewPagerFrag)
            return ((ViewPagerFrag)getSupportFragmentManager().findFragmentById(R.id.main_container)).getViewPager();
        return null;
    }

    public void showLoadingScreen(){
        loadingLayout.setVisibility(View.VISIBLE);
    }

    public void hideLoadingScreen(){
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof ViewPagerFrag)
            ((ViewPagerFrag)getSupportFragmentManager().findFragmentById(R.id.main_container)).updateNavBar();
        EnergyManager.getInstance().StartCounting(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EnergyManager.getInstance().StopCounting(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EnergyManager.getInstance().StopCounting(this);
    }
}
