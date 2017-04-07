package group8.scam.model.radar;

public class RadarData {

    private int angleOfServo = 0;
    private int ultrasonicReading;
    private int minAngle = 0;
    private int maxAngle = 0;

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
     * Retrieves the current Servo Angle
     * @return int angleOfServo
     */
    public int getAngle() {
        return this.angleOfServo;
    }

    /**
     * Retrieves the minimum Angle of the Servo
     * @return int minAngle
     */
    public int getMinAngle() {
        return this.minAngle;
    }

    /**
     * Retrieves the maximum angle of the Servo
     * @return int maxAngle
     */
    public int getMaxAngle() {
        return this.maxAngle;
    }

    /**
     * Updates the ultrasonic reading
     * @param reading: the numbers from the Ultrasonic sensor
     */
    public void updateRadar(int reading) {
        this.ultrasonicReading = reading;
    }

    /**
     * Retrieves ultrasonic reading
     * @return int ultrasonic reading
     */
    public int getUltrasonicReading() {
        return this.ultrasonicReading;
    }
}
