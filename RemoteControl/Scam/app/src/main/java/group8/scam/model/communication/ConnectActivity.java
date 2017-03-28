package group8.scam.model.communication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import group8.scam.R;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ConnectActivity extends AppCompatActivity {

    private JoystickView joystick;
    private TextView angleView;
    private TextView strengthView;
    private String angleData;
    private String strengthData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        joystick = (JoystickView) findViewById(R.id.joystickView);
        angleView = (TextView) findViewById(R.id.angleView);
        strengthView = (TextView) findViewById(R.id.strengthView);
        onMove();
    }

    private void onMove() {
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                angleData = String.valueOf(angle);
                strengthData = String.valueOf(strength);
                angleView.setText("Angle: " + angleData);
                strengthView.setText("Strength: " + strengthData);
            }
        },17);
    }
}
