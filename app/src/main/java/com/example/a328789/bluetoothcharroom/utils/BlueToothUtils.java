package com.example.a328789.bluetoothcharroom.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.a328789.bluetoothcharroom.MainActivity;
import com.example.a328789.bluetoothcharroom.broadcastreceiver.BlueToothBroadcast;
import com.example.a328789.bluetoothcharroom.view.DeviceItemAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 328789 on 2016/10/14.
 */
public class BlueToothUtils {

    private static  BluetoothAdapter defaultAdapter;
    private static BlueToothBroadcast blueToothBroadcast;
    private static BluetoothSocket rfcommSocketToServiceRecord=null;
    private static InputStream reading;
    private static OutputStream write;
    private static  UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static  MHandler mHandler;

    public BlueToothUtils(MHandler mHandler) {
        this.mHandler=mHandler;
        defaultAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static void openBT(MainActivity main,Handler mhandler){
        if(!defaultAdapter.isEnabled()){
            //直接打开蓝牙
//            defaultAdapter.enable();
            //询问用户
            main.openBT();
        }else{
            mhandler.sendEmptyMessage(0);
        }


    }
    public static void closeBT(){
        defaultAdapter.disable();
        if(rfcommSocketToServiceRecord!=null){
            try {
                rfcommSocketToServiceRecord.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void connectBTThread(final BluetoothDevice device){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                connectBT(device);
                Looper.loop();
            }
        }).start();
    }

    public static void readThread(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                super.run();
                read();
                Looper.loop();
            }
        }.start();
    }

    public static void writeThread(final String s){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                write(s);
                Looper.loop();
            }
        }.start();
    }


    public static void connectBT(BluetoothDevice device){


        try {
            rfcommSocketToServiceRecord = device.createRfcommSocketToServiceRecord(uuid);
//            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
//            rfcommSocketToServiceRecord = (BluetoothSocket) m.invoke(device, Integer.valueOf("2"));

        }
        catch (IOException e) {
            try {
                rfcommSocketToServiceRecord.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            Log.e("d","蓝牙连接失败");
        }
//        catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        try {
            rfcommSocketToServiceRecord.connect();

            write = rfcommSocketToServiceRecord.getOutputStream();
            reading=rfcommSocketToServiceRecord.getInputStream();
            Log.e("e","已经连接");
        } catch (IOException e) {
            try {
                rfcommSocketToServiceRecord.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            Log.e("d","蓝牙连接失败");
            accept(device);
        }

        read();

    }
    public static void accept(BluetoothDevice device){
        try {
            BluetoothServerSocket bluetoothServerSocket = defaultAdapter.listenUsingRfcommWithServiceRecord(device.getName(), uuid);
            BluetoothSocket accept = bluetoothServerSocket.accept();
//            reading = accept.getInputStream();
//            Method createRfcommSocket = defaultAdapter.getClass().getMethod("listenUsingRfcommOn", new Class[]{int.class});
//            BluetoothServerSocket invoke = (BluetoothServerSocket) createRfcommSocket.invoke(defaultAdapter, Integer.valueOf("2"));
//            BluetoothSocket accept = invoke.accept();
            reading=accept.getInputStream();
            write=accept.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        read();
    }
    public static void write(String s){
        if(write!=null){
            try {
                write.write(gethex(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        read();
    }
    public static String read(){
//        accept();
        StringBuffer stringBuffer = new StringBuffer();

        try {
            int available = reading.available();
            if(reading!=null){
                int len=0;
                byte[] b=new byte[1024];
                try {
                    if((len=reading.read(b))!=-1){
                        stringBuffer.append(reading.read(b,0,len));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    public static List<BluetoothDevice> getBTDivers(){
        Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();
        List<BluetoothDevice> list= new ArrayList<BluetoothDevice>();
        list.addAll(bondedDevices);
        return list;
    }
    public static void searchDivers(Context context, DeviceItemAdapter adpter){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        blueToothBroadcast = new BlueToothBroadcast(adpter);
        context.registerReceiver(blueToothBroadcast,intentFilter);
        defaultAdapter.startDiscovery();

    }
    public static void stopSearch(Context context){
        if(blueToothBroadcast!=null){
            context.unregisterReceiver(blueToothBroadcast);
        }

    }
    public void createBound(BluetoothDevice b){
        Class<BluetoothDevice> classBT = BluetoothDevice.class;
        try {
            Method createBond = classBT.getMethod("createBond");
            boolean invoke = (boolean) createBond.invoke(b);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e("f","获取配对方法失败");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public BluetoothDevice getRemote(String adress){
       return defaultAdapter.getRemoteDevice(adress);
    }
    public static byte[] gethex(String s){
        String hexRaw = null;
        try {
            hexRaw = String.format("%x", new BigInteger(1, s.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        char[] hexRawArr = hexRaw.toCharArray();
        StringBuilder hexFmtStr = new StringBuilder();
//        final String SEP = "\\x";
        final String SEP = "";
        for (int i = 0; i < hexRawArr.length; i++) {
            hexFmtStr.append(SEP).append(hexRawArr[i]).append(hexRawArr[++i]);
        }
        return hexFmtStr.toString().getBytes();
    }

}
