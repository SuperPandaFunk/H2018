package polytechnique.toursita.manager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.net.TrafficStats;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Calendar;

public class EnergyManager {
    private static EnergyManager instance;
    private int totalEnergyConsumed, startEnergy, endEnergy;
    private long startTime, totalTime;
    private boolean counting;
    private TrafficStats internet;
    private float temperature, humidity;
    private boolean isTemperatureSensor, isHumiditySensor;

     private EnergyManager(){
        totalEnergyConsumed = 0;
        startEnergy = 0;
        endEnergy = 0;
        totalTime = 0;
        startTime = 0;
        temperature = 0.0f;
        humidity = 0.0f;
        isTemperatureSensor = false;
        isHumiditySensor = false;
        counting = false;
        internet = new TrafficStats();
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
        totalTime = SharedPreferenceManager.getTotalTime(mContext);
        totalEnergyConsumed = SharedPreferenceManager.getTotalEnergy(mContext);
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
        SharedPreferenceManager.setTotalEnergy(mContext, totalEnergyConsumed);
        SharedPreferenceManager.setTotalTime(mContext, totalTime);

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

    public int getBatteryLevel(Context mContext){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }

    public void resetAllValues(Context mContext){
        totalEnergyConsumed = 0;
        startEnergy = getBatteryLevel(mContext);
        endEnergy = 0;
        totalTime = 0;
        startTime = Calendar.getInstance().getTimeInMillis();
        SharedPreferenceManager.setTotalEnergy(mContext, 0);
        SharedPreferenceManager.setTotalTime(mContext, 0);
    }

    public long getDownload(){
        return internet.getUidRxBytes(android.os.Process.myUid());
    }

    public long getUploads(){
        return internet.getUidTxBytes(android.os.Process.myUid());
    }

    public String getDataInfo()
    {
         String Download = getSuffix(getDownload());
         String Upload = getSuffix(getUploads());
         return "Download: " + Download + " | Upload: " + Upload;
    }

    private String getSuffix(long val){
        String Dsuffix = "";
        if(val > 1073741824){
            Dsuffix = val/1073741824 + " Go";
        }else if(val > 1048576) {
            Dsuffix = val/1048576 + " Mo";
        }else if (val > 1024){
            Dsuffix = val/1024 + " Ko";
        }else {
            Dsuffix = val + " o";
        }
        return Dsuffix;
    }

    public String temperature(){
        return getCpuTemp() +" °C";
    }

    public float getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Float.parseFloat(line) / 1000.0f;

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public String getTemperature(){
         if (isTemperatureSensor)
            return String.format("%.02f", temperature);
         return "Aucun sensor sur cet appareil";
    }

    public void setTemperature(float value){
         temperature = value;
    }

    public void setIsTemperatureSensor(boolean value){
         isTemperatureSensor = value;
    }

    public String getHumidity(){
        if (isHumiditySensor)
            return String.format("%.02f", humidity);
        return "Aucun sensor sur cet appareil";
    }

    public void setHumidity(float value){
        humidity = value;
    }

    public void setIsHumiditySensor(boolean value){
        isHumiditySensor = value;
    }

}
