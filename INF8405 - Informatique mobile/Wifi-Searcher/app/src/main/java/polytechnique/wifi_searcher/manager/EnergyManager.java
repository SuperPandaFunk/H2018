package polytechnique.wifi_searcher.manager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Vincent on 2018-02-17.
 */

public class EnergyManager {

    private static EnergyManager instance;
    private int totalEnergyConsumed, startEnergy, endEnergy;
    private long startTime, totalTime;
    private boolean counting;

    private EnergyManager(){
        resetAllValues();
        counting = false;
    }

    public static EnergyManager getInstance(){
        if (instance == null){
            instance = new EnergyManager();
        }
        return instance;
    }

    public void StartCounting(Context mContext){
        if (counting)
            return;
        counting = true;
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);
        try {
            startEnergy = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }catch (NullPointerException e){
            Toast.makeText(mContext, "Une erreur c'est produite lors de la lecture du niveau de batterie", Toast.LENGTH_LONG).show();
            startEnergy = 0;
            endEnergy = 0;
            counting = false;
            return;
        }
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    public void StopCounting(Context mContext){
        if (!counting)
            return; //hopefully this never happens
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);
        try {
            endEnergy = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }catch (NullPointerException e){
            Toast.makeText(mContext, "Une erreur c'est produite lors de la lecture du niveau de batterie", Toast.LENGTH_LONG).show();
            startEnergy = 0;
            endEnergy = 0;
            counting = false;
            return;
        }
        if (startEnergy != 0 && endEnergy != 0)
            totalEnergyConsumed += startEnergy - endEnergy;
        else
            totalEnergyConsumed = 0;
        totalTime += Calendar.getInstance().getTimeInMillis() - startTime;
        startEnergy = 0;
        endEnergy = 0;
        counting = false;
    }

    public int getEnergyConsumed(Context mContext){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);
        try {
            endEnergy = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }catch (NullPointerException e) {
            Toast.makeText(mContext, "Une erreur c'est produite lors de la lecture du niveau de batterie", Toast.LENGTH_LONG).show();
            return 0;
        }
        return (startEnergy - endEnergy) + totalEnergyConsumed;
    }

    public long getTotalTime(){ //Le temps est en milliseconde
        return (Calendar.getInstance().getTimeInMillis() - startTime) + totalTime;
    }

    public void resetAllValues(){
        totalEnergyConsumed = 0;
        startEnergy = 0;
        endEnergy = 0;
        totalTime = 0;
        startTime = 0;
    }

}
