package group8.scam.model.radar;

public class Radar {

    private int angleOfServo = 0;
    private int ultrasonicReading = 0;
    private int minAngle = 0;
    private int maxAngle = 0;

    public Radar (int minAngle, int maxAngle) {
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
}
