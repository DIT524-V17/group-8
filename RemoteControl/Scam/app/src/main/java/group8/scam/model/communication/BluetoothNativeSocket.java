package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by David on 07-Apr-17.
 */

public class BluetoothNativeSocket implements BluetoothSocketWrapper {

    private BluetoothSocket socket;

    public BluetoothNativeSocket(BluetoothSocket tmp) {
        this.socket = tmp;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public String getRemoteDeviceName() {
        return socket.getRemoteDevice().getName();
    }

    @Override
    public void connect() throws IOException {
        socket.connect();
    }

    @Override
    public String getRemoteDeviceAddress() {
        return socket.getRemoteDevice().getAddress();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public BluetoothSocket getUnderlyingSocket() {
        return socket;
    }
}
