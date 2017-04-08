package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import group8.scam.controller.handlers.IOHandler;

public class ReadWriteDataThread extends Thread {
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;
    private byte[] mBuffer;

    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;

    private IOHandler mHandler = IOHandler.getInstance();

    public ReadWriteDataThread(BluetoothSocket mSocket) {
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
        mBuffer = new byte[1024];
        int numBytes;

        while (true) {
            try {
                numBytes = mInStream.read(mBuffer);
                Message readMsg = mHandler.obtainMessage(MESSAGE_READ, numBytes, -1, mBuffer);
                System.out.println(readMsg.getData());
                System.out.println(readMsg.getTarget());
                readMsg.sendToTarget();
            } catch (IOException e) {
                System.out.println("Loop broken in ReadWriteDataThread.");
                e.printStackTrace();
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            mOutStream.write(bytes);



        } catch (IOException e) {

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
