package group8.scam.model.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group8.scam.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.mainColor));

    }
}
