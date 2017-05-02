package group8.scam.model.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;


/**
 * @author Samuel Bäckström
 * This class is responisble for opening a connection over Bluetooth to another device.
 */
public class ConnectThread extends Thread {

    /**
     * A connected or connecting Bluetooth socket.
     * Open the IO streams by calling getInputStream() and getOutputStream()
     * in order to retrieve InputStream and OutputStream objects, respectively,
     * which are automatically connected to the socket.
     */
    private BluetoothSocket mSocket;

    /**
     * Represents a remote Bluetooth device.
     * A BluetoothDevice lets you create a connection with
     * the respective device or query information about it,
     * such as the name, address, class, and bonding state.
     */
    private BluetoothDevice mDevice;

    private DataThread dataThread;

    /**
     * This constructor will try to create a socket to the device, it is through a socket two
     * bluetooth devices can communicate through.
     * @param device this is the device that has been selected by the user in ConnectActivity
     */
    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mDevice = device;

        try {
            tmp = mDevice.createRfcommSocketToServiceRecord(UUID.randomUUID()); /* Creates the socket to the device */
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        mSocket = tmp; /* Stores the socket in mSocket if it is created successfully */
    }

    /**
     * In this run() method a connection through the socket to the bluetooth device is attempted.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {

        /**
         * Device discovery is a heavyweight
         * procedure on the Bluetooth adapter
         * and will significantly slow a device connection.
         * That's why it is canceled here.
         **/
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            mSocket.connect(); /* Attempt to connect to a remote device. */
        } catch (IOException e) {
            try { /* If the first attempt fails, another attempt is made with a different type of socket. */
                mSocket = (BluetoothSocket) mDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mDevice,1);
                mSocket.connect();
                System.out.println("Socket created");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e1) {
                e1.printStackTrace();
                System.out.println(e1);
            }
        }

        System.out.println("Is connected to the device: " + mSocket.isConnected()); /* Returns true if the connection is successful */
        manageThread();
    }

    /**
     * Closes this socket and releases any system resources associated with it.
     * If the stream is already closed then invoking this method has no effect
     */
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not close the socket.");
        }
    }

    /**
     * Creates a new DataThread with the mSocket as a parameter.
     */
    private void manageThread() {
        dataThread = new DataThread(mSocket);
        dataThread.start();
    }

    /**
     * Getter for the dataThread variable.
     * @return dataThread
     */
    public DataThread getDataThread() {
        return dataThread;
    }
}
