package group8.scam.model.radar;

public class RadarData {

    private int angleOfServo = 0;
    private int ultrasonicReading;
    private int minAngle = 0;
    private int maxAngle = 0;
    private String servoString;

    public RadarData(int minAngle, int maxAngle) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    /**
     * Modifies the current Servo angle
     * @param newAngle the new angle the Servo is currently at
     */
    public void setAngle(int newAngle) {
        this.angleOfServo = newAngle;
    }

    /**
     * This method retrieves the current Servo Angle and returns it.
     *
     * @return int angleOfServo
     */
    public int getAngle() {
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
     */
    public int filterUltrasonicReading(String servoData) {

        for (int i = 0; i < servoData.length(); i++) {
            if (Character.isDigit(servoData.charAt(i))) {
                servoString += servoData.charAt(i);
            }
        }

        servoString = "";
        this.ultrasonicReading = Integer.parseInt(servoString);
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
}
