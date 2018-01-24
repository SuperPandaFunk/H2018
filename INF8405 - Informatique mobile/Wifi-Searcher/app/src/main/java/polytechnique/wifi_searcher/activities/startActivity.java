package polytechnique.wifi_searcher.activities;

import android.Manifest;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import polytechnique.wifi_searcher.R;

public class startActivity extends AppCompatActivity {

    //API key : AIzaSyBu_Kp6-jjKYhsB40W56nAWIZt-OT0H8TY

    private Button startButton;

    private View.OnClickListener startButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent startMapActivity = new Intent(startActivity.this, mapActivity.class);
            startActivity(startMapActivity);
           // finish();
        }
    };

    private void initializeView(){
        startButton = (Button)findViewById(R.id.startButton);

        startButton.setOnClickListener(startButtonClick);
    }

    private void initializePermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("my_book_realm.realm")
                .build();
        Realm.setDefaultConfiguration(config);


        setContentView(R.layout.activity_start);
        initializeView();
        initializePermission();
    }

}
