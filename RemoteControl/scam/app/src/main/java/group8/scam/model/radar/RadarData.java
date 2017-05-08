package group8.scam.model.radar;

import group8.scam.controller.handlers.Observer;
import group8.scam.controller.handlers.Subject;

/**
 * This class handles the Data relating to the Radar such as the angle of the servo, the readings
 * from the ultrasonic sensor, etc
 * Created by @Firas
 */

public class RadarData implements Observer {

    private int angleOfServo = 0;
    private int ultrasonicReading;
    private String dataString;

    public RadarData(int angleOfServo, int ultrasonicReading) {
        this.angleOfServo = angleOfServo;
        this.ultrasonicReading = ultrasonicReading;
        Subject.add(this);
    }

    @Override
    public void update(String data) {
        filterDataIntoDigit(data);
    }

    /**
     * This method is provided with the new Angle of the Servo
     * and stores it locally before sending it over to the Radar
     * to update the position of the pulse
     *
     * @param newAngle - the new angle the Servo is currently at
     */
    private void setAngle(int newAngle) {
        this.angleOfServo = newAngle;
    }

    /**
     * This method retrieves the String sent by the car to the Handler and
     * processes the string by verifying whether a character is a number/digit
     * or not. This ensures that any text indicators and unwanted data are filtered
     * and only the distance reading remains.
     * <p>
     * Furthermore, the string used to retrieve the distance reading is emptied
     * after filtering to ensure future calls of the method are correct.
     *
     * @param servoData - Raw data received as a string from the Handler
     * @see #filterDataIntoDigit(String)
     */
    private void filterServoAngle(String servoData) {
        dataString = "";
        for (int i = 0; i < servoData.length(); i++) {
            if (servoData.charAt(i) == ':') {
                break; //if the character is a column, break from the for loop
            }
            else if (Character.isDigit(servoData.charAt(i))) {
                dataString += servoData.charAt(i);
            }
        }
        setAngle(Integer.parseInt(dataString));
    }

    /**
     * This method retrieves the current Servo Angle and returns it.
     *
     * @return int angleOfServo
     */
    public int getAngleOfServo() {
        return this.angleOfServo;
    }

    /**
     * This method retrieves the String sent by the car to the Handler and
     * processes the string by verifying whether a character is a number/digit
     * or not. This ensures that any text indicators and unwanted data are filtered
     * and only the distance reading remains.
     * <p>
     * Furthermore, the string used to retrieve the distance reading is emptied
     * after filtering to ensure future calls of the method are correct.
     *
     * @param servoData - Raw data received as a string from the Handler
     * @see #filterDataIntoDigit(String)
     */
    private void filterUltrasonicReading(String servoData) {
        dataString = "";
        for (int i = 0; i < servoData.length(); i++) {
            if (servoData.charAt(i) == ':') {
                break; //if the character is a column, break from the for loop
            }
            else if (Character.isDigit(servoData.charAt(i))) {
                dataString += servoData.charAt(i);
            }
        }
        setUltrasonicReading(Integer.parseInt(dataString));
    }

    /**
     * This method retrieves the local ultrasonic distance reading and returns it.
     *
     * @return local ultrasonic distance reading
     */
    public int getUltrasonicReading() {
        return this.ultrasonicReading;
    }

    /**
     * This method sets the new local instance ultrasonic reading.
     *
     * @param newReading
     */
    private void setUltrasonicReading(int newReading) {
        this.ultrasonicReading = newReading;
    }

    /**
     * This method retrieves the String sent by the car to the Handler and
     * processes the string by verifying whether a character is a number/digit
     * or not and going through the String separators.
     * This ensures that any text indicators and unwanted data are filtered
     * and only the distance reading remains.
     * <p>
     * Furthermore, the string used to retrieve the data is emptied
     * after filtering to ensure future calls of the method are correct.
     *
     * @param data - Raw data received as a string from the Handler
     */
    private void filterDataIntoDigit(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (Character.isLetter(data.charAt(i))) {
                if (data.charAt(i) == 'a') {
                    filterServoAngle(data.substring(i, data.length()));//filters and sets angle
                    System.out.println("Filtered Servo data: " + getAngleOfServo());
                }
                else if (data.charAt(i) == 'u') {
                    filterUltrasonicReading(data.substring(i, data.length()));//filters,sets reading
                    System.out.println("Filtered Ultrasonic data: "+ getUltrasonicReading());
                }
            }
        }
    }
}
