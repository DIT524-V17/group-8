package group8.scam.model.radar;

public class RadarData {

    private int angleOfServo = 0;
    private int ultrasonicReading;
    private int minAngle = 0;
    private int maxAngle = 0;
    private String dataString;

    public RadarData(int angleOfServo) {
        this.angleOfServo = angleOfServo;
    }

    /**
     * This method is provided with the new Angle of the Servo
     * and stores it locally before sending it over to the Radar
     * to update the position of the pulse
     *
     * @param newAngle - the new angle the Servo is currently at
     */
    public void setAngle(int newAngle) {
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
     * @return the angle reading from the Servo
     * @see #filterDataIntoDigit(String)
     */
    public int filterServoAngle(String servoData) {
        this.angleOfServo = Integer.parseInt(filterDataIntoDigit(servoData));
        return getAngleOfServo();
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
     * @return the distance reading from the ultrasonic sensor
     * @see #filterDataIntoDigit(String)
     */
    public int filterUltrasonicReading(String servoData) {
        this.ultrasonicReading = Integer.parseInt(filterDataIntoDigit(servoData));
        return getUltrasonicReading();
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
     * This method retrieves the String sent by the car to the Handler and
     * processes the string by verifying whether a character is a number/digit
     * or not. This ensures that any text indicators and unwanted data are filtered
     * and only the distance reading remains.
     * <p>
     * Furthermore, the string used to retrieve the data is emptied
     * after filtering to ensure future calls of the method are correct.
     *
     * @param data - Raw data received as a string from the Handler
     * @return the filtered data as a String
     */
    public String filterDataIntoDigit(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (Character.isDigit(data.charAt(i))) {
                dataString += data.charAt(i);
            }
        }
        dataString = data;
        return data;
    }
}
