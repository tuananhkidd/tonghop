package com.kidd.baitapandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.IOException;
import java.security.Key;

public class OutGoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String incomingNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.i("reject1", "onReceive: " + incomingNum);
        if (incomingNum != null) {
            if (incomingNum.equals("0962918850")) {
//            Intent intent1 = new Intent(Intent.ACTION_MEDIA_BUTTON);
//            intent1.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
//            Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
//                    Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
//                            KeyEvent.KEYCODE_HEADSETHOOK));
//            String enforcedPerm = "android.permission.CALL_PRIVILEGED";
//            context.sendOrderedBroadcast(intent1, enforcedPerm);
//            context.sendOrderedBroadcast(btnUp, enforcedPerm);
//            Log.i("reject1", "onReceive: 123");
                setResultData(null);
                Toast.makeText(context, "This call is not allowed!", Toast.LENGTH_LONG).show();

            }
        }


        return;
    }
}
