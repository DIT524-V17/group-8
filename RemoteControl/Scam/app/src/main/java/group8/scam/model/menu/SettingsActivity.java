package group8.scam.model.menu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group8.scam.R;
import group8.scam.model.main.MainActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void btnBack(View view) {
        // Go back to the manual activity, and overriding the animation to switch
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        this.overridePendingTransition(0, 0);
    }
}
