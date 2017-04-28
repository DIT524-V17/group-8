package group8.scam.model.communication;

import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import group8.scam.controller.handlers.HandleThread;

/**
 * @author sambac
 * This class is responsible for transmitting and
 * recieving data over the bluetooth socket created
 * in ConnectThread.
 */
public class DataThread extends Thread {

    /**
     * The mSocket is the object that holds the connection to the other device.
     * The mInStream is the object that will receive data from the other device through mSocket.
     * The mOutStream is the object that will transmit data to the other device through mSocket.
     */
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;

    /**
     * These static ints tell the handler what type of message that is going to be handled
     * and can then use apply different logic accordingly.
     */
    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;
    public static final String KEY = "default";

    /** Get the instance of HandleThread and store it in mHandleThread */
    private HandleThread mHandleThread = HandleThread.getInstance();

    /**
     * The constructor tries to get the InputStream and OutputStream
     * from the socket and store them in mInStream and mOutStream respectively.
     * @param mSocket the socket that was created in the ConnectThread class is passed into here.
     */
    public DataThread(BluetoothSocket mSocket) {
        this.mSocket = mSocket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = mSocket.getInputStream(); /* Tries to get the socket's InputStream */
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Initializing input stream failed.");
        }

        try {
            tmpOut = mSocket.getOutputStream(); /* Tries to get the socket's OutputStream */
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Initializing output stream failed.");
        }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    /**
     * In this run() method the data is constantly read from the car.
     * The data is read as individual characters one by one.
     * The characters are put into a string in the while loop.
     */
    public void run() {
        /*
         * An InputStreamReader is a bridge from byte streams to character streams:
         * It reads bytes and decodes them into characters using a specified charset.
         * A new InputStreamReader is created using the mInStream.
         */
        InputStreamReader in = new InputStreamReader(mInStream);

        /*
         * Reads text from a character-input stream,
         * buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
         * The BufferedReader is created using the InputStreamReader in.
         */
        BufferedReader reader = new BufferedReader(in);

        /* This while loop will run as long as there is a connection to the other Bluetooth device. */
        while (true) {
            try {
                String data = ""; /* This string is what will store the data from the Bluetooth device. */
                int singleByte; /* Holds the ASCII int value of a single character. */
                char ch; /* The character casted from singleByte is temporarily stored in this variable. */
                /*
                 * reader.read() reads the next character from the buffer and will return the character ASCII value as an Integer.
                 * The int returned is stored in singleByte. The condition is then checked and if the singleByte variable does
                 * not hold the value 0, it executes the code inside the loop. The value zero is an End Of Line character (EOF).
                 * An EOF character will appear at the end of a string. It is used here to check when to stop the operation
                 * reader.read(). The singleByte is converted into a character and is then appended to the string.
                 */
                while ((singleByte = reader.read()) != 0) {
                    ch = (char)singleByte;
                    data += ch;
                }

                /* Sends a message to the handler inside HandleThread with the data string. */
                mHandleThread.sendMessage(MESSAGE_READ, data);
            } catch (Exception e) {
                System.out.println("Loop broken in DataThread.");
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * This method attempts to transmit the data in form a of a byte array to the device it is connected to.
     * @param bytes The data is sent to the Bluetooth device byte by byte in a stream. The bytes are in this byte array.
     */
    public void write(byte[] bytes) {
        try {
            /*
             * Writes bytes from the specified byte array to this output stream.
             * The bytes are then transmitted to the Bluetooth device on the other side of the OutputStream.
             */
            mOutStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Couldn't send the data.");
            e.printStackTrace();
        }
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
            System.out.println("Couldn't cancel the activity.");
        }
    }
}
