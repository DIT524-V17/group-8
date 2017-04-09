package group8.scam.model.menu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group8.scam.R;
import group8.scam.model.main.MainActivity;

/*
    An activity for choosing driving mode
    @Author David Larsson
*/

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void btnManual(View view) {
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }

    public void btnAutonomous(View view) {
        // TODO - Send the signal to start the autonomous mode
        // TODO - Start the autonomous activity
    }
}
