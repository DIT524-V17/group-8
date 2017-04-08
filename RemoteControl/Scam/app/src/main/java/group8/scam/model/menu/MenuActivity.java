package group8.scam.model.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group8.scam.R;

/*
    An ac
    @Authors David Larsson & Samuel Bäckström
*/

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void btnManual(View view) {
        // MANUAL BUTTON
    }

    public void btnAutonomous(View view) {
        // AUTONOMOUS BUTTON
    }
}
