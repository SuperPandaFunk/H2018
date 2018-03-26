package polytechnique.toursita.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
