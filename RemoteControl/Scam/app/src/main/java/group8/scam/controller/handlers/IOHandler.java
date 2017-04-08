package group8.scam.controller.handlers;

import android.os.Handler;

import group8.scam.model.communication.ConnectThread;

/**
 * Created by sambac on 2017-04-07.
 */

public class IOHandler extends Handler {
    private static final IOHandler mHandler = new IOHandler();
    private ConnectThread connection;

    private IOHandler() {

    }

    public static IOHandler getInstance() {
        return mHandler;
    }

    public void setConnection(ConnectThread connection) {
        this.connection = connection;
    }
}
