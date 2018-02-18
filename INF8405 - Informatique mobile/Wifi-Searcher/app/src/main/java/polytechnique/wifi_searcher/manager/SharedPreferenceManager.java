package polytechnique.wifi_searcher.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Vincent on 2018-02-18.
 */

public class SharedPreferenceManager {

    public static void setTotalTime(Context mContext, long time){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time", time);
        editor.apply();
    }

    public static void setTotalEnergy(Context mContext, int energy){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("energy", energy);
        editor.apply();
    }

    public static long getTotalTime(Context mContext){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getLong("time", 0);
    }

    public static int getTotalEnergy(Context mContext){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getInt("energy", 0);
    }
}
