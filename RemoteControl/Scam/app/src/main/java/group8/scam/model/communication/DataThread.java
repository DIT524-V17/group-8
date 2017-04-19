package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import group8.scam.controller.handlers.HandleThread;

public class DataThread extends Thread {
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;

    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;
    public static final String KEY = "default";

    private HandleThread mHandleThread = HandleThread.getInstance();

    public DataThread(BluetoothSocket mSocket) {
        this.mSocket = mSocket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Initializing input stream failed.");
        }

        try {
            tmpOut = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Initializing output stream failed.");
        }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public void run() {

        final int BUFFER_SIZE = 1024;
        byte[] mBuffer = new byte[BUFFER_SIZE];
        int numBytes = 0;

        while (true) {
            try {
                numBytes = mInStream.read(mBuffer, numBytes, BUFFER_SIZE - numBytes);
                Message readMsg = mHandleThread.getHandler().obtainMessage(
                        MESSAGE_READ, numBytes, -1, mBuffer);
                readMsg.sendToTarget();
            } catch (Exception e) {
                System.out.println("Loop broken in DataThread.");
                e.printStackTrace();
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            String str = new String(bytes, Charset.defaultCharset());
            System.out.println(str);
            mOutStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Couldn't send the data.");
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't cancel the activity.");
        }
    }
}
