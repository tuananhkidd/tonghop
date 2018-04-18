package com.kidd.baitapandroid;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kidd.baitapandroid.activity.BluetoothActivity;
import com.kidd.baitapandroid.activity.ContactActivity;
import com.kidd.baitapandroid.activity.WifiActivity;
import com.kidd.baitapandroid.receiver.OutGoingCallReceiver;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST = 1;
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

        checkPermission();
        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(srvcName);
        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                String callStateStr = "Unknown";
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        callStateStr = "idle";
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        callStateStr = "offhook";
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        callStateStr = "ringing. Incoming number is: "
                                + incomingNumber;
                        break;
                    default:
                        break;
                }
                Log.i(TAG+"state", "onCallStateChanged: " + callStateStr);
            }
        };

        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getContactList();
                //getCallLog();
                // register();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(receiver);
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

    private final String TAG = getClass().getSimpleName();
    String incommingNumber;
    String incno1 = "9916090941";

    //  @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (null == bundle)
            return;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            // Java reflection to gain access to TelephonyManager's
            // ITelephony getter
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Log.v(TAG, "Get getTeleService...");
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
//            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            Bundle b = intent.getExtras();
            incommingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.v(TAG, incommingNumber);
            Log.v(TAG, incno1);
            if (incommingNumber.equals(incno1)) {
                // telephonyService = (ITelephony) m.invoke(tm);
                // telephonyService.silenceRinger();
                // telephonyService.endCall();
                Log.v(TAG, "BYE BYE BYE");
            } else {
                //telephonyService.answerRingingCall();
                Log.v(TAG, "HELLO HELLO HELLO");
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,
                    "FATAL ERROR: could not connect to telephony subsystem");
            Log.e(TAG, "Exception object: " + e);
        }
    }
}
