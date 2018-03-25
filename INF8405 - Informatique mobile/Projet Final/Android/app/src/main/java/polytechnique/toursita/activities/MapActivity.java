package polytechnique.toursita.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import polytechnique.toursita.R;
import polytechnique.toursita.fragments.MapFrag;

/**
 * Created by Vincent on 2018-03-25.
 */

public class MapActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_container);

        if (savedInstanceState == null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            MapFrag mapFrag = new MapFrag();
            ft.add(R.id.main_container, mapFrag);
            ft.commit();
        }
    }
}
