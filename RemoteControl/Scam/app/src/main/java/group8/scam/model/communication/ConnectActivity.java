package group8.scam.model.communication;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import java.util.ArrayList;
import java.util.Set;

import group8.scam.R;

/*
    A class to handle connecting to the car. The first activity the user
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);

        registerReceiver(mReceiver, filter);

        // Turns the "loading" animation to invisible
        pgrBar.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Toast.makeText(ConnectActivity.this, adapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    // Button search
    public void btnSearch(View view) {
        if(!myBluetooth.isEnabled()){
            // Bluetooth is disabled, enables it
            Toast toast = Toast.makeText(ConnectActivity.this , "Turning on Bluetooth",
                    Toast.LENGTH_SHORT);
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
                        listView.clearChoices();
                        for (BluetoothDevice device : pairedDevices) {
                            listItems.add(device.getName() + "\n" + device.getAddress());
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        // There are no paired devices
                        Toast toast2 = Toast.makeText(ConnectActivity.this , "No paired devices",
                                Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.BOTTOM,0,600);
                        toast2.show();
                    }
                }
            }, 2000);// 2000 ms = 2s
            myBluetooth.startDiscovery();
            System.out.println("STARTED SEARCHING 1");
        }
        else{
            // Bluetooth is enabled
            pgrBar.setVisibility(View.VISIBLE);

            Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                adapter.clear(); // Clearing the adapter each time to not get any duplicates
                listView.clearChoices();
                for (BluetoothDevice device : pairedDevices) {
                    listItems.add(device.getName() + "\n" + device.getAddress());
                    adapter.notifyDataSetChanged();
                }
            }
            else{
                // There are no paired devices
                Toast toast = Toast.makeText(ConnectActivity.this , "No paired devices",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,600);
                toast.show();
            }
        }
        myBluetooth.startDiscovery();
        System.out.println("STARTED SEARCHING 2");
    }

    // Connect button
    public void btnConnect(View view) {
        //TODO - Connect to selected device
    }

    //Broadcast receiver for the bluetooth connection
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // TODO - It never gets here... :(
                System.out.println("FOUND DEVICE");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listItems.add(device.getName() + "\n" + device.getAddress());
                adapter.notifyDataSetChanged();
            }

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                // TODO - It never gets here either... :(
                System.out.println("FOUND DEVICE 2");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listItems.add(device.getName() + "\n" + device.getAddress());
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        myBluetooth.cancelDiscovery();
        super.onDestroy();
    }






        // Disable BT button, for testing - TODO REMOVE
    public void btnDisable(View view) {
        myBluetooth.cancelDiscovery();
        myBluetooth.disable();
        listView.clearChoices();
        listItems.clear();
        adapter.clear();
        pgrBar.setVisibility(View.INVISIBLE);
    }
}