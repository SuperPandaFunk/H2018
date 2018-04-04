package polytechnique.toursita.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import polytechnique.toursita.R;
import polytechnique.toursita.fragments.NavigationFrag;
import polytechnique.toursita.fragments.ViewPagerFrag;
import polytechnique.toursita.manager.EnergyManager;

/**
 * Created by Vincent on 2018-03-25.
 */

public class MapActivity extends AppCompatActivity implements SensorEventListener{

    private ImageButton optionButton;
    private RelativeLayout loadingLayout;
    private SensorManager sensorManager;
    private float temperature, humidity;

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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperature = 0.0f;
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
        loadAmbientTemperature();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EnergyManager.getInstance().StopCounting(this);
        unregisterAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EnergyManager.getInstance().StopCounting(this);
    }

    private void loadAmbientTemperature() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            EnergyManager.getInstance().setIsTemperatureSensor(true);
        } else {
            EnergyManager.getInstance().setIsTemperatureSensor(false);
        }

        if (humiditySensor != null){
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
            EnergyManager.getInstance().setIsHumiditySensor(true);
        }else{
            EnergyManager.getInstance().setIsHumiditySensor(false);
        }
    }

    private void unregisterAll() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do stuff
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values.length > 0) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temperature = sensorEvent.values[0];
                EnergyManager.getInstance().setTemperature(temperature);
            }else{
                humidity = sensorEvent.values[0];
                EnergyManager.getInstance().setHumidity(humidity);
            }
        }
    }
}
