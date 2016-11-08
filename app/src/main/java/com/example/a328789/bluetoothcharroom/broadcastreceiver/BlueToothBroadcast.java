package com.example.a328789.bluetoothcharroom.broadcastreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.a328789.bluetoothcharroom.view.DeviceItemAdapter;

import java.util.ArrayList;

/**
 * Created by 328789 on 2016/10/14.
 */
public class BlueToothBroadcast extends BroadcastReceiver {
    private final DeviceItemAdapter adapter;
    private final ArrayList<BluetoothDevice> list;

    public BlueToothBroadcast(DeviceItemAdapter adapter) {
        this.adapter=adapter;
        list = new ArrayList<>();
        adapter.setData(list);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            BluetoothDevice bluetoothDevic = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.e("device:*****","正在搜索");
            if(bluetoothDevic!=null)
                list.add(bluetoothDevic);
            adapter.notifyDataSetChanged();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

        }
    }
}
