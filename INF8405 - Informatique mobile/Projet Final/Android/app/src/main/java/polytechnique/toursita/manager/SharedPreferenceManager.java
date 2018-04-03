package polytechnique.toursita.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import polytechnique.toursita.R;

/**
 * Created by Vincent on 2018-03-25.
 */

public class SharedPreferenceManager {

    public static void setToken(String token, String userId, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.applicationToken), token);
        editor.putString(context.getString(R.string.userId), userId);
        editor.apply();
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.applicationToken),"");
    }

    public static String getUserId(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.userId),"");
    }

    public static void setName(String firstName, String lastName, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.firstName), firstName);
        editor.putString(context.getString(R.string.lastName), lastName);
        editor.apply();
    }

    public static String getName(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.firstName),"") + " " + sharedPreferences.getString(context.getString(R.string.lastName),"");
    }

    public static String getFirstName(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.firstName),"");
    }

    public static String getLastName(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.lastName),"");
    }

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

    public static int getTotalEnergy(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getInt("energy", 0);
    }
}
