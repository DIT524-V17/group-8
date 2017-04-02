package group8.scam.model.communication;

import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
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

//TODO - Discover new items
//TODO - Connect to device

public class ConnectActivity extends AppCompatActivity {

    private ListView listView;
    private BluetoothAdapter myBluetooth;
    private ProgressBar pgrBar;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting up
        pgrBar = (ProgressBar) findViewById(R.id.pgrBar);
        listView = (ListView) findViewById(R.id.listView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        adapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);

        // Turns the "loading" animation to invisible
        pgrBar.setVisibility(View.INVISIBLE);

    }

    // Button search
    public void btnSearch(View view) {
        if(!myBluetooth.isEnabled()){
            // Bluetooth is disabled, enables it
            Toast toast = Toast.makeText(ConnectActivity.this , "Turning on Bluetooth", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,600);
            toast.show();
            myBluetooth.enable();

            pgrBar.setVisibility(View.VISIBLE);

            // Adding a handler so the BlueTooth Adapter has time to turn on properly before finding paired
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Checks for previously paired devices
                    Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device
                        adapter.clear(); // Clearing the adapter each time to not get any duplicates
                        for (BluetoothDevice device : pairedDevices) {
                            listItems.add(device.getName());
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        // There are no paired devices
                        Toast toast2 = Toast.makeText(ConnectActivity.this , "No paired devices", Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.BOTTOM,0,600);
                        toast2.show();
                    }

                }
            }, 2000);}
            // 2000 ms = 2s

        else{
            // Bluetooth is enabled
            pgrBar.setVisibility(View.VISIBLE);

            Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                adapter.clear(); // Clearing the adapter each time to not get any duplicates
                for (BluetoothDevice device : pairedDevices) {
                    listItems.add(device.getName());
                    adapter.notifyDataSetChanged();

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

    // Disable BT button, for testing - TODO REMOVE
    public void btnDisable(View view) {
        myBluetooth.disable();
    }
}