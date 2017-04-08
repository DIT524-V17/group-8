package group8.scam.model.communication;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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
import group8.scam.controller.handlers.HandleThread;
import group8.scam.model.menu.MenuActivity;


/*
    A class to handle connecting to the car. The first activity the user
    @Authors David Larsson & Samuel Bäckström
*/

//TODO - Discover new items

public class ConnectActivity extends AppCompatActivity {

    private ListView listView;
    private BluetoothAdapter bluetoothAdapter;
    private ProgressBar pgrBar;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<>();
    private BluetoothDevice device;
    private ConnectThread connection;
    private HandleThread mHandleThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pgrBar = (ProgressBar) findViewById(R.id.pgrBar);
        listView = (ListView) findViewById(R.id.listView);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        adapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);

        mHandleThread = HandleThread.getInstance();
        mHandleThread.start();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        registerReceiver(mReceiver, filter);

        pgrBar.setVisibility(View.INVISIBLE);

        // OnClickListener for the list, for selecting devices
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                // Gets the unique MAC adress from the list by splitting the string
                String macAdress = listItems.get(position);
                String[] array = macAdress.split("\\n");

                // Calls the method with the MAC adress
                selectDevice(array[1]);
            }
        });
    }


    // Button search
    public void btnSearch(View view) {
        if (bluetoothAdapter.isEnabled()) {

            // Bluetooth is enabled
            pgrBar.setVisibility(View.VISIBLE);

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                adapter.clear(); // Clearing the adapter each time to not get any duplicates
                listView.clearChoices();
                for (BluetoothDevice device : pairedDevices) {
                    listItems.add(device.getName() +"\n"+ device.getAddress());
                    adapter.notifyDataSetChanged();
                }
            } else {
                // There are no paired devices
                Toast toast = Toast.makeText(ConnectActivity.this, "No previously paired devices",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 600);
                toast.show();
            }

            //Asking permission for searching
            ActivityCompat.requestPermissions(ConnectActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

        } else {

            // Bluetooth is disabled, enables it
            Toast toast = Toast.makeText(ConnectActivity.this, "Turning on Bluetooth",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 600);
            toast.show();
            bluetoothAdapter.enable();

            pgrBar.setVisibility(View.VISIBLE);

            // Adding a handler so the BlueTooth Adapter has time to turn on properly before finding paired
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Checks for previously paired devices
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device
                        adapter.clear(); // Clearing the adapter each time to not get any duplicates
                        listView.clearChoices();
                        for (BluetoothDevice device : pairedDevices) {
                            listItems.add(device.getName() +"\n"+ device.getAddress());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        // There are no paired devices
                        Toast toast2 = Toast.makeText(ConnectActivity.this, "No paired devices",
                                Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.BOTTOM, 0, 600);
                        toast2.show();
                    }

                    //Asking permission for searching
                    ActivityCompat.requestPermissions(ConnectActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);

                }
            }, 2000);// 2000 ms = 2s
        }
    }


    // Connect button
    public void btnConnect(View view) {
        if (device != null) {
            connection = new ConnectThread(device);
            connection.start();
            mHandleThread.setConnection(connection);

            // Toast of clarity
            Toast toast = Toast.makeText(ConnectActivity.this, "Connecting",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 600);
            toast.show();
        }
    }


    // Method to select bluetooth device from the list
    public void selectDevice(String macAdress){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if(macAdress.equals(device.getAddress())){
                this.device = device;
            }
        }
    }


    //Broadcast receiver for the bluetooth connection
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Found device in discovering
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                System.out.println("FOUND DEVICE");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!(device.getBondState() == device.BOND_BONDED)){
                    listItems.add(device.getName() + "\n" + device.getAddress());
                    adapter.notifyDataSetChanged();
                }
            }

            // Connected to device
            else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
                System.out.println("IM IN MOTHERFUCKER! TINY RIIIICK!");

                // Switching to menu activity
                startActivity(new Intent(ConnectActivity.this, MenuActivity.class));
                unregisterReceiver(mReceiver);
            }
        }
    };

    public ConnectThread getConnection() {
        return connection;
    }

    // Requesting permission for ACCESS_COURSE_LOCATION. Needed for discovering new devices
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // The permission was granted, start discovery
                    bluetoothAdapter.startDiscovery();
                    System.out.println("Started searching");

                } else {
                    Toast.makeText(ConnectActivity.this, "Permission denied!",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //Destroy them all
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.disable();
    }
}
