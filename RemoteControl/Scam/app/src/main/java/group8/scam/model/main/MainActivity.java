package group8.scam.model.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import group8.scam.R;
import group8.scam.controller.handlers.HandleThread;
import group8.scam.model.menu.SettingsActivity;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

public class MainActivity extends AppCompatActivity {

    private ToggleButton button;
    private String stateString;
    private HandleThread handler = HandleThread.getInstance();
    private ImageView safetyLed;
    private TextView txtSafety;
    private TextView txtAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        safetyLed = (ImageView) findViewById(R.id.safetyLed);
        safetyLed.setImageResource(R.drawable.off30dp);

        txtSafety = (TextView)findViewById(R.id.txtSafety);
        txtSafety.setText("Safety Off");

        txtAuto = (TextView)findViewById(R.id.txtAuto);
        txtAuto.setVisibility(View.INVISIBLE);

        button = (ToggleButton) findViewById(R.id.togglebutton);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                /*
                *@Firas: Switch between auto and manual. Send a for auto, m for manual
                 */
                if (isChecked) {
                   stateString = "a";

                    // Remove the means of controlling the car manually
                    findViewById(R.id.joystick).setVisibility(View.INVISIBLE);
                    findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                    // TODO - Add gyro

                    txtAuto.setVisibility(View.VISIBLE);

                } else {
                    stateString = "m";

                    // Logic to change back to current drivemode
                    SettingsActivity.DrivingMode driveMode = SettingsActivity.getCurrentDrivingMode();
                    switch(driveMode) {
                        case JOYSTICK:
                            txtAuto.setVisibility(View.INVISIBLE);
                            findViewById(R.id.joystick).setVisibility(View.VISIBLE);
                            findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                            // TODO - Add gyro
                            break;

                        case DPAD:
                            txtAuto.setVisibility(View.INVISIBLE);
                            findViewById(R.id.joystick).setVisibility(View.INVISIBLE);
                            findViewById(R.id.dpadView).setVisibility(View.VISIBLE);
                            // TODO - Add gyro
                            break;

                        case GYROSCOPE:
                            txtAuto.setVisibility(View.INVISIBLE);
                            findViewById(R.id.joystick).setVisibility(View.INVISIBLE);
                            findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                            // TODO - Add gyro
                            break;
                    }
                }
                Message msg = handler.getHandler().obtainMessage();
                msg.what = MESSAGE_WRITE;
                msg.obj = stateString;
                msg.sendToTarget();
            }
        });
    }

    public void btnSettings(View view) {
        // Start the settings activity, and overriding the animation to switch
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        this.overridePendingTransition(0, 0);
    }


    // The logic for the dpad buttons
    public void btnUp(View view){

    }

    public void btnDown(View view){

    }

    public void btnRight(View view){

    }

    public void btnLeft(View view){

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logic to change the driving mode available
        SettingsActivity.DrivingMode driveMode = SettingsActivity.getCurrentDrivingMode();
        switch(driveMode) {
            case JOYSTICK:
                findViewById(R.id.joystick).setVisibility(View.VISIBLE);
                findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                // TODO - Add gyro
                break;

            case DPAD:
                findViewById(R.id.joystick).setVisibility(View.INVISIBLE);
                findViewById(R.id.dpadView).setVisibility(View.VISIBLE);
                // TODO - Add gyro
                break;

            case GYROSCOPE:
                findViewById(R.id.joystick).setVisibility(View.INVISIBLE);
                findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                // TODO - Add gyro
                break;
        }

        // Logic to change the "led" for the safety in the GUI
        boolean safety = SettingsActivity.getSafety();
        if(safety){
            safetyLed.setImageResource(R.drawable.on30dp);
            txtSafety.setText("Safety On");
        }
        else{
            safetyLed.setImageResource(R.drawable.off30dp);
            txtSafety.setText("Safety Off");
        }
    }
}
