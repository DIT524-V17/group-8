package group8.scam.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by omidm on 4/28/2017.
 */

public class Accelerometer implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    double x;
    double y;
    double z;

    private long lastUpdate = 0;

    public Accelerometer (Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onPause(){
        sensorManager.unregisterListener(this);
    }
    public void onResume(){
        sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            double Rx = atan( x / (sqrt(pow(y,2) + pow(z,2))));
            Rx *= 180.00;
            Rx /= 3.141592;
            double Ry = atan( y / (sqrt(pow(x,2) + pow(z,2))));
            Ry *= 180.00;
            Ry /= 3.141592;
            double Rz = atan(sqrt(pow(x,2) + pow(y,2)) / z);
            Rz *= 180.00;
            Rz /= 3.141592;

            long curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 100) {
                lastUpdate = curTime;

                System.out.println("X is: " + Rx);
                System.out.println("Y is: " + Ry);
                System.out.println("Z is: " + Rz);
                System.out.println(" ");
                System.out.println(" ");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
