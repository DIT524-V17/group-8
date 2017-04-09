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

    private HandleThread mHandler = HandleThread.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void btnManual(View view) {
        // Send signal to car to start the manual mode
        byte[] bytes = ByteBuffer.allocate(4).putChar('m').array();

        Bundle bundle = new Bundle();
        bundle.putByteArray(KEY, bytes);

        Message msg = mHandler.getHandler().obtainMessage();
        msg.setData(bundle);
        msg.what = MESSAGE_WRITE;
        msg.sendToTarget();

        // Start the activity
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }

    public void btnAutonomous(View view) {
        // Send signal to car to start the autonomous mode
        byte[] bytes = ByteBuffer.allocate(4).putChar('a').array();

        Bundle bundle = new Bundle();
        bundle.putByteArray(KEY, bytes);

        Message msg = mHandler.getHandler().obtainMessage();
        msg.setData(bundle);
        msg.what = MESSAGE_WRITE;
        msg.sendToTarget();

        // Start the activity
        startActivity(new Intent(MenuActivity.this, AutoActivity.class));
    }
}
