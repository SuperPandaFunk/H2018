package polytechnique.toursita.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import polytechnique.toursita.R;
import polytechnique.toursita.manager.SharedPreferenceManager;
import polytechnique.toursita.webService.RegisterResponse;
import polytechnique.toursita.webService.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vincent on 2018-03-25.
 */

public class AuthActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private WebService webService;

    Callback<RegisterResponse> isUserExist = new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
            if (response.body() != null){
                saveUserAndGo(response.body()._id);
            }
            else {
                webService.registerFacebook(AccessToken.getCurrentAccessToken().getUserId(), SharedPreferenceManager.getFirstName(getApplicationContext()), SharedPreferenceManager.getLastName(getApplicationContext()))
                        .enqueue(registerUserCallBack);
            }
        }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Une erreur c\'est produite lors de la requete au serveur", Toast.LENGTH_LONG).show();
        }
    };

    Callback<RegisterResponse> registerUserCallBack = new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
            saveUserAndGo(response.body()._id);
        }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Une erreur c\'est produite lors de la requete au serveur", Toast.LENGTH_LONG).show();
        }
    };

    private void saveUserAndGo(String id){
        SharedPreferenceManager.setToken(AccessToken.getCurrentAccessToken().getToken(), id, getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

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
        webService = new WebService();
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
            //// Query au serveur /////
            webService.getUser(AccessToken.getCurrentAccessToken().getUserId()).enqueue(isUserExist);
            ///////////////////////////
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


    public void printHashKey(Context pContext) {
        String TAG = "hash";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
