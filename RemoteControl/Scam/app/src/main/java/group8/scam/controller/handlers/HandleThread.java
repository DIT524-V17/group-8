package group8.scam.controller.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Set;

import group8.scam.model.communication.ConnectThread;

import static group8.scam.model.communication.DataThread.KEY;
import static group8.scam.model.communication.DataThread.MESSAGE_READ;
import static group8.scam.model.communication.DataThread.MESSAGE_TOAST;
import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

/**
 * Created by sambac on 2017-04-07.
 */

public class HandleThread extends Thread {
    private static final HandleThread INSTANCE = new HandleThread();
    private ConnectThread connection;
    private Handler mHandler;

    private HandleThread() {

    }

    public static HandleThread getInstance() {
        return INSTANCE;
    }

    public void setConnection(ConnectThread connection) {
        this.connection = connection;
    }

    public ConnectThread getConnection() {
        return connection;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void run() {
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_WRITE:
                        connection.getDataThread().write(msg.getData().getByteArray(KEY));
                        break;
                    case MESSAGE_READ:
                        break;
                    case MESSAGE_TOAST:
                        break;
                }
            }
        };

        Looper.loop();
    }
}
