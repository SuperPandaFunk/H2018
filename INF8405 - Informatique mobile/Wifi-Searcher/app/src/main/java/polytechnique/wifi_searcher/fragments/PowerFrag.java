package polytechnique.wifi_searcher.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import polytechnique.wifi_searcher.R;

/**
 * Created by vince on 2018-01-24.
 */

public class PowerFrag extends Fragment {


    private void initializeView(View view){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.power_managment, container, false);
        initializeView(view);
        return view;
    }
}
