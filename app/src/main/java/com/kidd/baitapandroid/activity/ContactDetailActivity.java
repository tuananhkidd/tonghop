package com.kidd.baitapandroid.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidd.baitapandroid.R;
import com.kidd.baitapandroid.adapter.CallLogAdapter;
import com.kidd.baitapandroid.common.Constanst;
import com.kidd.baitapandroid.models.CallLogContact;
import com.kidd.baitapandroid.models.Contact;
import com.kidd.baitapandroid.receiver.OutGoingCallReceiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_SMS = 3;
    TextView txt_name;
    TextView txt_phone;
    ListView lv_history;
    ImageView btn_call;
    ImageView btn_send_message;
    Contact contact;
    Toolbar toolbar;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int PERMISSIONS_REQUEST_CALL = 2;
    List<CallLogContact> lsCallLog;
    CallLogAdapter adapter;
    int select = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        initData();

        showCallLog();
//        registerReceiver(receiver)
    }

    public void initData() {
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phonenumber);
        lv_history = findViewById(R.id.lv_history);
        toolbar = findViewById(R.id.tool_bar);

        btn_call = findViewById(R.id.btn_call);
        btn_send_message = findViewById(R.id.btn_send_message);
        btn_call.setOnClickListener(this);
        btn_send_message.setOnClickListener(this);
        lsCallLog = new ArrayList<>();
        adapter = new CallLogAdapter(this, R.layout.item_call_log, lsCallLog);
        lv_history.setAdapter(adapter);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (getIntent().getExtras() != null) {
            contact = (Contact) getIntent().getSerializableExtra(Constanst.CONTACT);
        }

        txt_phone.setText(contact.getPhonenumber());
        txt_name.setText(contact.getName());

    }

    void getCallLog() {
        Uri allCalls = Uri.parse("content://call_log/calls");
        Cursor c = managedQuery(allCalls, null, CallLog.Calls.NUMBER + " = ? ",
                new String[]{contact.getPhonenumber()}, CallLog.Calls.DATE + " DESC");

        c.moveToFirst();
        //if(c.moveToFirst()){
        while (c.moveToNext()) {
            String num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));// for  number
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));// for name
            int duration = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.DURATION)));// for duration
            int type = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));// for call type, Incoming or out going
            long date = Long.parseLong(c.getString(c.getColumnIndex(CallLog.Calls.DATE)));

            lsCallLog.add(new CallLogContact(name, num, duration, type, date));
            Log.i("Call", "getCallLog: " + name + "  " + num + "  " + duration + "  " + type + "  " + new Date(Long.valueOf(date)));
        }

        Log.i("call", "getCallLog: " + lsCallLog.size());
        adapter.notifyDataSetChanged();
        // }

    }

    private void showCallLog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            getCallLog();
            //getContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getContactList();
                getCallLog();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission Deny", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contact.getPhonenumber(), null, "ahihi", null, null);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } else {

            }
        }

    }

    void getInfoCall() {
        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(srvcName);

        String phoneTypeStr = "unknown";
        int phoneType = telephonyManager.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                phoneTypeStr = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                phoneTypeStr = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                phoneTypeStr = "SIP";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                phoneTypeStr = "None";
                break;
            default:
                break;
        }

        Log.i("Call State", phoneTypeStr);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//              return;
//        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_call: {
                String uri = "tel:" + contact.getPhonenumber().trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL);
                    return;
                }
                startActivity(intent);
                break;
            }
            case R.id.btn_send_message: {
                String[] lsChoice = new String[]{"Build-in Message", "None Build-in Message"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactDetailActivity.this);
                builder.setSingleChoiceItems(lsChoice, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        select = i;
                    }
                });

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (select) {
                            case 0: {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.getPhonenumber()));
                                intent.putExtra("sms_body", "demo");
                                startActivity(intent);
                                break;
                            }
                            case 1: {
                                if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SMS);
                                    return;
                                }
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(contact.getPhonenumber(), null, "ahihi", null, null);
                                Toast.makeText(ContactDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
                builder.show();
            }
        }
    }
}
