package group8.scam.model.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    private DataThread dataThread;

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mDevice = device;

        try {
            tmp = mDevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
        } catch (IOException exception) {
            System.out.println(exception);
        }

        mSocket = tmp;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            mSocket.connect();
        } catch (IOException e) {
            try {
                mSocket = (BluetoothSocket) mDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mDevice,1);
                mSocket.connect();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e1) {
                e1.printStackTrace();
                System.out.println(e1);
            }
        }

        System.out.println("Is connected to the device: " + mSocket.isConnected());
        manageThread();
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not close the socket.");
        }
    }

    private void manageThread() {
        dataThread = new DataThread(mSocket);
        dataThread.start();
    }

    public DataThread getDataThread() {
        return dataThread;
    }
}
