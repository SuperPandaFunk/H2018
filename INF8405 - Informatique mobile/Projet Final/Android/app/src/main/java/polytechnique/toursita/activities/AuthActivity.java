package polytechnique.toursita.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import polytechnique.toursita.R;
import polytechnique.toursita.manager.SharedPreferenceManager;

/**
 * Created by Vincent on 2018-03-25.
 */

public class AuthActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.auth_layout);

        LoginButton loginButton = findViewById(R.id.fbLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        loginButton.registerCallback(callbackManager, fbCallBack);
    }

    private FacebookCallback<LoginResult> fbCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {
                            SharedPreferenceManager.setName(Profile.getCurrentProfile().getFirstName(), Profile.getCurrentProfile().getLastName(), getApplicationContext());
                        }
                    });
            request.executeAsync();
            Toast.makeText(getApplicationContext(), SharedPreferenceManager.getName(getApplicationContext()), Toast.LENGTH_LONG).show();
            SharedPreferenceManager.setToken(AccessToken.getCurrentAccessToken().getToken(), AccessToken.getCurrentAccessToken().getUserId(), getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        @Override
        public void onCancel() {
            SharedPreferenceManager.setToken("", "1", getApplicationContext());
            Toast.makeText(getApplicationContext(), R.string.connect_cancel, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException error) {
            SharedPreferenceManager.setToken("", "1", getApplicationContext());
            Toast.makeText(getApplicationContext(), R.string.connect_error, Toast.LENGTH_LONG).show();
        }
    };

}
