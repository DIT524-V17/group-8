package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        InputStreamReader in = new InputStreamReader(mInStream);
        BufferedReader reader = new BufferedReader(in);

        char[] buffer = new char[100];
        int bytes = 0;

        while (true) {
            try {
                String data = "";
                int singleByte;
                char ch;
                while ((singleByte = reader.read()) != 0) {
                    ch = (char)singleByte;
                    data += ch;
                }

                Message msg = mHandleThread.getHandler().obtainMessage();
                msg.what = MESSAGE_READ;
                msg.obj = data;
                msg.sendToTarget();
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
