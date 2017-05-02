package group8.scam.model.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import group8.scam.R;
import group8.scam.controller.handlers.HandleThread;
import group8.scam.controller.handlers.Observer;
import group8.scam.controller.handlers.Subject;
import group8.scam.model.menu.SettingsActivity;
import group8.scam.model.dpad.DpadLogic;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

public class MainActivity extends AppCompatActivity implements Observer {

    private ToggleButton button;
    private String stateString;

    private HandleThread handler = HandleThread.getInstance();

    private Button btnleft, btnright, btnup, btndown;

    private ImageView safetyLed;
    private TextView txtSafety;
    private TextView txtAuto;
    private TextView txtSpeed;
    private TextView txtDistance;

    DpadLogic dpadlogic = new DpadLogic();

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

        txtSpeed = (TextView)findViewById(R.id.speedLbl);
        txtDistance = (TextView)findViewById(R.id.distanceLbl);

        button = (ToggleButton) findViewById(R.id.togglebutton);

        btnleft = (Button)findViewById(R.id.btnleft);
        btnright = (Button)findViewById(R.id.btnRight);
        btnup = (Button)findViewById(R.id.btnUp);
        btndown = (Button)findViewById(R.id.btnDown);

        // TODO - Move all of Dpad into one view
        findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
        btndown.setVisibility(View.INVISIBLE);
        btnup.setVisibility(View.INVISIBLE);
        btnleft.setVisibility(View.INVISIBLE);
        btnright.setVisibility(View.INVISIBLE);

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                /*
                *@Firas: Switch between auto and manual. Send a for auto, m for manual
                 */
                if (isChecked) {
                   stateString = "a"; //TODO - car is forced to stop before switching to AUTO

                    // Remove the means of controlling the car manually
                    findViewById(R.id.joystick).setVisibility(View.INVISIBLE);

                    // TODO - Move all of Dpad into one view
                    findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                    btndown.setVisibility(View.INVISIBLE);
                    btnup.setVisibility(View.INVISIBLE);
                    btnleft.setVisibility(View.INVISIBLE);
                    btnright.setVisibility(View.INVISIBLE);

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

                            // TODO - Move all of Dpad into one view
                            findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                            btndown.setVisibility(View.INVISIBLE);
                            btnup.setVisibility(View.INVISIBLE);
                            btnleft.setVisibility(View.INVISIBLE);
                            btnright.setVisibility(View.INVISIBLE);

                            // TODO - Add gyro
                            break;

                        case DPAD:
                            txtAuto.setVisibility(View.INVISIBLE);
                            findViewById(R.id.joystick).setVisibility(View.INVISIBLE);

                            // TODO - Move all of Dpad into one view
                            findViewById(R.id.dpadView).setVisibility(View.VISIBLE);
                            btndown.setVisibility(View.VISIBLE);
                            btnup.setVisibility(View.VISIBLE);
                            btnleft.setVisibility(View.VISIBLE);
                            btnright.setVisibility(View.VISIBLE);

                            // TODO - Add gyro
                            break;

                        case GYROSCOPE:
                            txtAuto.setVisibility(View.INVISIBLE);
                            findViewById(R.id.joystick).setVisibility(View.INVISIBLE);

                            // TODO - Move all of Dpad into one view
                            findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                            btndown.setVisibility(View.INVISIBLE);
                            btnup.setVisibility(View.INVISIBLE);
                            btnleft.setVisibility(View.INVISIBLE);
                            btnright.setVisibility(View.INVISIBLE);

                            // TODO - Add gyro
                            break;
                    }
                }
                handler.sendMessage(MESSAGE_WRITE, stateString);
            }
        });

        /*
        *logic for the left button in dpad
        *
         */
        btnleft.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){
                return dpadlogic.left(event);
            }
        });

        btndown.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){
                return dpadlogic.down(event);
            }
        });

        btnright.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){
                return dpadlogic.right(event);
            }
        });

        btnup.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){

                return dpadlogic.up(event);
            }
        });

        Subject.add(this);
    }

    // sXX:dXX:aXX:uXX:
    // s = speed, d = distance, a = angle, u = ultrasonic
    @Override
    public void update(String data) {
        int speedBeginIndex = 0;
        int speedEndIndex = 0;
        int distanceBeginIndex = 0;
        int distanceEndIndex = 0;
        for (int i = 0; i < data.length(); i++ ){
            if (Character.isLetter(data.charAt(i)) && data.charAt(i) == 's'){
                speedBeginIndex = i+1;
            }else if (data.charAt(i) == ':' && speedBeginIndex > 0){
                speedEndIndex = i+1;
                break;
            }
        }
        for (int i = 0; i < data.length(); i++ ){
            if (Character.isLetter(data.charAt(i)) && data.charAt(i) == 'd'){
                distanceBeginIndex = i+1;
            }else if (data.charAt(i) == ':' && distanceBeginIndex > 0){
                distanceEndIndex = i+1;
                break;
            }
        }

        String speed = data.substring(speedBeginIndex, speedEndIndex - 1);
        String distance = data.substring(distanceBeginIndex, distanceEndIndex - 1);
        updateView("Speed: " + speed,"Distance: " + distance );
    }

    public void updateView(final String data,final String eller) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtSpeed.setText(data);
                txtDistance.setText(eller);

            }
        });
    }

    public void btnSettings(View view) {
        // Start the settings activity, and overriding the animation to switch
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        this.overridePendingTransition(0, 0);
    }



    @Override
    protected void onResume() {
        super.onResume();

        // Logic to change the driving mode available
        SettingsActivity.DrivingMode driveMode = SettingsActivity.getCurrentDrivingMode();
        switch(driveMode) {
            case JOYSTICK:
                findViewById(R.id.joystick).setVisibility(View.VISIBLE);

                // TODO - Move all of Dpad into one view
                findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                btndown.setVisibility(View.INVISIBLE);
                btnup.setVisibility(View.INVISIBLE);
                btnleft.setVisibility(View.INVISIBLE);
                btnright.setVisibility(View.INVISIBLE);

                // TODO - Add gyro
                break;

            case DPAD:
                findViewById(R.id.joystick).setVisibility(View.INVISIBLE);

                // TODO - Move all of Dpad into one view
                findViewById(R.id.dpadView).setVisibility(View.VISIBLE);
                btndown.setVisibility(View.VISIBLE);
                btnup.setVisibility(View.VISIBLE);
                btnleft.setVisibility(View.VISIBLE);
                btnright.setVisibility(View.VISIBLE);

                // TODO - Add gyro
                break;

            case GYROSCOPE:
                findViewById(R.id.joystick).setVisibility(View.INVISIBLE);

                // TODO - Move all of Dpad into one view
                findViewById(R.id.dpadView).setVisibility(View.INVISIBLE);
                btndown.setVisibility(View.INVISIBLE);
                btnup.setVisibility(View.INVISIBLE);
                btnleft.setVisibility(View.INVISIBLE);
                btnright.setVisibility(View.INVISIBLE);

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
