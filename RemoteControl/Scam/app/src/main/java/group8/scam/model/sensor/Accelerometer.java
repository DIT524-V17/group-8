package group8.scam.model.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import group8.scam.controller.handlers.HandleThread;
import group8.scam.model.main.MainActivity;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * This class implements SensorEventListener for using Accelerometer sensor on the mobile phone device.
 * The sensor will be used for navigating the SmartCar by measuring the angles derived by monitoring
 * the motion of the phone. The application will use the 3 axis data from the phone to perform the navigation.
 *
 * Created by omidm on 4/28/2017.
 */

public class Accelerometer implements SensorEventListener {

    private SensorManager sensorManager;
    private HandleThread mHandle = HandleThread.getInstance();
    private Sensor sensor;
    private String dataStr;
    private boolean isTurning = false;
    private static boolean isAccel = false;
    private double Rx;
    private double Ry;
    private double Rz;

    private long lastUpdate = 0;

    /**
     * Constructor of the class which consists of declaring the Accelerometer sensor as the
     * default sensor type for the sensor manager.
     * @param context
     */

    public Accelerometer (Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * The onPause() method unregisters the usage of Accelerometer sensor in the application
     * allowing to navigate the car with other means.
     */

    public void onPause(){
        sensorManager.unregisterListener(this);
    }

    /**
     * The onResume() method registers the Accelerometer sensor in the application
     * to enable the usage of this sensor in navigating the car.
     */

    public void onResume(){
        sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * The onSensorChanged method monitors the sensor changes which in this case is monitoring
     * the motions of the mobile device. The changes concerned with different axis will be used
     * and implemented in this method.
     * @param event
     */

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER && isAccel){

            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            Rx = atan( x / (sqrt(pow(y,2) + pow(z,2))));
            Rx *= 180.00;
            Rx /= 3.141592;
            Ry = atan( y / (sqrt(pow(x,2) + pow(z,2))));
            Ry *= 180.00;
            Ry /= 3.141592;
            Rz = atan(sqrt(pow(x,2) + pow(y,2)) / z);
            Rz *= 180.00;
            Rz /= 3.141592;

            long curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 25) {
                lastUpdate = curTime;

                int angle = getCarAngle();
                int speed = getCarSpeed();
                dataStr = angle + ":" + speed + ":";

                mHandle.sendMessage(MESSAGE_WRITE, dataStr);
            }
        }
    }

    /**
     * The getCarSpeed method returns the CarSpeed number. This happens by assigning specific angles
     * of the phone in z-axis to relevant amount of acceleration on the car. The further the phone
     * tilted back or forth, the higher speed will be sent
     * to the car.
     *
     * @return CarSpeed
     */

    private int getCarSpeed() {

        if (isTurning) {
            if(Rz > 0){
                return 60;
            }else{
                return -60;
            }
        }
        else if(Rz < 90 && Rz >= 60){
            return 0;
        }
        else if(Rz < 60 && Rz >= 35){
            return 35;
        }
        else if(Rz < 35 && Rz >= 0){
            return 50;
        }
        else if(Rz <= -65 && Rz > -90){
            return 0;
        }
        else if(Rz <= -35 && Rz > -65){
            return -30;
        }
        else if(Rz < 0 && Rz > -35){
            return -50;
        }
        else{
            return 0;
        }
    }

    /**
     * The getCarAngle method returns the rotation angle. The number is acquired based on the y-axis
     * of the phone. The further the phone tilted left or right, the higher angles will be sent
     * to the car.
     *
     * @return CarAngle
     */

    private int getCarAngle() {

        if(Ry <= 15 && Ry > -15){
            isTurning = false;
            return 0;
        }
        else if(Ry < -15 && Ry >= -35){
            isTurning = true;
            return -35;
        }
        else if(Ry < -35 && Ry >= -70){
            isTurning = true;
            return -60;
        }
        else if(Ry <= 35 && Ry > 15){
            isTurning = true;
            return 35;
        }
        else if(Ry <= 70 && Ry > 35){
            isTurning = true;
            return 60;
        }
        else{
            isTurning = false;
            return 0;
        }
    }

    /**
     * onAccuracyChanged method will be called when the accuracy of a sensor has been changed.
     * This method has not been used in our class.
     *
     * @param sensor
     * @param accuracy
     */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * The setIsAccel method returns a boolean which is used in MainActivity class in order to toggle
     * the accelerometer on/off in different manual control methods.
     *
     * @param bool
     */

    public static void setIsAccel(boolean bool) {
        isAccel = bool;
    }
}
