package group8.scam.controller.handlers;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.Set;

import group8.scam.R;
import group8.scam.model.communication.ConnectThread;
import group8.scam.model.main.MainActivity;

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
                        String writeStr = (String) msg.obj;
                        if (writeStr != null) {
                            byte[] strBytes = writeStr.getBytes(Charset.defaultCharset());
                            connection.getDataThread().write(strBytes);
                        }
                        break;
                    case MESSAGE_READ:
                        String readStr = (String)msg.obj;
                        if (readStr != null) {
                            System.out.println(readStr);
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
