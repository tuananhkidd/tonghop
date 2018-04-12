package com.kidd.baitapandroid;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kidd.baitapandroid.activity.BluetoothActivity;
import com.kidd.baitapandroid.activity.ContactActivity;
import com.kidd.baitapandroid.activity.WifiActivity;
import com.kidd.baitapandroid.receiver.OutGoingCallReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_contact;
    Button btn_wifi;
    Button btn_bluetooth;
    OutGoingCallReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_bluetooth = findViewById(R.id.btn_bluetooth);
        btn_wifi = findViewById(R.id.btn_wifi);
        btn_contact = findViewById(R.id.btn_contact);

        btn_bluetooth.setOnClickListener(this);
        btn_wifi.setOnClickListener(this);
        btn_contact.setOnClickListener(this);

        receiver = new OutGoingCallReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_contact: {
                startActivity(new Intent(this, ContactActivity.class));
                break;
            }
            case R.id.btn_wifi: {
                startActivity(new Intent(this, WifiActivity.class));
                break;
            }
            case R.id.btn_bluetooth: {
                startActivity(new Intent(this, BluetoothActivity.class));
                break;
            }
        }
    }
}
