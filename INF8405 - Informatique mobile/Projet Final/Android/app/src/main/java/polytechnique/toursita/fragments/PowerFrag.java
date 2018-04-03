package polytechnique.toursita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import polytechnique.toursita.R;
import polytechnique.toursita.manager.EnergyManager;

public class PowerFrag extends Fragment{

    private TextView quantity, time, totalTime, hourEstimate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button reset;


    private SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshLayout();
        }
    };

    private View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EnergyManager.getInstance().resetAllValues(getContext());
            refreshLayout();
        }
    };

    private void initializeView(View view){
        quantity = view.findViewById(R.id.quantity);
        time = view.findViewById(R.id.time);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        totalTime = view.findViewById(R.id.totalTime);
        hourEstimate = view.findViewById(R.id.consumption);
        reset = view.findViewById(R.id.resetButton);

        swipeRefreshLayout.setOnRefreshListener(swipeListener);
        reset.setOnClickListener(resetListener);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread t = new Thread(){
            @Override
            public void run() {
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout();
                            }
                        });
                    }
                }catch (InterruptedException e){ }
            }
        };
        t.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout(){
        quantity.setText(Integer.toString(EnergyManager.getInstance().getEnergyConsumed(getContext())) + "%");
        time.setText(parseTime(EnergyManager.getInstance().getTotalTime()));
        long tmp = EnergyManager.getInstance().getTotalTime();
        double nbHeure = tmp / 3600000.0; //60s * 60m * 1000ms
        double consommation = EnergyManager.getInstance().getEnergyConsumed(getContext()) / nbHeure;
        hourEstimate.setText(String.format("%.02f", consommation) + "% / hrs");
        totalTime.setText(parseHourToString(EnergyManager.getInstance().getBatteryLevel(getContext()) / consommation) + " restant");

        swipeRefreshLayout.setRefreshing(false);
    }

    private String parseTime(long time){
        int second = (int)((time / 1000) % 60); //1000ms
        int minute = (int)((time / 60000) % 60); //60s * 1000ms
        int hours = (int)((time / 3600000) % 60); //60s * 60m * 1000ms
        return Integer.toString(hours) + "h, " + Integer.toString(minute) + "m, " + Integer.toString(second) + "s";
    }

    private String parseHourToString(double hour){
        int newhour = (int)hour;
        double minute = (hour - Math.floor(hour)) * 60;
        int second = (int)((minute - Math.floor(minute)) * 60);

        return Integer.toString(newhour) + "h, " + Integer.toString((int)minute) + "m, " + Integer.toString(second) + "s";
    }
}
