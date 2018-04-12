package com.kidd.baitapandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class OutGoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("reject9", "onReceive: ");
        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).toString();
        if (phoneNumber.equals("0962918850")) {
            setResultData(null);
            Toast.makeText(context, "This call is not allowed", Toast.LENGTH_SHORT).show();
        }
    }
}
