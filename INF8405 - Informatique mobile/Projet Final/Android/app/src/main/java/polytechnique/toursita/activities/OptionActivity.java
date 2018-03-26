package polytechnique.toursita.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import polytechnique.toursita.R;
import polytechnique.toursita.manager.SharedPreferenceManager;

/**
 * Created by Vincent on 2018-03-25.
 */

public class OptionActivity extends Activity {

    private Button logoutButton, changeName;
    private EditText firstName, lastName;
    private ImageView backArrow;

    private View.OnClickListener logoutButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LoginManager.getInstance().logOut();
            SharedPreferenceManager.setToken("", "", getApplicationContext());
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
            SharedPreferenceManager.setName(firstName.getText().toString(), lastName.getText().toString(), getApplicationContext());
            Toast.makeText(getApplicationContext(), "Vous avez change votre nom!", Toast.LENGTH_LONG).show();
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

        firstName.setText(SharedPreferenceManager.getFirstName(this));
        lastName.setText(SharedPreferenceManager.getLastName(this));

        logoutButton.setOnClickListener(logoutButtonListener);
        changeName.setOnClickListener(changeNameListener);
        backArrow.setOnClickListener(backArrowListener);
    }
}
