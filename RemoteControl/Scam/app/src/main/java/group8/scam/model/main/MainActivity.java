package group8.scam.model.main;

import android.content.pm.ActivityInfo;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import group8.scam.R;

public class MainActivity extends AppCompatActivity {

    TextView txtdev = (TextView) findViewById(R.id.txtdev);
    public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        txtdev.setText("Starting");
        mainActivity = this;
    }

    public void developer(String msg){
        txtdev.setText(msg);
        System.out.println("Called developer");
    }

}
