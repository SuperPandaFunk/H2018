package polytechnique.toursita.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import polytechnique.toursita.R;
import polytechnique.toursita.manager.SharedPreferenceManager;

public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("tourista_realm.realm")
                .build();
        Realm.setDefaultConfiguration(config);

        setContentView(R.layout.activity_boot);

        String connectionToken = SharedPreferenceManager.getToken(getApplicationContext());
        String fbToken = "";
        if (!(AccessToken.getCurrentAccessToken() == null))
            fbToken = AccessToken.getCurrentAccessToken().getToken();
        if (connectionToken.equals("") || !connectionToken.equals(fbToken)){
            Intent authIntent = new Intent(this, AuthActivity.class);
            authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(authIntent);
        }else{
            Intent intent = new Intent(this, MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
