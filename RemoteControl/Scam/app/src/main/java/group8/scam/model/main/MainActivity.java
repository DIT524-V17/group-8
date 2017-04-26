package group8.scam.model.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import group8.scam.R;
import group8.scam.controller.handlers.HandleThread;
import group8.scam.model.menu.SettingsActivity;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

public class MainActivity extends AppCompatActivity {

    private ToggleButton button;
    private String stateString;
    private HandleThread handler = HandleThread.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                /*
                *@Firas: Switch between auto and manual. Send a for auto, m for manual
                 */
                if (isChecked) {
                   stateString = "a";
                } else {
                    stateString = "m";
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
}
