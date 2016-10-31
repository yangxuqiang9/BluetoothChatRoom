package com.example.a328789.bluetoothcharroom.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.a328789.bluetoothcharroom.CharRoomActivity;

/**
 * Created by 328789 on 2016/10/14.
 */
public class MHandler extends Handler {
    private final Context context;

    public MHandler(Context context) {
        this.context=context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 0://蓝牙已开启
                Toast.makeText(context,"蓝牙已开启",Toast.LENGTH_SHORT).show();
                break;
            case 1://更新对话
                if(context instanceof CharRoomActivity){
                    CharRoomActivity charRoomActivity=(CharRoomActivity)context;
                    charRoomActivity.refreshContext((String) msg.obj);
                }
                break;
        }
    }
}
