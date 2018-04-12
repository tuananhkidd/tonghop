package com.kidd.baitapandroid.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.kidd.baitapandroid.R;
import com.kidd.baitapandroid.common.Utils;
import com.kidd.baitapandroid.models.CallLogContact;

import java.util.List;

public class CallLogAdapter extends ArrayAdapter<CallLogContact> {
    private Context context;
    private int layout;
    private List<CallLogContact> callLogs;

    public CallLogAdapter(@NonNull Context context, int resource, @NonNull List<CallLogContact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.callLogs = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;
        if (convertView == null) {
            viewholder = new Viewholder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_call_log, parent, false);
            viewholder.img_type = convertView.findViewById(R.id.img_type);
            viewholder.txt_date = convertView.findViewById(R.id.txt_date);
            viewholder.txt_time = convertView.findViewById(R.id.txt_time);
            viewholder.txt_type = convertView.findViewById(R.id.txt_type);
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }
        CallLogContact callLog = callLogs.get(position);
        switch (callLog.getType()) {
            case CallLog.Calls.OUTGOING_TYPE:
                viewholder.img_type.setImageResource(R.drawable.ic_call_out);
                viewholder.txt_type.setText("Cuộc gọi đi");
                break;

            case CallLog.Calls.INCOMING_TYPE:
                viewholder.img_type.setImageResource(R.drawable.ic_call_in);
                viewholder.txt_type.setText("Cuộc gọi đến");
                break;

            case CallLog.Calls.MISSED_TYPE:
                viewholder.img_type.setImageResource(R.drawable.ic_misscall);
                viewholder.txt_type.setText("Cuộc gọi nhỡ");
                break;
            case CallLog.Calls.REJECTED_TYPE:{
                viewholder.img_type.setImageResource(R.drawable.ic_call_reject);
                viewholder.txt_type.setText("Cuộc bị từ chối");
                break;
            }
        }
        int duration = callLog.getDuration();
        if (duration <= 0) {
            viewholder.txt_time.setVisibility(View.GONE);

        } else if (duration < 60) {
            viewholder.txt_time.setVisibility(View.VISIBLE);
            viewholder.txt_time.setText(duration + " giây");
        } else {
            viewholder.txt_time.setVisibility(View.VISIBLE);
            int min = duration / 60;
            int sec = duration - min * 60;
            viewholder.txt_time.setText(duration / 60 + " phút " + sec + " giây");
        }
        viewholder.txt_date.setText(Utils.getTimeFromMilliseconds(callLog.getDate()));
        return convertView;
    }

    class Viewholder {
        private ImageView img_type;
        private TextView txt_date;
        private TextView txt_time;
        private TextView txt_type;

    }
}
