package group8.scam.model.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import static android.content.ContentValues.TAG;

public class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;
        private BluetoothAdapter bluetoothAdapter;

        public ConnectThread(BluetoothAdapter bluetoothAdapter, BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            this.bluetoothAdapter = bluetoothAdapter;
            BluetoothSocket tmp = null;
            mmDevice = device;
            System.out.println("IN CONSTRUCT");

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device,1);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            System.out.println("RUNNING");
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            System.out.println(mmSocket.getRemoteDevice());

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                System.out.println("TRYING TO CONNECT");
                mmSocket.connect();
                System.out.println("CONNECTED");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket = new FallbackBluetoothSocket(mmSocket.)
                } catch () {

                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            // manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
