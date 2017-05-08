package group8.scam.controller.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.nio.charset.Charset;

import group8.scam.model.communication.ConnectThread;

import static group8.scam.model.communication.DataThread.MESSAGE_READ;
import static group8.scam.model.communication.DataThread.MESSAGE_TOAST;
import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

/**
 * @author Samuel Bäckström
 * This thread handles the input and output sent by the car and app.
 * The class implements the singleton pattern.
 */

public class HandleThread extends Thread {

    private static final HandleThread INSTANCE = new HandleThread();
    private ConnectThread connection;
    private Handler mHandler;
    private Subject subject;

    private HandleThread() {
        subject = new Subject();
    }

    /** @return INSTANCE Returns the instance of this class. */
    public static HandleThread getInstance() {
        return INSTANCE;
    }

    /* Method called from ConnectActivity to get the ConnectThread instance. */
    public void setConnection(ConnectThread connection) {
        this.connection = connection;
    }

    /** @return connection returns the ConnectThread instance. */
    public ConnectThread getConnection() {
        return connection;
    }

    /** @return mHandler returns the Handler. */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Message documentation: Defines a message containing a description and arbitrary data
     * object that can be sent to a Handler.
     * Creates and sends a Message to the Handler with the passed parameters.
     * @param what What kind of message is it, either MESSAGE_WRITE or MESSAGE_READ.
     * @param obj The object that is to be handled by the Handler
     * */
    public void sendMessage(int what, String obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        msg.sendToTarget();
    }

    /**
     * This method run a message loop for a thread.
     * Threads by default do not have a message loop associated with them; to create one,
     * Looper.prepare() is called in the thread that is to run the loop,
     * and then Looper.loop() to have it process messages until the loop is stopped.
     * handleMessage(msg) method is what handles the messages.
     */
    public void run() {
        if (Looper.myLooper() == null)
            Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) { /* Checks what type of message it is */
                    case MESSAGE_WRITE:
                        String writeStr = (String) msg.obj; /* msg.obj is of type Object so it has to be cast into String */
                        if (writeStr != null) {
                            byte[] strBytes = writeStr.getBytes(Charset.defaultCharset()); /* Converts the string into a byte array */
                            connection.getDataThread().write(strBytes); /* Send the byte array to the DataThread where it will be transmitted to the car */
                        }
                        break;
                    case MESSAGE_READ:
                        String readStr = (String)msg.obj; /* msg.obj is of type Object so it has to be cast into String */
                        if (readStr != null) {
                            subject.notifyObservers(readStr);
                        }
                        break;
                    case MESSAGE_TOAST:
                        break;
                }
            }
        };

        Looper.loop();
    }
}
