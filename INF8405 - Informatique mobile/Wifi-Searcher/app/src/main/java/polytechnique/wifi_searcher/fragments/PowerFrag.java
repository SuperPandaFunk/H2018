package polytechnique.wifi_searcher.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import polytechnique.wifi_searcher.R;
import polytechnique.wifi_searcher.manager.EnergyManager;

/**
 * Created by vince on 2018-01-24.
 */

public class PowerFrag extends Fragment {

    private TextView quantity, time;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshLayout();
        }
    };

    private void initializeView(View view){
        quantity = (TextView)view.findViewById(R.id.quantity);
        time = (TextView)view.findViewById(R.id.time);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(swipeListener);

        refreshLayout();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.power_managment, container, false);
        initializeView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout(){
        quantity.setText(Integer.toString(EnergyManager.getInstance().getEnergyConsumed(getContext())));
        time.setText(Long.toString(EnergyManager.getInstance().getTotalTime()));
        swipeRefreshLayout.setRefreshing(false);
    }
}
