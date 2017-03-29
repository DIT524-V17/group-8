package group8.scam.model.radar;

public class Radar {

    private int angleOfServo = 0;
    private int ultrasonicReading = 0;

    public Radar (int angleOfServo, int ultrasonicReading) {
        this.angleOfServo = angleOfServo;
        this.ultrasonicReading = ultrasonicReading;
    }

    /**
     * Modifies the current Servo angle
     * @param newAngle
     */
    public void setAngle(int newAngle) {
        this.angleOfServo = newAngle;
    }

    /**
     * Retrieves the current Servo Angle
     * @return
     */
    public int getAngle() {
        return this.angleOfServo;
    }
}
