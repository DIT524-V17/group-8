package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by David on 07-Apr-17.
 */

public interface BluetoothSocketWrapper {

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getRemoteDeviceName();

    void connect() throws IOException;

    String getRemoteDeviceAddress();

    void close() throws IOException;

    BluetoothSocket getUnderlyingSocket();

}