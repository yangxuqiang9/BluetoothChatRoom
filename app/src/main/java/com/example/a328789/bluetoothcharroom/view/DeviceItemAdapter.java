package com.example.a328789.bluetoothcharroom.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a328789.bluetoothcharroom.R;

import java.util.List;

/**
 * Created by 328789 on 2016/10/14.
 */
public class DeviceItemAdapter extends RecyclerView.Adapter<DeviceItemAdapter.DeviceHolder> implements View.OnClickListener {
    private final Context context;
    private List<BluetoothDevice> list;
    private DeviceItemClickListener deviceItemClickListener;

    public DeviceItemAdapter(Context context) {
        this.context=context;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.device_name_item, null);
        inflate.setOnClickListener(this);
        return new DeviceHolder(inflate);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        if(list!=null){
            holder.name.setText(list.get(position).getName());
            holder.itemView.setTag(R.string.app_name,list.get(position));
        }
    }

    public void setData(List<BluetoothDevice> list){
        this.list=list;
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if(deviceItemClickListener!=null){
            deviceItemClickListener.onItemClick(v,v.getTag(R.string.app_name));
        }
    }

    public void setOnItemClickListener(DeviceItemClickListener deviceItemClickListener){
        this.deviceItemClickListener=deviceItemClickListener;
    }
    public interface DeviceItemClickListener{
        public abstract void onItemClick(View view, Object tag);
    }
    class DeviceHolder extends RecyclerView.ViewHolder{

        public  TextView name;

        public DeviceHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.device_name);
        }
    }
}
