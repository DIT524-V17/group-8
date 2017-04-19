package group8.scam.model.menu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.nio.ByteBuffer;

import group8.scam.R;
import group8.scam.controller.handlers.HandleThread;
import group8.scam.model.main.AutoActivity;
import group8.scam.model.main.MainActivity;

import static group8.scam.model.communication.DataThread.KEY;
import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

/*
    An activity for choosing driving mode
    @Author David Larsson
*/

public class MenuActivity extends AppCompatActivity {

    private HandleThread mHandle = HandleThread.getInstance();
    private String dataStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void btnManual(View view) {
        // Send signal to car to start the manual mode
        dataStr = "m";
        Message msg = mHandle.getHandler().obtainMessage();
        msg.what = MESSAGE_WRITE;
        msg.obj = dataStr;
        msg.sendToTarget();

        // Start the activity
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }

    public void btnAutonomous(View view) {
        // Send signal to car to start the autonomous mode
        dataStr = "a";
        Message msg = mHandle.getHandler().obtainMessage();
        msg.what = MESSAGE_WRITE;
        msg.obj = dataStr;
        msg.sendToTarget();

        // Start the activity
        startActivity(new Intent(MenuActivity.this, AutoActivity.class));
    }
}
