package polytechnique.wifi_searcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import polytechnique.wifi_searcher.R;

public class startActivity extends AppCompatActivity {

    //API key : AIzaSyBu_Kp6-jjKYhsB40W56nAWIZt-OT0H8TY

    private Button startButton;

    private View.OnClickListener startButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent startMapActivity = new Intent(startActivity.this, mapActivity.class);
            startActivity(startMapActivity);
            finish();
        }
    };

    private void initializeView(){
        startButton = (Button)findViewById(R.id.startButton);

        startButton.setOnClickListener(startButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initializeView();
    }


}
