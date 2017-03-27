package group8.scam.model.communication;

import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;

import java.util.ArrayList;
import java.util.Set;

import group8.scam.R;

/*
A class to handle connecting the bluetooth to the car. The first activity
@Author David Larsson
*/

//TODO - Add the items into the listview.
//TODO - Discover new items
//TODO - Connect to device

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ListView listView = (ListView) findViewById(R.id.listView);
        final BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
        final ProgressBar pgrBar = (ProgressBar) findViewById(R.id.pgrBar);

        // Turns the "loading" animation to invisible
        pgrBar.setVisibility(View.INVISIBLE);

        // Search Button
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!myBluetooth.isEnabled()){
                    // Bluetooth is disabled, enables it
                    Toast toast = Toast.makeText(ConnectActivity.this , "Turning on Bluetooth", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM,0,600);
                    toast.show();
                    myBluetooth.enable();

                    //TODO - Make sure the Bluetooth is fully enabled before continuing

                    // Turns on the "loading" animation
                    pgrBar.setVisibility(View.VISIBLE);

                    // Checks for previously paired devices
                    Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device
                        for (BluetoothDevice device : pairedDevices) {
                            //TODO - Add the results into the listview
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress();
                        }
                    }
                    else{
                        // There are no paired devices
                        Toast toast2 = Toast.makeText(ConnectActivity.this , "No paired devices", Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.BOTTOM,0,600);
                        toast2.show();
                    }

                }
                else{
                    // Bluetooth is enabled
                    pgrBar.setVisibility(View.VISIBLE);

                    Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            //TODO - Add the results into the listview
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress();
                        }
                    }
                    else{
                        // There are no paired devices
                        Toast toast2 = Toast.makeText(ConnectActivity.this , "No paired devices", Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.BOTTOM,0,600);
                        toast2.show();
                    }
                }
            }
        });






        /////////////////////////////TIS A SILLY PLACE//////////////////////////////////
        findViewById(R.id.DISABLEBUTTON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBluetooth.disable();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////


    }
}