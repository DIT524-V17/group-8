package group8.scam.controller.handlers;

/**
 * Created by sambac on 2017-04-03.
 */

class JoystickHandler {
    private static final JoystickHandler ourInstance = new JoystickHandler();

    static JoystickHandler getInstance() {
        return ourInstance;
    }

    private JoystickHandler() {
    }
}
