package com.kidd.baitapandroid.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.kidd.baitapandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    TextView txt_state;
    Switch aSwitch;
    LinearLayout ll_result;
    ListView lv_bluetooth;
    TextView txt_no_result;
    Toolbar toolbar;
    BluetoothAdapter bluetoothAdapter;
    List<String> lsDevice;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initWidget();
        setDefault();
        scanBluetooth();
//        bluetoothScanning();
//        bluetoothAdapter.startDiscovery();

//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
//        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    void initWidget() {
        txt_no_result = findViewById(R.id.txt_no_result);
        txt_state = findViewById(R.id.txt_state);
        aSwitch = findViewById(R.id.switchblue);
        ll_result = findViewById(R.id.ll_result);
        lv_bluetooth = findViewById(R.id.lv_bluetooth);
        toolbar = findViewById(R.id.tool_bar);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lsDevice = new ArrayList<>();
        adapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lsDevice);
        lv_bluetooth.setAdapter(adapter);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.enable();
                        txt_state.setText("Bật");
                        ll_result.setVisibility(View.VISIBLE);
                        txt_no_result.setVisibility(View.VISIBLE);
                        scanBluetooth();
//                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                } else {
                    lsDevice.clear();
                    adapter.notifyDataSetChanged();
                    txt_state.setText("Tắt");
                    bluetoothAdapter.disable();
                    ll_result.setVisibility(View.GONE);
                    txt_no_result.setVisibility(View.GONE);
                }
            }
        });
    }

    void setDefault() {
        if (bluetoothAdapter.isEnabled()) {
            txt_state.setText("Bật");
            aSwitch.setChecked(true);
            ll_result.setVisibility(View.VISIBLE);
            txt_no_result.setVisibility(View.VISIBLE);
        } else {
            txt_state.setText("Tắt");
            aSwitch.setChecked(false);
            ll_result.setVisibility(View.GONE);
            txt_no_result.setVisibility(View.GONE);
        }
    }

    void scanBluetooth() {

        //if (bluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : bluetoothDevices) {
                Log.i("bluetooth1", "scanBluetooth: " + device.getName());
                lsDevice.add(device.getName());
            }
            adapter.notifyDataSetChanged();
            Log.i("bluetooth1", "scanBluetooth: " + bluetoothDevices.size());

        //}

    }

    void bluetoothScanning() {
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
//        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        bluetoothAdapter.startDiscovery();

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("bluetooth1", "onReceive: ");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.i("bluetooth1: ", "device " + deviceName);
                Log.i("bluetooth1 ", "hard" + deviceHardwareAddress);
            }
        }
    };
}
