package group8.scam.model.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import group8.scam.R;

/*
    Simple activity to show the user something during the autonomous drive
    @Author David Larsson
*/

//TODO - Add more info

public class AutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // TextView with big scary letters
        TextView ENGAGE = (TextView)findViewById(R.id.txtEngage);

        //Pulsing animation for the text
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(ENGAGE,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }
}
