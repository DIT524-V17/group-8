package group8.scam.controller.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sambac on 2017-04-28.
 */

public class Subject {
    private List<Observer> observers = new ArrayList<>(); /* Contains a list of all the observers. */

    public Subject() {

    }

    public void add(Observer o) {
        observers.add(o);
    }

    public void notifyObservers(String data) {
        for (Observer observer : observers) {
            observer.update(data);
        }
    }
}
