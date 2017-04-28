package group8.scam.model.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import group8.scam.R;
import group8.scam.controller.handlers.HandleThread;
import group8.scam.model.menu.SettingsActivity;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

public class MainActivity extends AppCompatActivity {

    private ToggleButton button;
    private String stateString;
    private String dataStr;
    private HandleThread handler = HandleThread.getInstance();
    private Button btnleft, btnright, btnup, btndown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        button = (ToggleButton) findViewById(R.id.togglebutton);

        btnleft = (Button)findViewById(R.id.btnleft);
        btnright = (Button)findViewById(R.id.btnRight);
        btnup = (Button)findViewById(R.id.btnUp);
        btndown = (Button)findViewById(R.id.btnDown);

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

        /*
        *logic for the left button in dpad
        *
         */
        btnleft.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dataStr = "-50:50:";
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    dataStr = " STOP";
                }
                Message msg = handler.getHandler().obtainMessage();
                msg.what = MESSAGE_WRITE;
                msg.obj = dataStr;
                msg.sendToTarget();

                return true;
            }
        });

        /*
        *logic for the dow button in dpad
        *
         */
        btndown.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dataStr = "0:-50:";
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    dataStr = " STOP";
                }
                Message msg = handler.getHandler().obtainMessage();
                msg.what = MESSAGE_WRITE;
                msg.obj = dataStr;
                msg.sendToTarget();

                return true;
            }
        });

        /*
        *logic for the right button in dpad
        *
         */
        btnright.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dataStr = "50:50:";
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    dataStr = " STOP";
                }
                Message msg = handler.getHandler().obtainMessage();
                msg.what = MESSAGE_WRITE;
                msg.obj = dataStr;
                msg.sendToTarget();

                return true;
            }
        });

        /*
        *logic for the up button in dpad
        *
         */
        btnup.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dataStr = "0:50:";
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    dataStr = " STOP";
                }
                Message msg = handler.getHandler().obtainMessage();
                msg.what = MESSAGE_WRITE;
                msg.obj = dataStr;
                msg.sendToTarget();

                return true;
            }
        });

    }

    public void btnSettings(View view) {
        // Start the settings activity, and overriding the animation to switch
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        this.overridePendingTransition(0, 0);
    }


}
