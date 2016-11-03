package com.example.a328789.bluetoothcharroom.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a328789.bluetoothcharroom.R;

import java.util.List;

/**
 * Created by 328789 on 2016/10/14.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> implements View.OnClickListener {
    private final Context context;
    private List<BluetoothDevice> list;
    private DeviceItemAdapter.DeviceItemClickListener listener;

    public MyRecyclerViewAdapter(Context context,List list) {
        this.list=list;
        this.context=context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.main_bluetoothdevice_item, parent,false);
        inflate.setOnClickListener(this);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.MyHolder holder, int position) {
        BluetoothDevice bluetoothDevice = list.get(position);
        if(bluetoothDevice!=null){
            holder.device.setText(bluetoothDevice.getName());
            holder.addr.setText(bluetoothDevice.getAddress());
            holder.itemView.setTag(R.string.item_tag,bluetoothDevice);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setList(List<BluetoothDevice> list){
        this.list=list;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)
            listener.onItemClick(v,v.getTag(R.string.item_tag));
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public TextView device;
        public TextView addr;

        public MyHolder(View itemView) {
            super(itemView);
            device =  (TextView) itemView.findViewById(R.id.device);
            addr = (TextView) itemView.findViewById(R.id.addr);
        }
    }

    public void setOnItemClickListener(DeviceItemAdapter.DeviceItemClickListener listener){
        this.listener=listener;

    }
}
