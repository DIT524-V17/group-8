package group8.scam.controller.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel Bäckström
 * This class is responsible for sending the data from the HandleThread to the Observers.
 * An Observer is a class that implements the Observer interface.
 */

public class Subject {
    private static List<Observer> observers = new ArrayList<>(); /* Contains a list of all the observers. */

    /**
     * This method is used by the Observers to add their own instance to the observers list.
     * @param o The instance of the observer that is calling this method.
     */
    public static void add(Observer o) {
        observers.add(o);
    }

    /**
     * This method call the update() method of each Observer instance in the observer list.
     * @param data This string represent the data from the car. The data is unformatted. 
     */
    public void notifyObservers(String data) {
        for (Observer observer : observers) {
            observer.update(data);
        }
    }
}
