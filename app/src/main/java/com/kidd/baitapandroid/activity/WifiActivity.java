package com.kidd.baitapandroid.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kidd.baitapandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class WifiActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_turn_on;
    Button btn_turn_off;
    Button btn_wifi_info;
    Button btn_scan;
    ToggleButton tButton;
    ListView lv_wifi;
    TextView txt_info;
    WifiManager wifiManager;
    Toolbar toolbar;
    private int PERMISSIONS_REQUEST_WIFI = 1;
    private List<ScanResult> results;
    private List<String> lsWifi;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        initData();
        chechWifiPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);

        MenuItem menuItem = menu.findItem(R.id.toggle);
        menuItem.setActionView(R.layout.wifi_switch);
        final Switch switchWifi = menu.findItem(R.id.toggle).getActionView().findViewById(R.id.switchwifi);
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle:

                tButton = (ToggleButton) findViewById(R.id.toggle);
                tButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                    }
                });

                return true;

            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WIFI) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //getContactList();
                //chechWifiPermission();
                scanAllWifi();
            } else {
                finish();
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }

    }

    void chechWifiPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, PERMISSIONS_REQUEST_WIFI);
        } else {
            //finish();
         //   scanAllWifi();
            Log.i("wifi1", "chechWifiPermission: ");
        }
    }

    private void initData() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        btn_scan = findViewById(R.id.btn_scan);
        btn_turn_on = findViewById(R.id.btn_on);
        btn_turn_off = findViewById(R.id.btn_off);
        btn_wifi_info = findViewById(R.id.btn_info);
        lv_wifi = findViewById(R.id.lv_wifi);
        txt_info = findViewById(R.id.txt_wifi_info);
        toolbar = findViewById(R.id.tool_bar);

        lsWifi = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lsWifi);
        lv_wifi.setAdapter(adapter);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_scan.setOnClickListener(this);
        btn_turn_on.setOnClickListener(this);
        btn_turn_off.setOnClickListener(this);
        btn_wifi_info.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_info: {
                txt_info.setVisibility(View.VISIBLE);
                lv_wifi.setVisibility(View.GONE);

                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiManager.isWifiEnabled()) {
                    if (wifiInfo.getBSSID() != null) {
                        int speed = wifiInfo.getLinkSpeed();
                        int strenght = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
                        String ssid = wifiInfo.getSSID();

                        txt_info.setText("SSID : " + ssid + " , speed : " + speed + " , strength : " + strenght);
                    }
                } else {
                    txt_info.setText("No wifi connected");
                }

                break;
            }
            case R.id.btn_on: {
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                break;
            }
            case R.id.btn_off: {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                }
                break;
            }
            case R.id.btn_scan: {
                lsWifi.clear();
                adapter.notifyDataSetChanged();
                txt_info.setVisibility(View.GONE);
                lv_wifi.setVisibility(View.VISIBLE);
                scanAllWifi();
                break;
            }
        }
    }

    void scanAllWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wifiManager.startScan();
            List<WifiConfiguration> networks = wifiManager.getConfiguredNetworks();
            for(WifiConfiguration wifiConfiguration:networks){
            }
            Iterator<WifiConfiguration> iterator = networks.iterator();
            while (iterator.hasNext()) {
                WifiConfiguration wifiConfig = iterator.next();
                Log.i("wifi1", "/scanAllWifi: "+wifiConfig.SSID);
                lsWifi.add(wifiConfig.SSID);
            }
            adapter.notifyDataSetChanged();

//            wifiManager.

//            for (WifiConfiguration wifiConfiguration : networks) {
//                Log.i("wifi1", "scanAllWifi: " + wifiConfiguration.SSID);
//            }
            //wm.reconnect();
        } else {
            wifiManager.startScan();

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context c, Intent intent) {
                    results = wifiManager.getScanResults();
                    //size = results.size();
                    Log.i("wifi1", "onReceive: " + results.size());

                    Collections.sort(results, new Comparator<ScanResult>() {
                        @Override
                        public int compare(ScanResult scanResult, ScanResult t1) {
                            if (scanResult.level >= t1.level) {
                                return 1;
                            }
                            return -1;
                        }
                    });
                    for (ScanResult scanResult : results) {
                        lsWifi.add(scanResult.SSID);
                    }
                    adapter.notifyDataSetChanged();
                    unregisterReceiver(this);
                }
            }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
