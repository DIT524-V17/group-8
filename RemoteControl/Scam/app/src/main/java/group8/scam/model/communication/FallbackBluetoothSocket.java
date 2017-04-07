package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by David on 07-Apr-17.
 */

public class FallbackBluetoothSocket extends BluetoothNativeSocket {

    private BluetoothSocket fallbackSocket;

    public FallbackBluetoothSocket(BluetoothSocket tmp)  {
        super(tmp);

        try {
            Class<?> clazz = tmp.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
            Method m = null;
            m = clazz.getMethod("createRfcommSocket", paramTypes);
            Object[] params = new Object[] {Integer.valueOf(1)};
            fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return fallbackSocket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return fallbackSocket.getOutputStream();
    }


    @Override
    public void connect() throws IOException {
        fallbackSocket.connect();
    }


    @Override
    public void close() throws IOException {
        fallbackSocket.close();
    }
}
