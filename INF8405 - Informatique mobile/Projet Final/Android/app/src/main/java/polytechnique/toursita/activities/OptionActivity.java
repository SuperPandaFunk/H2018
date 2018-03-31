package polytechnique.toursita.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

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

public class OptionActivity extends Activity {

    private Button logoutButton, changeName;
    private EditText firstName, lastName;
    private ImageView backArrow;
    private RelativeLayout loadingView;

    private View.OnClickListener logoutButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LoginManager.getInstance().logOut();
            SharedPreferenceManager.setToken("", "", getApplicationContext());
            closeKeyboard();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };

    private View.OnClickListener changeNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (firstName.getText().toString().equals(SharedPreferenceManager.getFirstName(getApplicationContext())) && lastName.getText().toString().equals(SharedPreferenceManager.getLastName(getApplicationContext()))){
                Toast.makeText(getApplicationContext(), "Vous possedez deja ce nom!", Toast.LENGTH_LONG).show();
                return;
            }
            WebService webService = new WebService();
            closeKeyboard();
            showLoadingScreen();
            webService.changeName(AccessToken.getCurrentAccessToken().getUserId(), firstName.getText().toString(), lastName.getText().toString()).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()){
                        SharedPreferenceManager.setName(firstName.getText().toString(), lastName.getText().toString(), getApplicationContext());
                    }
                    hideLoadingScreen();
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    firstName.setText(SharedPreferenceManager.getFirstName(getApplicationContext()));
                    lastName.setText(SharedPreferenceManager.getLastName(getApplicationContext()));
                    hideLoadingScreen();
                    Toast.makeText(getApplicationContext(), "Un probleme c\'est produit lors du changement de nom avec le serveur", Toast.LENGTH_LONG).show();
                }
            });

        }
    };

    View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_layout);
        initializeView();
    }

    private void initializeView(){
        logoutButton = findViewById(R.id.logoutButton);
        changeName = findViewById(R.id.changeName);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        backArrow = findViewById(R.id.backArrow);
        loadingView = findViewById(R.id.loadingScreen);

        firstName.setText(SharedPreferenceManager.getFirstName(this));
        lastName.setText(SharedPreferenceManager.getLastName(this));

        logoutButton.setOnClickListener(logoutButtonListener);
        changeName.setOnClickListener(changeNameListener);
        backArrow.setOnClickListener(backArrowListener);
    }

    protected void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showLoadingScreen(){
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingScreen(){
        loadingView.setVisibility(View.GONE);
    }
}
