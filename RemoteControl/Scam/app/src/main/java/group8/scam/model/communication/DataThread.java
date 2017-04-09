package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import group8.scam.controller.handlers.HandleThread;

public class DataThread extends Thread {
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;
    private byte[] mBuffer;

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

        int numBytes;

        while (true) {
            try {
                mBuffer = new byte[1024];
                numBytes = mInStream.read(mBuffer);

                byte[] copyOfBuffer = new byte[numBytes];
                System.arraycopy(mBuffer, 0, copyOfBuffer, 0, numBytes);

                final Bundle bundle = new Bundle();
                bundle.putByteArray(KEY, copyOfBuffer);

                Message msg = mHandleThread.getHandler().obtainMessage();
                msg.what = MESSAGE_READ;
                msg.arg1 = numBytes;
                msg.arg2 = -1;
                msg.setData(bundle);

                mHandleThread.getHandler().sendMessage(msg);
            } catch (Exception e) {
                System.out.println("Loop broken in DataThread.");
                e.printStackTrace();
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
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
