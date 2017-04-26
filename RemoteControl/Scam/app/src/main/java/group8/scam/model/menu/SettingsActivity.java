package group8.scam.model.menu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import group8.scam.R;
import group8.scam.model.main.MainActivity;

/*
    A class to handle the different settings the user has access to
    @Authors David Larsson
*/

// TODO - Add the logic and a class for holding settings

public class SettingsActivity extends AppCompatActivity {
    private ToggleButton btnJoystick;
    private ToggleButton btnDpad;
    private ToggleButton btnGyroscope;
    private ToggleButton btnSafety;

    private static Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btnJoystick = (ToggleButton)findViewById(R.id.btnJoyStick);
        btnDpad = (ToggleButton)findViewById(R.id.btnDpad);
        btnGyroscope = (ToggleButton)findViewById(R.id.btnGyroScope);
        btnSafety = (ToggleButton)findViewById(R.id.btnSafety);

    }

    public void btnBack(View view) {
        // Go back to the manual activity, and overriding the animation to switch
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        this.overridePendingTransition(0, 0);
    }

    public void btnJoystick(View view) {
        // TODO - Insert logic

        // Setting the button on, and the rest off
        btnJoystick.setChecked(true);
        btnDpad.setChecked(false);
        btnGyroscope.setChecked(false);
    }

    public void btnDpad(View view) {
        // TODO - Insert logic

        // Setting the button on, and the rest off
        btnJoystick.setChecked(false);
        btnDpad.setChecked(true);
        btnGyroscope.setChecked(false);
    }

    public void btnGyroscope(View view) {
        // TODO - Insert logic

        // Setting the button on, and the rest off
        btnJoystick.setChecked(false);
        btnDpad.setChecked(false);
        btnGyroscope.setChecked(true);
    }

    public void btnSafety(View view) {
        // TODO - Insert logic
    }

    @Override
    public void onPause() {
        super.onPause();

        // Saving the states of the toggle buttons
        bundle.putBoolean("joystickState", btnJoystick.isChecked());
        bundle.putBoolean("gyroscopeState", btnGyroscope.isChecked());
        bundle.putBoolean("dpadState", btnDpad.isChecked());
        bundle.putBoolean("safetyState", btnSafety.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming the states of the toggle buttons
        btnJoystick.setChecked(bundle.getBoolean("joystickState",false));
        btnGyroscope.setChecked(bundle.getBoolean("gyroscopeState",false));
        btnDpad.setChecked(bundle.getBoolean("dpadState", false));
        btnSafety.setChecked(bundle.getBoolean("safetyState", false));
    }
}
