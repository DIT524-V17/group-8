package group8.scam.model.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group8.scam.R;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    private JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joystick = (JoystickView) findViewById(R.id.joystickView);
        onMove();
    }

    private void onMove() {
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                System.out.println(angle + " " + strength);
            }
        });
    }
}
